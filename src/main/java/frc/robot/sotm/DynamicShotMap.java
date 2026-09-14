package frc.robot.sotm;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

public class DynamicShotMap implements ShotMap {

  private final SOTMState sotmstate;

  public DynamicShotMap(SOTMState sotmstate) {
    this.sotmstate = sotmstate;
  }

  public String getTarget() {
    return "Dynamic SOTM";
  }

  public AngularVelocity getFlywheelSpeed() {
    return sotmstate.getFlywheelSpeed();
  }

  public Angle getHoodAngle() {
    return sotmstate.getHoodAngle();
  }
}
