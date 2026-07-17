package frc.robot.targets;

import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.units.measure.AngularVelocity;

public interface DyeRotorTargets {
    public enum HookTargets {
        On(6000), Off(0);

        public AngularVelocity speed;

        private HookTargets(AngularVelocity speed) {
            this.speed = speed;
        }

        private HookTargets(double speed) {
            this.speed = RPM.of(speed);
        }
    }
}
