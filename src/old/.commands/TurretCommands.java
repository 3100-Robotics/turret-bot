package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.shooter.Turret;
import java.util.function.Supplier;

public class TurretCommands {
  public static Command setTurretAngleFieldOriented(
      Turret turret, Supplier<Pose2d> robotPoseSupplier, Supplier<Angle> turretSetpoint) {
    return Commands.none();
  }
}
