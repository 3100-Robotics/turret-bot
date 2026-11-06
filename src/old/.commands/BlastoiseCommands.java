package frc.blastoise.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.blastoise.constants.DrivetrainConstants;
import frc.blastoise.sotm.ShotMap;
import frc.blastoise.subsystems.drive.Drivetrain;
import frc.blastoise.subsystems.dyerotor.Hook;
import frc.blastoise.subsystems.dyerotor.Kicker;
import frc.blastoise.subsystems.intake.IntakeExtension;
import frc.blastoise.subsystems.intake.IntakeRollers;
import frc.blastoise.subsystems.shooter.Flywheel;
import frc.blastoise.subsystems.shooter.Hood;
import frc.blastoise.subsystems.shooter.Turret;
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
