package frc.robot.targets;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import java.util.Optional;

public interface IntakeTargets {
  public enum IntakeRollerTarget {
    On(4000, 2000),
    Off;

    public final Optional<AngularVelocity> topSpeed;
    public final Optional<AngularVelocity> bottomSpeed;

    private IntakeRollerTarget(double topSpeed, double bottomSpeed) {
      this.topSpeed = Optional.of(RPM.of(topSpeed));
      this.bottomSpeed = Optional.of(RPM.of(bottomSpeed));
    }

    private IntakeRollerTarget(AngularVelocity topSpeed, AngularVelocity bottomSpeed) {
      this.topSpeed = Optional.of(topSpeed);
      this.bottomSpeed = Optional.of(bottomSpeed);
    }

    private IntakeRollerTarget() {
      this.topSpeed = Optional.empty();
      this.bottomSpeed = Optional.empty();
    }
  }

  public enum IntakeExtensionTarget {
    Full(1),
    Half(0.5),
    Stowed(0);

    public final Distance extensionDistance;

    private IntakeExtensionTarget(double distance) {
      this.extensionDistance = Meters.of(distance);
    }

    private IntakeExtensionTarget(Distance distance) {
      this.extensionDistance = distance;
    }
  }
}
