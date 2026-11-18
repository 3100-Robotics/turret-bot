package frc.robot.vision;

import com.sbdc.loggerhead.core.LightSubsystem;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.VisionConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.VisionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class MainVision extends LightSubsystem implements Loggable {
  @FunctionalInterface
  public static interface EstimateConsumer {
    public void accept(Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs);
  }

  public static final AprilTagFieldLayout kTagLayout = VisionConstants.kTagLayout;

  // Simulation
  private VisionSystemSim visionSim;
  private Supplier<Pose2d> simDTGetter;
  public final Drivetrain drivetrain;

  private List<Camera> cameras = new ArrayList<>();

  private final EstimateConsumer poseOutput;
  private final Function<Double, Optional<Pose2d>> poseAtFpgaTime;
  private final DoubleSupplier yawRateRadPerSec;

  private double ignoreResultsBefore = 0.0; // FPGA seconds
  private boolean headingResetPending = false;

  // BS about the alpha sigma pose
  private boolean peakThisLoop = false;
  private double lastPeakFpgaTime = Double.NEGATIVE_INFINITY;
  private int peakCount = 0;

  /**
   * @param estConsumer Lamba that will accept a pose estimate and pass it to your desired {@link
   *     org.wpilib.math.estimator.SwerveDrivePoseEstimator}
   */
  @SuppressWarnings("unused")
  public MainVision(
      EstimateConsumer estConsumer, Supplier<Pose2d> simDTGetter, Drivetrain drivetrain) {
    this.simDTGetter = simDTGetter;
    this.drivetrain = drivetrain;
    this.poseOutput = estConsumer;

    yawRateRadPerSec = () -> drivetrain.getState().Speeds.omegaRadiansPerSecond;
    poseAtFpgaTime = drivetrain::samplePoseAt;

    cameras.add(VisionConstants.CAM_BACK_LEFT);
    cameras.add(VisionConstants.CAM_FRONT_RIGHT);

    for (Camera camera : cameras) {
      camera.setPoseOutput(poseOutput);
      camera.setHeadingSupplier(
          () -> {
            return Pair.of(obtainDrivetrainRotation(drivetrain), Timer.getTimestamp());
          });
    }

    if ((!Robot.isReal()) && VisionConstants.simulateCoproc) {
      visionSim = new VisionSystemSim("main");
      visionSim.addAprilTags(kTagLayout);
      for (Camera camera : cameras) {
        visionSim.addCamera(camera.sim, camera.robotToCam);
        camera.setSimField(getSimDebugField());
      }
    }
  }

  public Rotation3d obtainDrivetrainRotation(Drivetrain drivetrain) {
    var e = drivetrain.getState().RawHeading;
    return drivetrain.getRotation3d();
  }

  public void onPoseReset() {
    ignoreResultsBefore = Timer.getFPGATimestamp();
    headingResetPending = true; // seeded next loop, once the drivetrain history has new data
    for (Camera c : cameras) {
      c.peakFrameStreak = 0;
    }
  }

  public boolean gotPeakPoseThisLoop() {
    return peakThisLoop;
  }

  public double secondsSinceLastPeakPose() {
    return Timer.getFPGATimestamp() - lastPeakFpgaTime;
  }

  public int getPeakPoseCount() {
    return peakCount;
  }

  private void seedHeadingBuffers() {
    double now = Timer.getFPGATimestamp();
    Optional<Pose2d> p = poseAtFpgaTime.apply(now);
    if (p.isEmpty()) {
      System.out.println("Seeding failed!");
      return; // try again next loop
    }
    System.out.println("Seeding sucess!");
    for (Camera camera : cameras) {
      camera.photonEstimator.resetHeadingData(now, p.get().getRotation());
    }
    headingResetPending = false;
  }

  private boolean isPeakCandidate(
      EstimatedRobotPose seed,
      boolean seedIsMultiTag,
      List<PhotonTrackedTarget> targets,
      double linearStdDev,
      Rotation2d gyroHeading) {
    if (linearStdDev > VisionConstants.peakMaxLinearStdDev) {
      return false;
    }
    if (Math.abs(yawRateRadPerSec.getAsDouble()) > VisionConstants.peakMaxYawRate) {
      return false;
    }
    if (!seedIsMultiTag) {
      if (!VisionConstants.allowSingleTagPeak) {
        return false;
      }
      double amb = VisionUtils.lowestAmbiguity(targets);
      if (amb < 0 || amb > VisionConstants.peakMaxAmbiguity) {
        return false;
      }
    }
    if (!DriverStation.isDisabled()
        && Math.abs(VisionUtils.headingError(seed.estimatedPose.toPose2d(), gyroHeading))
            > VisionConstants.peakMaxCorrectionWhileEnabled) {
      return false;
    }
    return true;
  }

  private Optional<EstimatedRobotPose> solveConstrained(
      Camera camera, PhotonPipelineResult r, Pose3d seedPose) {
    if (camera.cameraMatrix == null) {
      camera.camera.getCameraMatrix().ifPresent(m -> camera.cameraMatrix = m);
    }
    if (camera.distCoeffs == null) {
      camera.camera.getDistCoeffs().ifPresent(d -> camera.distCoeffs = d);
    }
    if (camera.cameraMatrix == null || camera.distCoeffs == null) {
      return Optional.empty(); // camera is not calibrated in the PhotonVision UI
    }
    return camera.photonEstimator.estimateConstrainedSolvepnpPose(
        r,
        camera.cameraMatrix,
        camera.distCoeffs,
        seedPose,
        false,
        VisionConstants.headingScaleFactor);
  }

  private String process(Camera camera, PhotonPipelineResult r) {
    if (!r.hasTargets()) {
      camera.peakFrameStreak = 0;
      return "no-targets";
    }
    final double ts = r.getTimestampSeconds();
    if (ts < ignoreResultsBefore) {
      return "pre-reset";
    }

    // The gyro-driven heading the drivetrain had when this frame was captured.
    Optional<Pose2d> history = poseAtFpgaTime.apply(ts);
    if (history.isEmpty()) {
      return "no-history";
    }
    final Pose2d poseAtCapture = history.get();
    final Rotation2d gyroHeading = poseAtCapture.getRotation();
    camera.photonEstimator.addHeadingData(
        ts, gyroHeading); // required once per result for constrained PnP

    // Non-PnP solve: coprocessor multi-tag, or lowest ambiguity as fallback.
    Optional<EstimatedRobotPose> multi = camera.photonEstimator.estimateCoprocMultiTagPose(r);
    Optional<EstimatedRobotPose> seed =
        multi.isPresent() ? multi : camera.photonEstimator.estimateLowestAmbiguityPose(r);
    if (seed.isEmpty()) {
      camera.peakFrameStreak = 0;
      return "no-seed";
    }

    // ---- the one stddev formula ----
    List<PhotonTrackedTarget> targets = r.getTargets();
    final int tagCount = targets.size();
    double distSum = 0.0;
    for (PhotonTrackedTarget t : targets) {
      distSum += t.getBestCameraToTarget().getTranslation().getNorm();
    }
    final double avgDist = distSum / tagCount;
    if (avgDist > VisionConstants.maxAvgTagDist) {
      camera.peakFrameStreak = 0;
      return "far";
    }

    // Now as seen on tv! packaged with camera!
    // final int camIndex = indexOf(camera);

    final double linearStdDev =
        VisionConstants.linearStdDevBaseline * avgDist * avgDist / tagCount * camera.stdDevFactor;

    // ---- peak candidate? needs N consecutive candidate frames on this camera ----
    boolean candidate =
        isPeakCandidate(seed.get(), multi.isPresent(), targets, linearStdDev, gyroHeading);
    camera.peakFrameStreak = candidate ? camera.peakFrameStreak + 1 : 0;
    final boolean peak = candidate && camera.peakFrameStreak >= VisionConstants.peakFramesRequired;

    // ---- choose the pose ----
    final Pose3d chosen;
    final double angularStdDev;
    if (peak) {
      chosen = seed.get().estimatedPose; // no PnP: this pose's own heading is the correction
      angularStdDev = VisionConstants.peakAngularStdDev;
    } else {
      Optional<EstimatedRobotPose> constrained =
          solveConstrained(camera, r, seed.get().estimatedPose);
      if (constrained.isPresent()) {
        chosen = constrained.get().estimatedPose;
      } else if (multi.isPresent()) {
        chosen = multi.get().estimatedPose; // calibration missing or solver failed: xy only
      } else {
        return "pnp-failed";
      }
      angularStdDev = VisionConstants.ignoreAngle;
    }

    // ---- sanity gates (all compared against the pose at capture time) ----
    final Pose2d pose2d = chosen.toPose2d();
    if (!VisionUtils.inField(pose2d)) {
      return "out-of-field";
    }
    if (!DriverStation.isDisabled()
        && pose2d.getTranslation().getDistance(poseAtCapture.getTranslation())
            > VisionConstants.maxJumpWhileEnabled) {
      return "jump";
    }
    if (!peak
        && Math.abs(VisionUtils.headingError(pose2d, gyroHeading))
            > VisionConstants.maxHeadingDisagreement) {
      return "heading-disagree";
    }

    poseOutput.accept(pose2d, ts, VecBuilder.fill(linearStdDev, linearStdDev, angularStdDev));
    SmartDashboard.putNumberArray(
        "Vision/" + camera.camName + "/pose",
        new double[] {pose2d.getX(), pose2d.getY(), pose2d.getRotation().getRadians()});

    if (peak) {
      peakThisLoop = true;
      lastPeakFpgaTime = Timer.getFPGATimestamp();
      peakCount++;
      return "PEAK!! :)";
    }
    return "ok";
  }

  @Override
  public void periodic() {
    peakThisLoop = false;

    if (headingResetPending) {
      seedHeadingBuffers();
    }

    boolean spinning = Math.abs(yawRateRadPerSec.getAsDouble()) > VisionConstants.maxYawRate;

    for (Camera camera : cameras) {
      // Must be called exactly once per loop per camera (it drains the queue).
      List<PhotonPipelineResult> results = camera.camera.getAllUnreadResults();
      for (PhotonPipelineResult r : results) {
        camera.status = spinning ? "YOU SPIN ME RIGHT ROUND" : process(camera, r);
      }
      SmartDashboard.putString("Vision/" + camera.camName + "/status", camera.status);
    }

    SmartDashboard.putBoolean("Vision/peakThisLoop", peakThisLoop);
    SmartDashboard.putNumber("Vision/peakCount", peakCount);

    for (Camera camera : cameras) {
      camera.update();
    }

    if ((!Robot.isReal()) && VisionConstants.simulateCoproc) {
      visionSim.update(simDTGetter.get());
    }
  }

  /** Reset pose history of the robot in the vision system simulation. */
  @SuppressWarnings("unused")
  public void resetSimPose(Pose2d pose) {
    if (Robot.isSimulation() && VisionConstants.simulateCoproc) visionSim.resetRobotPose(pose);
  }

  /** A Field2d for visualizing our robot and objects on the field. */
  public Field2d getSimDebugField() {
    if (!Robot.isSimulation()) return null;
    if (!VisionConstants.simulateCoproc) return null;
    return visionSim.getDebugField();
  }

  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    parentTable.addStructLogger(
        "drivetrainRotation",
        logMode,
        () -> obtainDrivetrainRotation(drivetrain),
        Rotation3d.struct);
    // for (Camera camera : cameras) {
    //   parentTable.addLoggable(camera, logMode);
    // }

    // for (Camera camera : cameras) {
    //   parentTable.addStructLogger(
    //       "pose_cam" + camera.camName,
    //       logMode,
    //       () -> new Pose3d(drivetrain.getState().Pose).plus(camera.robotToCam),
    //       // () ->
    //       //     new Pose3d(
    //       //         camera.robotToCam.getX(),
    //       //         camera.robotToCam.getY(),
    //       //         camera.robotToCam.getZ(),
    //       //         camera.robotToCam.getRotation()),
    //       Pose3d.struct);
    // }

    // parentTable.addDoubleLogger(
    //     "rot0Degrees", logMode, () -> drivetrain.getState().Pose.getRotation().getDegrees());
    // // parentTable.addDoubleLogger(
    // //     "rot1Degrees", logMode, () -> drivetrain.getState().Pose.getRotation().getDegrees());
  }
}
