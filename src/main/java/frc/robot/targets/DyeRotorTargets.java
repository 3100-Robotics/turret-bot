package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.units.measure.AngularVelocity;
import java.util.Optional;

public interface DyeRotorTargets {
  public enum HookTargets {
    On(6000),
    Off;

    public Optional<AngularVelocity> speed;

    private HookTargets(AngularVelocity speed) {
      this.speed = Optional.of(speed);
    }

    private HookTargets(double speed) {
      this.speed = Optional.of(RPM.of(speed));
    }

    private HookTargets() {
      this.speed = Optional.empty();
    }
  }

  public enum KickerTarget {
    On(4000),
    Off;

    public final Optional<AngularVelocity> speed;

    private KickerTarget(double speed) {
      this.speed = Optional.of(RPM.of(speed));
    }

    private KickerTarget(
        AngularVelocity lowSpeed, AngularVelocity midSpeed, AngularVelocity highSpeed) {
      this.speed = Optional.of(lowSpeed);
    }

    private KickerTarget() {
      this.speed = Optional.empty();
    }
  }
}
