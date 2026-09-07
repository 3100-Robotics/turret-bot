// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hook;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Turret;

public class RobotContainer {

  // Shooter
  private Hood hood = new Hood();
  private Flywheel flywheel = new Flywheel();
  private Turret turret = new Turret();

  // Dye Rotor
  private Hook hook = new Hook();
  private Kicker kicker = new Kicker();

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
