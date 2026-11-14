package frc.blastoise.subsystems.intakerollers;

import edu.wpi.first.units.measure.AngularVelocity;
import org.littletonrobotics.junction.AutoLog;

public interface IntakeRollersIO {
  public static class IntakeRollersIOReplay implements IntakeRollersIO {}

  @AutoLog
  public static class IntakeRollersIOInputs {
    public double rightPositionMeters = 0.0;
    public double rightVelocityMetersPerSec = 0.0;
    public double rightAppliedVolts = 0.0;
    public double rightSupplyCurrentAmps = 0.0;
    public double rightStatorCurrentAmps = 0.0;
    public double rightTemperatureCelsius = 0.0;
    public double rightTargetPositionMeters = 0.0;

    // public double leftPositionMeters = 0.0;
    // public double leftVelocityMetersPerSec = 0.0;
    // public double leftAppliedVolts = 0.0;
    // public double leftSupplyCurrentAmps = 0.0;
    // public double leftStatorCurrentAmps = 0.0;
    // public double leftTemperatureCelsius = 0.0;
    // public double leftTargetPositionMeters = 0.0;
  }

  default void updateInputs(IntakeRollersIOInputs inputs) {}

  default void setSpeed(AngularVelocity speed) {}

  default void stop() {}

  /** Update the sim telemetry */
  default void simIterate() {}

  /** Update the telemetry */
  default void updateTelemetry() {}
}
