package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IntakeConstants;
import frc.robot.targets.IntakeTargets.IntakeRollerMotorTargets;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeRoller extends SubsystemBase {
    private final TalonFX topRawMotor = 
        new TalonFX(IntakeConstants.topIntakeRollerMotorCanID1);

    private final SmartMotorController topMotor =
        new TalonFXWrapper(
            topRawMotor, 
            IntakeConstants.intakeRollerMotorPhysical, 
            IntakeConstants.topIntakeRollerMotorConfig
                .withSubsystem(this)
                .withFollowers(
                    Pair.of(
                        new TalonFX(IntakeConstants.topIntakeRollerMotorCanID2), false
                    )));
    
    private final TalonFX bottomRawMotor =
        new TalonFX(IntakeConstants.bottomIntakeRollerMotorCanID);

    private final SmartMotorController bottomMotor =
        new TalonFXWrapper(
            bottomRawMotor,
            IntakeConstants.bottomIntakeRollerMotorPhysical,
            IntakeConstants.bottomIntakeRollerMotorConfig
                .withSubsystem(this)
                );

    public void setState(IntakeRollerMotorTargets state) {
        switch (state) {
            case On:
                topMotor.setVelocity(state.topSpeed.get());
                bottomMotor.setVelocity(state.topSpeed.get());
                break;
            case Off:
                topMotor.setDutyCycle(0);
                bottomMotor.setDutyCycle(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void periodic() {
        topMotor.updateTelemetry();
    }

    @Override
    public void simulationPeriodic() {
        topMotor.simIterate();
    }
}

