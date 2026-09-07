package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;

import edu.wpi.first.units.measure.Angle;
import frc.robot.constants.ShooterConstants;
import yams.mechanisms.positional.Pivot;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class Turret extends LightSubsystem implements Loggable {

  // Define vendor motors
  private final TalonFX rawMotor = new TalonFX(ShooterConstants.turretMotorCanID);

  // Define SmartMotorControllers
  private final SmartMotorController motor =
      new TalonFXWrapper(
          rawMotor,
          ShooterConstants.turretMotorPhysical,
          ShooterConstants.turretMotorConfig.withSubsystem(this));

  private final Pivot pivotMech = new Pivot(ShooterConstants.turretConfig, motor);

  public void setTurretAngle(Angle angle) {
    motor.setPosition(angle);
  }

  public void stopTurret() {
    motor.setDutyCycle(0);
  }

  @Override
  public void periodic() {
    // motor.updateTelemetry();
    pivotMech.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    // motor.simIterate();
    pivotMech.simIterate();
  }

  @Override
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    parentTable.addDoubleLogger(
        "turretMechAngle", logMode, () -> motor.getMechanismPosition().in(Degrees));
  }
}
