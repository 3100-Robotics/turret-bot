package frc.robot.vision;

import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.numbers.N8;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.vision.MainVision.EstimateConsumer;
import java.util.function.Supplier;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;

public class Camera implements Loggable {
  // config
  public final AprilTagFieldLayout tagLayout;
  public final double distanceCutoffMeters;
  public final Transform3d robotToCam;
  public final String camName;
  public final PhotonCamera camera;
  public final PhotonPoseEstimator photonEstimator;

  public Matrix<N3, N3> cameraMatrix;
  public Matrix<N8, N1> distCoeffs;
  public int peakFrameStreak = 0;
  public String status = "init";
  public double stdDevFactor;

  private Field2d simField;
  public SimCameraProperties simCamProps;
  public PhotonCameraSim sim;

  // Log Values
  private String stdevMode = "";
  private String newStdDevMode = "";
  private String distMode = "";
  private String currentStrat = "";
  private double currentPoseAmbiguity = 0;
  public boolean multiTagFound = false;
  public double latency = 0;
  private double avgDist = 0;
  private Pose3d rawLatestPose = Pose3d.kZero;

  private EstimateConsumer estimateConsumer =
      (Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs) -> {};

  private Supplier<Pair<Rotation3d, Double>> headingSupplier = () -> Pair.of(Rotation3d.kZero, 0.0);

  public Camera(
      AprilTagFieldLayout atagfieldlayout,
      Transform3d robotCameraTransform,
      String camName,
      boolean doSim,
      SimCameraProperties simCamProps,
      boolean simDrawWireframe,
      boolean processedStream,
      boolean rawStream,
      double distanceCutoffMeters,
      double stdDevFactor) {

    this.stdDevFactor = stdDevFactor;
    this.distanceCutoffMeters = distanceCutoffMeters;

    tagLayout = atagfieldlayout;
    this.robotToCam = robotCameraTransform;
    this.camName = camName;
    camera = new PhotonCamera(this.camName);
    photonEstimator = new PhotonPoseEstimator(this.tagLayout, this.robotToCam);

    if (doSim) {
      this.simCamProps = simCamProps;
      this.sim = new PhotonCameraSim(this.camera, this.simCamProps);
      this.sim.enableDrawWireframe(simDrawWireframe);
      this.sim.enableProcessedStream(processedStream);
      this.sim.enableRawStream(rawStream);
    }
  }

  public void setSimField(Field2d simField) {
    this.simField = simField;
  }

  public void setHeadingSupplier(Supplier<Pair<Rotation3d, Double>> headingSupplier) {
    this.headingSupplier = headingSupplier;
  }

  public void setPoseOutput(EstimateConsumer estimateConsumer) {
    this.estimateConsumer = estimateConsumer;
  }

  public void update() {
    // var heading = headingSupplier.get();
    // // photonEstimator.addHeadingData(heading.getSecond(), heading.getFirst());

    // Optional<EstimatedRobotPose> visionEst = Optional.empty();
    // for (var result : camera.getAllUnreadResults()) {
    //   if (result.getTargets().size() == 1) {
    //     if (result.getTargets().get(0).poseAmbiguity > 0.2) {
    //       return;
    //     }
    //   }

    //   // visionEst = photonEstimator.estimatePnpDistanceTrigSolvePose(result);
    //   visionEst = photonEstimator.estimateCoprocMultiTagPose(result);
    //   currentStrat = "CoprocMultiTag";
    //   if (visionEst.isEmpty()) {
    //     currentStrat = "LowestAmbiguityPose";
    //     visionEst = photonEstimator.estimateLowestAmbiguityPose(result);
    //   }

    //   result
    //       .getMultiTagResult()
    //       .ifPresentOrElse(
    //           e -> {
    //             multiTagFound = true;
    //           },
    //           () -> multiTagFound = false);

    //   updateEstimationStdDevs(visionEst, result.getTargets());

    //   double total = 0;
    //   for (PhotonTrackedTarget t : result.getTargets()) {
    //     total += t.poseAmbiguity;
    //   }

    //   currentPoseAmbiguity =
    //       result.getTargets().size() > 0 ? total / result.getTargets().size() : 0;

    //   latency = result.metadata.getLatencyMillis();

    //   if (Robot.isSimulation() && VisionConstants.simulateCoproc) {
    //     visionEst.ifPresentOrElse(
    //         est ->
    //             simField
    //                 .getObject("VisionEstimation" + camName)
    //                 .setPose(est.estimatedPose.toPose2d()),
    //         () -> {
    //           simField.getObject("VisionEstimation" + camName).setPoses();
    //         });
    //   }

    //   visionEst.ifPresent(
    //       est -> {
    //         // Change our trust in the measurement based on the tags we can see
    //         var estStdDevs = getEstimationStdDevs();
    //         // if (est.estimatedPose.)
    //         // return;

    //         rawLatestPose = est.estimatedPose;
    //         estimateConsumer.accept(est.estimatedPose.toPose2d(), est.timestampSeconds,
    // estStdDevs);
    //         // SmartDashboard.put("poseRaw_" + camName, est.estimatedPose.toPose2d());
    //       });
    // }
  }

  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    // parentTable
    //     .addStructLogger(
    //         camName + "rawLatestPose", logMode, () -> rawLatestPose.toPose2d(), Pose2d.struct)
    //     .addStructLogger(camName + "camPose", logMode, () -> robotToCam, Transform3d.struct)
    //     .addDoubleLogger(camName + "latency", logMode, () -> latency)
    //     .addDoubleLogger(camName + "currentAmbig", logMode, () -> currentPoseAmbiguity)
    //     .addDoubleLogger(camName + "stdX", logMode, () -> curStdDevs.get(0, 0))
    //     .addDoubleLogger(camName + "stdY", logMode, () -> curStdDevs.get(1, 0))
    //     .addDoubleLogger(camName + "stdT", logMode, () -> curStdDevs.get(2, 0))
    //     .addStringLogger(camName + "stdDevMode", logMode, () -> stdevMode)
    //     .addStringLogger(camName + "newStdDevMode", logMode, () -> newStdDevMode)
    //     .addStringLogger(camName + "currentStrat", logMode, () -> currentStrat)
    //     .addBooleanLogger(camName + "multiTagFound", logMode, () -> multiTagFound);
  }
}
