package frc.robot.subsystems.dyerotor;

import static edu.wpi.first.units.Units.RPM;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.Pair;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.KickerTarget;
import frc.robot.utils.Utils;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class Kicker extends LightSubsystem implements Loggable {
  private final SparkMax rawMotor =
      new SparkMax(DyeRotorConstants.kickerMotorCanID1, MotorType.kBrushless);

  private final SmartMotorController motor =
      new SparkWrapper(
          rawMotor,
          DyeRotorConstants.kickerMotorPhysical,
          DyeRotorConstants.kickerMotorConfig
              .withSubsystem(this)
              .withFollowers(
                  Pair.of(
                      new SparkMax(DyeRotorConstants.kickerMotorCanID2, MotorType.kBrushless),
                      false)));

  public void setState(KickerTarget state) {
    switch (state) {
      case On:
        motor.setVelocity(state.speed.get());
        break;
      case Off:
        motor.setDutyCycle(0);
        break;
      default:
        break;
    }
  }

  public void setSpeed(AngularVelocity speed) {
    Utils.warnIfDriverStationOutOfTest();

    if (speed.in(RPM) == 0) {
      stopKicker();
    } else {
      motor.setVelocity(speed);
    }
  }

  public void stopKicker() {
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
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {}
}
