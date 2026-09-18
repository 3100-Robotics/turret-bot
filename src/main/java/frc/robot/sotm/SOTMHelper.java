package frc.robot.sotm;

import static edu.wpi.first.units.Units.Degrees;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.MatchContext;
import frc.robot.constants.ShooterConstants;
import frc.robot.constants.ShotTable;

/** Based on blog.eeshwark.com/blog/shooting-on-the-fly-pt2 */
public class SOTMHelper {
  public static double latencySeconds = 0.1;

  public record Solution(Rotation2d aimHeading, double hoodAngleDeg, double flywheelRpm) {}

  public static Solution solve(Pose2d robotPose, ChassisSpeeds robotRelativeSpeeds) {
    ChassisSpeeds fieldSpeeds =
        ChassisSpeeds.fromRobotRelativeSpeeds(robotRelativeSpeeds, robotPose.getRotation());
    Translation2d robotVel =
        new Translation2d(fieldSpeeds.vxMetersPerSecond, fieldSpeeds.vyMetersPerSecond);

    Translation2d futurePos = robotPose.getTranslation().plus(robotVel.times(latencySeconds));

    Translation2d toGoal = MatchContext.getInstance().getHubTranslation().minus(futurePos);
    double distance = toGoal.getNorm();

    double baselineHorizVel = ShotTable.horizontalVelocity(distance);
    Translation2d targetVel = toGoal.div(distance).times(baselineHorizVel);

    Translation2d shotVel = targetVel.minus(robotVel);

    double effectiveDistance = ShotTable.effectiveDistanceForVelocity(shotVel.getNorm());

    return new Solution(
        shotVel.getAngle(),
        MathUtil.clamp(
            ShotTable.ANGLE_MAP.get(effectiveDistance),
            ShooterConstants.minHoodAngle.in(Degrees),
            ShooterConstants.maxHoodAngle.in(Degrees)),
        ShotTable.SPEED_MAP.get(effectiveDistance));
  }
}
