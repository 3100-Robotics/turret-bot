package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.subsystems.Drivetrain;
import java.util.function.DoubleSupplier;

public class BlastoiseCommands {
  public static Command driveTeleopCommand(
      Drivetrain drivetrain,
      DoubleSupplier xPercent,
      DoubleSupplier yPercent,
      DoubleSupplier tPercent,
      DoubleSupplier gasPercent) {
    return Commands.runEnd(
        () ->
            DrivetrainConstants.DRIVE_FIELD
                .withVelocityX(-xPercent.getAsDouble() * gasPercent.getAsDouble())
                .withVelocityY(-yPercent.getAsDouble() * gasPercent.getAsDouble())
                .withRotationalRate(tPercent.getAsDouble()),
        () -> drivetrain.setControl(DrivetrainConstants.IDLE),
        drivetrain);
  }

  public static Command shootCommand() {
    return Commands.parallel(
        // Dye rotor
        Commands.waitSeconds(1)
            .andThen(
                Commands.run(() -> {}) // Run dye rotor
                    .until(() -> false)
                    .repeatedly()),
        // Shooter
        Commands.none() // Activate flywheel / hood
        );
  }
}
