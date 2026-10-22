package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.sotm.ShotMap;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;

public class ShooterCommands {
  public static Command flywheelsDynamic(Flywheel flywheel, ShotMap shotMap) {
    return Commands.runEnd(
            () -> {
              flywheel.setSpeed(shotMap.getFlywheelSpeed());
            },
            () -> {
              flywheel.stopFlywheel();
            },
            flywheel)
        .withName("flywheelsDynamic w target: " + shotMap.getTarget());
  }

  public static Command hoodDynamic(Hood hood, ShotMap shotMap) {
    return Commands.runEnd(
            () -> {
              hood.setHoodAngle(shotMap.getHoodAngle());
            },
            () -> {
              hood.stopHood();
            },
            hood)
        .withName("hoodDynamic w target: " + shotMap.getTarget());
  }

  public static Command shooterDynamic(Hood hood, Flywheel flywheel, ShotMap shotMap) {
    return hoodDynamic(hood, shotMap)
        .alongWith(flywheelsDynamic(flywheel, shotMap))
        .withName("shooterDynamic w target: " + shotMap.getTarget());
  }
}
