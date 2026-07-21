package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ShooterConstants;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class Hood extends SubsystemBase {
  private final TalonFX rawMotor = new TalonFX(ShooterConstants.hoodMotorCanID);

  private final SmartMotorController motor =
      new TalonFXWrapper(
          rawMotor,
          ShooterConstants.hoodMotorPhysical,
          ShooterConstants.hoodMotorConfig.withSubsystem(this));

  public void setAngle(Angle angle) {
    motor.setPosition(angle);
  }

  public void motorOff() {
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
}
