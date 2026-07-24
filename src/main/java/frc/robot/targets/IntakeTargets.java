package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import java.util.Optional;

import edu.wpi.first.units.measure.AngularVelocity;

public interface IntakeTargets {
    public enum IntakeRollerMotorTargets {
        On(4000),
        Off;

        public final Optional<AngularVelocity> speed;

        private IntakeRollerMotorTargets(double speed) {
        this.speed = Optional.of(RPM.of(speed));
        }

        private IntakeRollerMotorTargets(
            AngularVelocity lowSpeed, AngularVelocity midSpeed, AngularVelocity highSpeed) {
        this.speed = Optional.of(lowSpeed);
        }

        private IntakeRollerMotorTargets() {
        this.speed = Optional.empty();
        }
  }
}
