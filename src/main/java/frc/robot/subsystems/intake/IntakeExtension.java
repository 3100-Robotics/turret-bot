package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Meters;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.units.measure.Distance;
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
              .withLooselyCoupledFollowers(rightMotor) // Pair.of(extensionMotorRight, true))
              .withSubsystem(this));

  private final Elevator linearMechanism =
      new Elevator(IntakeConstants.extensionMechanismConfig, leftMotor);

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
    parentTable.addDoubleLogger(
        "measuredExtensionDistance", logMode, () -> leftMotor.getMeasurementPosition().in(Meters));
  }
}
