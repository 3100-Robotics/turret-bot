package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggable;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.util.LightSubsystem;
import edu.wpi.first.math.Pair;
import frc.robot.constants.IntakeConstants;
import frc.robot.targets.IntakeTargets.IntakeRollerTarget;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class IntakeRoller extends LightSubsystem implements Loggable {
  private final TalonFX topRawMotor1 = new TalonFX(IntakeConstants.topRollerMotorCanID1);
  private final TalonFX topRawMotor2 = new TalonFX(IntakeConstants.topRollerMotorCanID2);
  private final TalonFX bottomRawMotor = new TalonFX(IntakeConstants.bottomRollerMotorCanID);

  private final SmartMotorController topMotor =
      new TalonFXWrapper(
          topRawMotor1,
          IntakeConstants.topRollerMotorPhysical,
          IntakeConstants.topRollerMotorConfig
              .withSubsystem(this)
              .withFollowers(Pair.of(topRawMotor2, false)));

  private final SmartMotorController bottomMotor =
      new TalonFXWrapper(
          bottomRawMotor,
          IntakeConstants.bottomRollerMotorPhysical,
          IntakeConstants.bottomRollerMotorConfig.withSubsystem(this));

  public void setState(IntakeRollerTarget state) {
    switch (state) {
      case On:
        topMotor.setVelocity(state.topSpeed.get());
        bottomMotor.setVelocity(state.bottomSpeed.get());
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
    bottomMotor.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
    topMotor.simIterate();
    bottomMotor.simIterate();
  }

  @Override
  public void setupLogging(Table parentTable, LogMode logMode, Loggerhead loggerhead) {}
}
