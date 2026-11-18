package frc.robot.constants;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Robot;
import frc.robot.vision.Camera;
import org.photonvision.simulation.SimCameraProperties;

public class VisionConstants {
  public static final double maxReasonableVisionDistance = 6;

  public static final boolean simulateCoproc = true;

  public static final AprilTagFieldLayout kTagLayout = FieldConstants.fieldAprilTags;

  public static final Camera CAM_FRONT_RIGHT =
      new Camera(
          kTagLayout,
          new Transform3d(
              new Translation3d(
                  Inches.of(6.618558).in(Meters),
                  Inches.of(-14.110284).in(Meters),
                  Inches.of(6.977005).in(Meters)),
              new Rotation3d(0, Math.toRadians(-15), 0)
                  .rotateBy(new Rotation3d(0, 0, Math.toRadians(-49)))),
          "frontright",
          (!Robot.isReal()) && simulateCoproc,
          new SimCameraProperties()
              .setCalibration(960, 720, Rotation2d.fromDegrees(70))
              .setCalibError(0.35, 0.10)
              .setFPS(30)
              .setAvgLatencyMs(50)
              .setLatencyStdDevMs(15),
          true,
          true,
          true,
          2,
          1.0);

  public static final Camera CAM_BACK_LEFT =
      new Camera(
          kTagLayout,
          new Transform3d(
              new Translation3d(
                  Inches.of(-7.128678).in(Meters),
                  Inches.of(10.086332).in(Meters),
                  Inches.of(6.987764).in(Meters)),
              // new Rotation3d(0, Math.toRadians(-25), Math.toRadians(0))
              //     .rotateBy(new Rotation3d(0, 0, -20))
              new Rotation3d(0, Math.toRadians(-15), Math.toRadians(0))
                  .rotateBy(new Rotation3d(0, 0, Math.toRadians(-225)))),
          "backleft",
          (!Robot.isReal()) && simulateCoproc,
          new SimCameraProperties()
              .setCalibration(960, 720, Rotation2d.fromDegrees(70))
              .setCalibError(0.35, 0.10)
              .setFPS(30)
              .setAvgLatencyMs(50)
              .setLatencyStdDevMs(15),
          true,
          true,
          true,
          2,
          1.0);

  // ---------------- Core standard deviation: ONE formula ----------------
  // xyStdDev = baseline * avgTagDistance^2 / tagCount * cameraFactor
  // The distance^2 / tagCount shape is the widely used one (see sources in the chat answer).
  // The 0.02 baseline is TUNE THIS B. Replace it with your measured k from the static noise test.
  public static final double linearStdDevBaseline = 0.02; // m

  // Packaged with the camera objet
  //   public static final double[] cameraStdDevFactors = {1.0, 1.0}; // raise for a worse camera

  // Passed as the angular stddev for normal frames so the estimator never uses vision heading.
  public static final double ignoreAngle = 1e9;

  // ---------------- Frame rejection ----------------
  public static final double maxAvgTagDist = 5.0; // m, TUNE THIS B
  public static final double fieldMargin = 0.5; // m outside the field is rejected
  public static final double maxJumpWhileEnabled = 1.0; // m from the pose we had at capture time
  public static final double maxYawRate = 4.0; // rad/s, drop all frames while spinning faster

  // ---------------- Constrained solvePNP ----------------
  // Weight of "stay near the gyro heading" against pixel reprojection error.
  // TUNE THIS B PLACEHOLDER. I found no recommended value. Sweep 10, 100, 1000, 10000 (see tuning).
  public static final double headingScaleFactor = 100.0;
  // If the solved heading still disagrees with the gyro by more than this, the frame is rejected.
  public static final double maxHeadingDisagreement = Units.degreesToRadians(5.0); // TUNE THIS B

  // ---------------- Peak pose (the only path that corrects the gyro) ----------------
  // A frame is a peak CANDIDATE only if its xy stddev (same formula as above) is this small.
  // With baseline 0.02: 2 tags within 1.4 m, or 3 tags within 1.7 m. TUNE THIS B.
  public static final double peakMaxLinearStdDev = 0.02; // m
  public static final int peakFramesRequired = 5; // consecutive candidate frames on one camera
  public static final double peakMaxYawRate = 0.3; // rad/s
  // While ENABLED, a peak pose whose heading differs from the gyro by more than this is not used
  // for correction (treated as a gyro fault or bad frame). While disabled there is no limit.
  public static final double peakMaxCorrectionWhileEnabled = Units.degreesToRadians(15.0);
  // Angular stddev sent with a peak pose. Smaller means a stronger gyro correction per frame.
  public static final double peakAngularStdDev = 0.1; // rad, TUNE THIS B
  // Single-tag lowest-ambiguity poses can be peak candidates only if this is true.
  // Default false: a single tag's heading is the least trustworthy thing to correct a gyro with.
  public static final boolean allowSingleTagPeak = false;
  public static final double peakMaxAmbiguity = 0.05; // only used when allowSingleTagPeak
}
