package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.DyeRotorConstants;
import frc.robot.targets.DyeRotorTargets.HookTargets;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.local.SparkWrapper;

public class Hook extends SubsystemBase {
    private SparkMax rawMotor = new SparkMax(DyeRotorConstants.hookMotorCanID, MotorType.kBrushless);
    private SmartMotorController motor = new SparkWrapper(rawMotor, DCMotor.getNEO(1), DyeRotorConstants.hookMotorConfig);

    public void setTarget(HookTargets targetVelocity) {
        switch (targetVelocity) {
            case Off:
                motor.setDutyCycle(0);
                break;
            default:
                motor.setVelocity(targetVelocity.speed);
                break;
        }
    }
}
