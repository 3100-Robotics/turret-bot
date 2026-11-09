package frc.blastoise.subsystems.intakerollers;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.blastoise.constants.IntakeConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeRollersIOTalonFX implements IntakeRollersIO {
  private final TalonFX rightRawMotor = new TalonFX(IntakeConstants.topRollerMotorRightCanID2);
  private final TalonFX leftRawMotor = new TalonFX(IntakeConstants.topRollerMotorLeftCanID1);

  private final SmartMotorController motor;

  public IntakeRollersIOTalonFX(SubsystemBase subsystem) {
    this.motor =
        new TalonFXWrapper(
            rightRawMotor,
            IntakeConstants.topRollerMotorPhysical,
            IntakeConstants.rightExtensionMotorConfig
                .clone()
                .withFollowers(Pair.of(leftRawMotor, true))
                .withSubsystem(subsystem));
    // this.leftMotor =
    //     new TalonFXWrapper(
    //         leftRawMotor,
    //         IntakeConstants.extensionMotorPhysical,
    //         IntakeConstants.leftExtensionMotorConfig.withSubsystem(subsystem));

    // Step 4: Create Elevator mechanism, simulation is handled automatically
    // this.rightLinearMechansim =
    //     new Elevator(IntakeConstants.rightExtensionMechanismConfig, rightMotor);
    // this.leftLinearMechansim =
    //     new Elevator(IntakeConstants.leftExtensionMechanismConfig, leftMotor);
  }

  @Override
  public void updateInputs(IntakeRollersIOInputs inputs) {
    // Pull telemetry data from the underlying SmartMotorController
    inputs.rightPositionMeters = motor.getMeasurementPosition().in(Meters);
    inputs.rightVelocityMetersPerSec = motor.getMeasurementVelocity().in(MetersPerSecond);
    inputs.rightAppliedVolts = motor.getVoltage().in(Volts);
    inputs.rightSupplyCurrentAmps = motor.getSupplyCurrent().map(c -> c.in(Amps)).orElse(0.0);
    inputs.rightStatorCurrentAmps = motor.getStatorCurrent().in(Amps);
    inputs.rightTemperatureCelsius = motor.getTemperature().in(Celsius);
    // The setpoint is stored as a mechanism angle; convert it to a height with the config
    inputs.rightTargetPositionMeters =
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
    // Use SmartMotorController's setPosition method with Distance
    // leftMotor.setVelocity(speed);
    motor.setVelocity(speed);
  }

  @Override
  public void stop() {
    // leftMotor.setDutyCycle(0);
    motor.setDutyCycle(0);
  }

  @Override
  public void simIterate() {
    motor.simIterate();
    // leftMotor.simIterate();
  }

  @Override
  public void updateTelemetry() {
    motor.updateTelemetry();
    // leftMotor.updateTelemetry();
  }
}
