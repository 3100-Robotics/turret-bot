package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import java.util.Optional;

import edu.wpi.first.units.measure.AngularVelocity;

public interface IntakeTargets {
    public enum IntakeRollerMotorTargets {
        On(4000, 2000),
        Off;

        public final Optional<AngularVelocity> topSpeed;
        public final Optional<AngularVelocity> bottomSpeed;

        private IntakeRollerMotorTargets(double topSpeed, double bottomSpeed) {
        this.topSpeed = Optional.of(RPM.of(topSpeed));
        this.bottomSpeed = Optional.of(RPM.of(bottomSpeed));
        }

        private IntakeRollerMotorTargets(
            AngularVelocity lowSpeed, AngularVelocity midSpeed, AngularVelocity highSpeed) {
        this.topSpeed = Optional.of(lowSpeed);
        }

        private IntakeRollerMotorTargets() {
        this.topSpeed = Optional.empty();
        }
  }
}
