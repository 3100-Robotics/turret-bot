package frc.robot.utils;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.VisionConstants;
import java.util.List;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionUtils {
  /** Smallest non-negative ambiguity among targets, or -1 if none was computed. */
  public static double lowestAmbiguity(List<PhotonTrackedTarget> targets) {
    double best = -1.0;
    for (PhotonTrackedTarget t : targets) {
      double a = t.getPoseAmbiguity();
      if (a >= 0 && (best < 0 || a < best)) {
        best = a;
      }
    }
    return best;
  }

  public static double headingError(Pose2d visionPose, Rotation2d gyroHeading) {
    return MathUtil.angleModulus(visionPose.getRotation().minus(gyroHeading).getRadians());
  }

  public static boolean inField(Pose2d p) {
    double m = VisionConstants.fieldMargin;
    return p.getX() >= -m
        && p.getX() <= VisionConstants.kTagLayout.getFieldLength() + m
        && p.getY() >= -m
        && p.getY() <= VisionConstants.kTagLayout.getFieldWidth() + m;
  }
}
