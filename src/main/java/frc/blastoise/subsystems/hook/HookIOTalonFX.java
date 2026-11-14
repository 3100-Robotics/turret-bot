package frc.blastoise.subsystems.hook;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.blastoise.constants.DyeRotorConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class HookIOTalonFX implements HookIO {
  private final TalonFX rawMotor = new TalonFX(DyeRotorConstants.hookMotorCanID);

  private final SmartMotorController motor;

  public HookIOTalonFX(SubsystemBase subsystem) {
    this.motor =
        new TalonFXWrapper(
            rawMotor,
            DyeRotorConstants.hookMotorPhysical,
            DyeRotorConstants.hookMotorConfig.clone().withSubsystem(subsystem));
  }

  @Override
  public void updateInputs(HookIOInputs inputs) {
    // Pull telemetry data from the underlying SmartMotorController
    inputs.positionMeters = motor.getMeasurementPosition().in(Meters);
    inputs.velocityMetersPerSec = motor.getMeasurementVelocity().in(MetersPerSecond);
    inputs.appliedVolts = motor.getVoltage().in(Volts);
    inputs.supplyCurrentAmps = motor.getSupplyCurrent().map(c -> c.in(Amps)).orElse(0.0);
    inputs.statorCurrentAmps = motor.getStatorCurrent().in(Amps);
    inputs.temperatureCelsius = motor.getTemperature().in(Celsius);
    // The setpoint is stored as a mechanism angle; convert it to a height with the config
    inputs.targetPositionMeters =
        motor
            .getMechanismPositionSetpoint()
            .map(a -> motor.getConfig().convertFromMechanism(a).in(Meters))
            .orElse(0.0);
  }

  @Override
  public void setSpeed(AngularVelocity speed) {
    motor.setVelocity(speed);
  }

  @Override
  public void stop() {
    motor.setDutyCycle(0);
  }

  @Override
  public void simIterate() {
    motor.simIterate();
  }

  @Override
  public void updateTelemetry() {
    motor.updateTelemetry();
  }
}
