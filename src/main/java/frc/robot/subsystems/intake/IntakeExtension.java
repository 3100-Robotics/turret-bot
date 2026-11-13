package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.IntakeConstants;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;
import frc.robot.utils.Utils;
import yams.mechanisms.positional.Elevator;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeExtension extends LightSubsystem implements Loggable {
  private final TalonFX extensionMotorLeft = new TalonFX(IntakeConstants.extensionMotorLeftCanID);
  private final TalonFX extensionMotorRight = new TalonFX(IntakeConstants.extensionMotorRightCanID);

  private final SmartMotorController rightMotor =
      new TalonFXWrapper(
          extensionMotorRight,
          IntakeConstants.extensionMotorPhysical,
          IntakeConstants.extensionMotorConfigRight.withSubsystem(this));

  private final SmartMotorController leftMotor =
      new TalonFXWrapper(
          extensionMotorLeft,
          IntakeConstants.extensionMotorPhysical,
          IntakeConstants.extensionMotorConfigLeft
              .withLooselyCoupledFollowers(rightMotor)
              .withSubsystem(this));

  private final Elevator linearMechanism =
      new Elevator(IntakeConstants.extensionMechanismConfig, leftMotor);

  public final Trigger isAtSetpointTrigger =
      new Trigger(() -> isLinearMotorAtSetpoint(leftMotor) && isLinearMotorAtSetpoint(rightMotor));

  public void setIntakeExtensionTarget(IntakeExtensionTarget target) {
    leftMotor.setPosition(target.extensionDistance);
  }

  public void setPositionRaw(Distance distance) {
    Utils.warnIfDriverStationOutOfTest();

    leftMotor.setPosition(distance);
  }

  public void stopIntakeExtension() {
    leftMotor.setDutyCycle(0);
  }

  public static Distance getLinearMotorSetpointOrZero(SmartMotorController motor) {
    Angle motorSetpointAngle = motor.getMechanismPositionSetpoint().orElseGet(Degrees::zero);
    Distance motorSetpoint = motor.getConfig().convertFromMechanism(motorSetpointAngle);
    return motorSetpoint;
  }

  public static boolean isLinearMotorAtSetpoint(SmartMotorController motor) {
    Distance motorPosition = motor.getMeasurementPosition();
    return motorPosition.isNear(getLinearMotorSetpointOrZero(motor), 0.05);
  }

  @Override
  public void periodic() {
    linearMechanism.updateTelemetry();
    rightMotor.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    linearMechanism.simIterate();
  }

  @Override
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    parentTable.addBooleanLogger("isAtSetpoint", logMode, isAtSetpointTrigger::getAsBoolean);
    parentTable.addDoubleLogger(
        "leftMotorSetpoint", logMode, () -> getLinearMotorSetpointOrZero(leftMotor).in(Meters));
    parentTable.addDoubleLogger(
        "rightMotorSetpoint", logMode, () -> getLinearMotorSetpointOrZero(rightMotor).in(Meters));
    parentTable.addDoubleLogger(
        "leftMeasuredExtensionDistance",
        logMode,
        () -> leftMotor.getMeasurementPosition().in(Meters));
    parentTable.addDoubleLogger(
        "rightMeasuredExtensionDistance",
        logMode,
        () -> rightMotor.getMeasurementPosition().in(Meters));
    parentTable.addDoubleLogger(
        "intakeExtensionCurrent", logMode, () -> leftMotor.getStatorCurrent().in(Amps));
  }
}
