package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.KickerMotorTarget;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class Kicker extends SubsystemBase {
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

  public void setState(KickerMotorTarget state) {
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

  @Override
  public void periodic() {
    motor.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    motor.simIterate();
  }
}
