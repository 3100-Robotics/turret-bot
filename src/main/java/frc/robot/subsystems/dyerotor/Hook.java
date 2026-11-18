package frc.robot.subsystems.dyerotor;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RPM;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.sbdc.loggerhead.core.LightSubsystem;
import com.sbdc.loggerhead.core.LoggerheadUtils;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.HookTargets;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorController.ClosedLoopControllerSlot;
import yams.motorcontrollers.local.SparkWrapper;

public class Hook extends LightSubsystem implements Loggable {
  private SparkMax rawMotor = new SparkMax(DyeRotorConstants.hookMotorCanID, MotorType.kBrushless);
  private SmartMotorController motor =
      new SparkWrapper(
          rawMotor, DCMotor.getNEO(1), DyeRotorConstants.hookMotorConfig.withSubsystem(this));

  public void setTarget(HookTargets targetVelocity) {
    switch (targetVelocity) {
      case Off:
        stopHook();
        break;
      default:
        motor.setVelocity(targetVelocity.speed.get());
        break;
    }
  }

  public void getOutOfIntakeRollersWay() {
    motor.setPosition(Degrees.zero());
  }

  public void setSpeed(AngularVelocity speed) {
    LoggerheadUtils.warnIfDriverStationOutOfTest();

    if (speed.in(RPM) == 0) {
      stopHook();
    } else {
      motor.setClosedLoopSlot(ClosedLoopControllerSlot.SLOT_0);
      motor.setVelocity(speed);
    }
  }

  public void stopHook() {
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
    LoggerheadUtils.setupBasicMotorLogging(
        "hook",
        parentTable,
        logMode,
        loggerhead,
        motor); // , Utils.BasicSMCLoggingMode.SpeedAngular,
    // Utils.BasicSMCLoggingMode.PositionalAngular);
  }
}
