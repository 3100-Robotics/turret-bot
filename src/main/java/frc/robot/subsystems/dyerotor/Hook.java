package frc.robot.subsystems.dyerotor;

import static edu.wpi.first.units.Units.RPM;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.HookTargets;
import frc.robot.utils.Utils;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class Hook extends LightSubsystem implements Loggable {
  private SparkMax rawMotor = new SparkMax(DyeRotorConstants.hookMotorCanID, MotorType.kBrushless);
  private SmartMotorController motor =
      new SparkWrapper(rawMotor, DCMotor.getNEO(1), DyeRotorConstants.hookMotorConfig);

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

  public void setSpeed(AngularVelocity speed) {
    Utils.warnIfDriverStationOutOfTest();

    if (speed.in(RPM) == 0) {
      stopHook();
    } else {
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
    parentTable.addDoubleLogger(
        "hookSpeedRPM", logMode, () -> motor.getMechanismVelocity().in(RPM));
  }
}
