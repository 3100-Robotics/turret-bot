package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.sotm.ShotMap;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.shooter.Turret;
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

  public static Command shootCommand(
      Flywheel flywheel,
      Hood hood,
      Turret turret,
      Hook hook,
      Kicker kicker,
      IntakeExtension intakeExtension,
      IntakeRollers intakeRoller,
      ShotMap shotMap) {
    return Commands.parallel(
        // Dye rotor
        Commands.waitSeconds(1)
            .andThen(
                DyeRotorCommands.runDyeRotor(hook, kicker) // Run dye rotor ...
                    .until(turret.isNearSetpoint.negate()), // ... until the turret hasto flip
                Commands.waitUntil(turret.isNearSetpoint) // Hold until its back
                    .repeatedly() // back to top
                ),
        // Shooter
        ShooterCommands.shooterDynamic(hood, flywheel, shotMap));
  }
}
