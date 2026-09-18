package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.RPM;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.constants.ShooterConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class Flywheel extends LightSubsystem implements Loggable {
  // Define vendor motors
  private final TalonFX rawMotorLeftFlywheel = new TalonFX(ShooterConstants.leftFlywheelMotorCanID);
  private final TalonFX rawMotorRightFlywheel =
      new TalonFX(ShooterConstants.rightFlywheelMotorCanID);

  // Define SmartMotorControllers
  private final SmartMotorController motor =
      new TalonFXWrapper(
          rawMotorLeftFlywheel,
          ShooterConstants.flywheelMotorPhysical,
          ShooterConstants.flywheelMotorConfig
              .withFollowers(Pair.of(rawMotorRightFlywheel, true))
              .withSubsystem(this));

  public void setSpeed(AngularVelocity speed) {
    if (speed.in(RPM) == 0) {
      stopFlywheel();
    } else {
      motor.setVelocity(speed);
    }
  }

  public void stopFlywheel() {
    motor.setDutyCycle(0);
  }

  @Override
  public void periodic() {
    motor.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    motor.simIterate();
  }

  @Override
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {
    parentTable.addDoubleLogger(
        "flywheelMechVelocity", logMode, () -> motor.getMechanismVelocity().in(RPM));
  }
}
