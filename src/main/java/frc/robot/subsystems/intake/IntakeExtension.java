package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Meters;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import frc.robot.constants.IntakeConstants;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;
import frc.robot.utils.Utils;
import yams.mechanisms.positional.Elevator;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeExtension extends LightSubsystem implements Loggable {
  private final TalonFX extensionMotorLeft = new TalonFX(IntakeConstants.extensionMotorLeftCanID);
  private final TalonFX extensionMotorRight = new TalonFX(IntakeConstants.extensionMotorLeftCanID);

  private final SmartMotorController motor =
      new TalonFXWrapper(
          extensionMotorLeft,
          IntakeConstants.extensionMotorPhysical,
          IntakeConstants.extensionMotorConfig
              .withFollowers(Pair.of(extensionMotorRight, true))
              .withSubsystem(this));

  private final Elevator linearMechanism =
      new Elevator(IntakeConstants.extensionMechanismConfig, motor);

  public void setIntakeExtensionTarget(IntakeExtensionTarget target) {
    motor.setPosition(target.extensionDistance);
  }

  public void setPositionRaw(Distance distance) {
    Utils.warnIfDriverStationOutOfTest();

    motor.setPosition(distance);
  }

  public void stopIntakeExtension() {
    motor.setDutyCycle(0);
  }

  @Override
  public void periodic() {
    linearMechanism.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    linearMechanism.simIterate();
  }

  @Override
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    parentTable.addDoubleLogger(
        "measuredExtensionDistance", logMode, () -> motor.getMeasurementPosition().in(Meters));
  }
}
