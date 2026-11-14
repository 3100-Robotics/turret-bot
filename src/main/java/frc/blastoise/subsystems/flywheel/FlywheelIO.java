package frc.blastoise.subsystems.flywheel;

import edu.wpi.first.units.measure.AngularVelocity;
import org.littletonrobotics.junction.AutoLog;

public interface FlywheelIO {
  public static class FlywheelIOReplay implements FlywheelIO {}

  @AutoLog
  public static class FlywheelIOInputs {
    public double positionMeters = 0.0;
    public double velocityMetersPerSec = 0.0;
    public double appliedVolts = 0.0;
    public double supplyCurrentAmps = 0.0;
    public double statorCurrentAmps = 0.0;
    public double temperatureCelsius = 0.0;
    public double targetPositionMeters = 0.0;
  }

  default void updateInputs(FlywheelIOInputs inputs) {}

  default void setSpeed(AngularVelocity speed) {}

  default void stop() {}

  /** Update the sim telemetry */
  default void simIterate() {}

  /** Update the telemetry */
  default void updateTelemetry() {}
}
