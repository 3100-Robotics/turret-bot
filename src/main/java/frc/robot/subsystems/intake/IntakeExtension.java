package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Meters;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.core.LightSubsystem;
import com.sbdc.loggerhead.core.LoggerheadUtils;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.IntakeConstants;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;
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
      new Trigger(
          () ->
              LoggerheadUtils.isLinearMotorAtSetpoint(leftMotor, 0.05)
                  && LoggerheadUtils.isLinearMotorAtSetpoint(rightMotor, 0.05));

  public void setIntakeExtensionTarget(IntakeExtensionTarget target) {
    leftMotor.setPosition(target.extensionDistance);
  }

  public void setPositionRaw(Distance distance) {
    LoggerheadUtils.warnIfDriverStationOutOfTest();

    leftMotor.setPosition(distance);
  }

  public void stopIntakeExtension() {
    leftMotor.setDutyCycle(0);
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
        "leftMotorSetpoint",
        logMode,
        () -> LoggerheadUtils.getLinearMotorSetpointOrZero(leftMotor).in(Meters));
    parentTable.addDoubleLogger(
        "rightMotorSetpoint",
        logMode,
        () -> LoggerheadUtils.getLinearMotorSetpointOrZero(rightMotor).in(Meters));
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
