package frc.robot.constants;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.Robot;
import frc.robot.vision.Camera;
import org.photonvision.simulation.SimCameraProperties;

public class VisionAndPoseEstConstants {
  public static final double maxReasonableVisionDistance = 4;

  public static final boolean simulateCoproc = true;

  public static final AprilTagFieldLayout kTagLayout = FieldConstants.fieldAprilTags;

  // TODO: May need to flip YAW signs

  public static final Camera CAM_FRONT_RIGHT =
      new Camera(
          kTagLayout,
          new Transform3d(
              new Translation3d(
                  Inches.of(-7.822376).in(Meters),
                  Inches.of(-10.446815).in(Meters),
                  Inches.of(28.002807).in(Meters)),
              new Rotation3d(0, Math.toRadians(-15), 0)
                  .rotateBy(new Rotation3d(0, 0, Math.toRadians(49)))),
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
          2);

  public static final Camera CAM_BACK_LEFT =
      new Camera(
          kTagLayout,
          new Transform3d(
              new Translation3d(
                  Inches.of(-7.128678).in(Meters),
                  Inches.of(10.086332).in(Meters),
                  Inches.of(28.077753).in(Meters)),
              // new Rotation3d(0, Math.toRadians(-25), Math.toRadians(0))
              //     .rotateBy(new Rotation3d(0, 0, -20))
              new Rotation3d(0, Math.toRadians(-15), Math.toRadians(0))
                  .rotateBy(new Rotation3d(0, 0, Math.toRadians(225)))),
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
          2);
}
