package frc.blastoise.subsystems.turret;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.blastoise.constants.ShooterConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class TurretIOTalonFX implements TurretIO {
  private final TalonFX rawMotor = new TalonFX(ShooterConstants.hoodMotorCanID);

  private final SmartMotorController motor;

  public TurretIOTalonFX(SubsystemBase subsystem) {
    this.motor =
        new TalonFXWrapper(
            rawMotor,
            ShooterConstants.hoodMotorPhysical,
            ShooterConstants.hoodMotorConfig.withSubsystem(subsystem));
  }

  @Override
  public void updateInputs(TurretIOInputs inputs) {
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
  public void setAngle(Angle angle) {
    motor.setPosition(angle);
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
