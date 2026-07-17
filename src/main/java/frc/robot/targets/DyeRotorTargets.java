package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.units.measure.AngularVelocity;
import java.util.Optional;

public interface DyeRotorTargets {
  public enum HookTargets {
    On(6000),
    Off(0);

    public AngularVelocity speed;

    private HookTargets(AngularVelocity speed) {
      this.speed = speed;
    }

    private HookTargets(double speed) {
      this.speed = RPM.of(speed);
    }
  }

  public enum KickerMotorTarget {
    On(4000),
    Off;

    public final Optional<AngularVelocity> speed;

    private KickerMotorTarget(double speed) {
      this.speed = Optional.of(RPM.of(speed));
    }

    private KickerMotorTarget(
        AngularVelocity lowSpeed, AngularVelocity midSpeed, AngularVelocity highSpeed) {
      this.speed = Optional.of(lowSpeed);
    }

    private KickerMotorTarget() {
      this.speed = Optional.empty();
    }
  }
}
