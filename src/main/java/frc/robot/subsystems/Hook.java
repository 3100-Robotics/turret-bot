package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.HookTargets;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class Hook extends LightSubsystem {
  private SparkMax rawMotor = new SparkMax(DyeRotorConstants.hookMotorCanID, MotorType.kBrushless);
  private SmartMotorController motor =
      new SparkWrapper(rawMotor, DCMotor.getNEO(1), DyeRotorConstants.hookMotorConfig);

  public void setTarget(HookTargets targetVelocity) {
    switch (targetVelocity) {
      case Off:
        motor.setDutyCycle(0);
        break;
      default:
        motor.setVelocity(targetVelocity.speed.get());
        break;
    }
  }

  @Override
  public void periodic() {
    motor.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    motor.simIterate();
  }
}
