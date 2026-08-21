package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import java.util.Optional;

import edu.wpi.first.units.measure.AngularVelocity;

public interface IntakeTargets {
    public enum IntakeRollerMotorTarget {
        On(4000, 2000),
        Off(0, 0);

        public final Optional<AngularVelocity> topSpeed;
        public final Optional<AngularVelocity> bottomSpeed;

        private IntakeRollerMotorTarget(double topSpeed, double bottomSpeed) {
        this.topSpeed = Optional.of(RPM.of(topSpeed));
        this.bottomSpeed = Optional.of(RPM.of(bottomSpeed));
        }

        private IntakeRollerMotorTarget(
            AngularVelocity lowSpeed, AngularVelocity midSpeed, AngularVelocity highSpeed) {
        this.topSpeed = Optional.of(lowSpeed);
        this.bottomSpeed = Optional.of(lowSpeed);
        }

        private IntakeRollerMotorTarget() {
        this.topSpeed = Optional.empty();
        this.bottomSpeed = Optional.empty();
        }
  }
}
