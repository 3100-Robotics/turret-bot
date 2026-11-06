package frc.blastoise.subsystems.intakeextension;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.blastoise.constants.IntakeConstants;
import yams.mechanisms.positional.Elevator;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeExtensionIOTalonFX implements IntakeExtensionIO {
  private final TalonFX rightRawMotor = new TalonFX(0);
  private final TalonFX leftRawMotor = new TalonFX(0);

  private final SmartMotorController rightMotor;
  private final SmartMotorController leftMotor;

  private final Elevator rightLinearMechansim;
  private final Elevator leftLinearMechansim;

  public IntakeExtensionIOTalonFX(SubsystemBase subsystem) {
    // Step 2: Create SmartMotorController (TalonFXWrapper)
    this.rightMotor =
        new TalonFXWrapper(
            rightRawMotor,
            IntakeConstants.extensionMotorPhysical,
            IntakeConstants.extensionMotorConfig.clone().withSubsystem(subsystem));
    this.leftMotor =
        new TalonFXWrapper(
            leftRawMotor,
            IntakeConstants.extensionMotorPhysical,
            IntakeConstants.extensionMotorConfig.clone().withSubsystem(subsystem));

    // Step 4: Create Elevator mechanism, simulation is handled automatically
    this.rightLinearMechansim = new Elevator(IntakeConstants.extensionMechanismConfig, rightMotor);
    this.leftLinearMechansim = new Elevator(IntakeConstants.extensionMechanismConfig, leftMotor);
  }

  @Override
  public void updateInputs(IntakeExtensionIOInputs inputs) {
    // Pull telemetry data from the underlying SmartMotorController
    inputs.rightPositionMeters = rightMotor.getMeasurementPosition().in(Meters);
    inputs.rightVelocityMetersPerSec = rightMotor.getMeasurementVelocity().in(MetersPerSecond);
    inputs.rightAppliedVolts = rightMotor.getVoltage().in(Volts);
    inputs.rightSupplyCurrentAmps = rightMotor.getSupplyCurrent().map(c -> c.in(Amps)).orElse(0.0);
    inputs.rightStatorCurrentAmps = rightMotor.getStatorCurrent().in(Amps);
    inputs.rightTemperatureCelsius = rightMotor.getTemperature().in(Celsius);
    // The setpoint is stored as a mechanism angle; convert it to a height with the config
    inputs.rightTargetPositionMeters =
        rightMotor
            .getMechanismPositionSetpoint()
            .map(a -> rightMotor.getConfig().convertFromMechanism(a).in(Meters))
            .orElse(0.0);

    // Pull telemetry data from the underlying SmartMotorController
    inputs.leftPositionMeters = leftMotor.getMeasurementPosition().in(Meters);
    inputs.leftVelocityMetersPerSec = leftMotor.getMeasurementVelocity().in(MetersPerSecond);
    inputs.leftAppliedVolts = leftMotor.getVoltage().in(Volts);
    inputs.leftSupplyCurrentAmps = leftMotor.getSupplyCurrent().map(c -> c.in(Amps)).orElse(0.0);
    inputs.leftStatorCurrentAmps = leftMotor.getStatorCurrent().in(Amps);
    inputs.leftTemperatureCelsius = leftMotor.getTemperature().in(Celsius);
    // The setpoint is stored as a mechanism angle; convert it to a height with the config
    inputs.leftTargetPositionMeters =
        leftMotor
            .getMechanismPositionSetpoint()
            .map(a -> leftMotor.getConfig().convertFromMechanism(a).in(Meters))
            .orElse(0.0);
  }

  @Override
  public void setTargetHeight(Distance height) {
    // Use SmartMotorController's setPosition method with Distance
    leftMotor.setPosition(height);
    rightMotor.setPosition(height);
  }

  @Override
  public void stop() {
    leftMotor.setDutyCycle(0);
    rightMotor.setDutyCycle(0);
  }

  /**
   * Access the Elevator mechanism for command helpers like run() and runTo(). Returns first right
   * and second left
   */
  public Pair<Elevator, Elevator> getLinearMechanism() {
    return Pair.of(rightLinearMechansim, leftLinearMechansim);
  }

  @Override
  public void simIterate() {
    rightLinearMechansim.simIterate();
    leftLinearMechansim.simIterate();
  }

  @Override
  public void updateTelemetry() {
    rightLinearMechansim.updateTelemetry();
    leftLinearMechansim.updateTelemetry();
  }
}
