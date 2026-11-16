package frc.robot.commands.drivecommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.subsystems.Drivetrain;
import java.util.function.Supplier;

public class DriveTeleop extends Command {
  private Drivetrain drivetrain;
  private Supplier<Double> xSpeed;
  private Supplier<Double> ySpeed;
  private Supplier<Double> thetaSpeed;
  private Supplier<Double> throttle;
  private final Supplier<Boolean> isShooting;

  public DriveTeleop(
      Drivetrain drivetrain,
      Supplier<Double> xSpeed,
      Supplier<Double> ySpeed,
      Supplier<Double> thetaSpeed,
      Supplier<Double> throttle,
      Supplier<Boolean> isShooting) {
    this.drivetrain = drivetrain;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    this.thetaSpeed = thetaSpeed;
    this.throttle = throttle;
    this.isShooting = isShooting;
    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    drivetrain.setControl(
        DrivetrainConstants.DRIVE_FIELD
            .withVelocityX(
                -xSpeed.get()
                    * throttle.get()
                    * (isShooting.get() ? DrivetrainConstants.DRIVETRAIN_SHOOTING_SPEED_TRANS : 1))
            .withVelocityY(
                -ySpeed.get()
                    * throttle.get()
                    * (isShooting.get() ? DrivetrainConstants.DRIVETRAIN_SHOOTING_SPEED_TRANS : 1))
            .withRotationalRate(
                thetaSpeed.get()
                    * (isShooting.get() ? DrivetrainConstants.DRIVETRAIN_SHOOTING_SPEED_ROT : 1)));
  }

  @Override
  public void end(boolean inturupted) {
    drivetrain.setControl(DrivetrainConstants.IDLE);
  }
}
