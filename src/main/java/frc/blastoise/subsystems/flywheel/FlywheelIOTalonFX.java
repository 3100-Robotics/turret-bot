package frc.blastoise.subsystems.flywheel;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.blastoise.constants.ShooterConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class FlywheelIOTalonFX implements FlywheelIO {
  private final TalonFX rightRawMotor = new TalonFX(ShooterConstants.rightFlywheelMotorCanID);
  private final TalonFX leftRawMotor = new TalonFX(ShooterConstants.leftFlywheelMotorCanID);

  private final SmartMotorController motor;

  public FlywheelIOTalonFX(SubsystemBase subsystem) {
    this.motor =
        new TalonFXWrapper(
            rightRawMotor,
            ShooterConstants.flywheelMotorPhysical,
            ShooterConstants.flywheelMotorConfig
                .clone()
                .withFollowers(Pair.of(leftRawMotor, true))
                .withSubsystem(subsystem));
  }

  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
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

    // Pull telemetry data from the underlying SmartMotorController
    // inputs.leftPositionMeters = leftMotor.getMeasurementPosition().in(Meters);
    // inputs.leftVelocityMetersPerSec = leftMotor.getMeasurementVelocity().in(MetersPerSecond);
    // inputs.leftAppliedVolts = leftMotor.getVoltage().in(Volts);
    // inputs.leftSupplyCurrentAmps = leftMotor.getSupplyCurrent().map(c -> c.in(Amps)).orElse(0.0);
    // inputs.leftStatorCurrentAmps = leftMotor.getStatorCurrent().in(Amps);
    // inputs.leftTemperatureCelsius = leftMotor.getTemperature().in(Celsius);
    // // The setpoint is stored as a mechanism angle; convert it to a height with the config
    // inputs.leftTargetPositionMeters =
    //     leftMotor
    //         .getMechanismPositionSetpoint()
    //         .map(a -> leftMotor.getConfig().convertFromMechanism(a).in(Meters))
    //         .orElse(0.0);
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
