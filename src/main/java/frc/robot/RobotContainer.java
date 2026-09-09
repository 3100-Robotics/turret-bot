// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.compoundlogger.LogSubsystemCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.testcommands.TestIntakeExtension;
import frc.robot.commands.testcommands.TestTurret;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Hood;
import frc.robot.subsystems.Hook;
import frc.robot.subsystems.IntakeExtension;
import frc.robot.subsystems.IntakeRoller;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Turret;

public class RobotContainer {

  // Shooter
  private final Hood hood = new Hood();
  private final Flywheel flywheel = new Flywheel();
  private final Turret turret = new Turret();

  // Dye Rotor
  private final Hook hook = new Hook();
  private final Kicker kicker = new Kicker();

  // Intake
  private final IntakeExtension intakeExtension = new IntakeExtension();
  private final IntakeRoller intakeRoller = new IntakeRoller();

  public RobotContainer() {
    configureTeleopBindings();
    configureTeleopDefaultCommands();

    Loggerhead.getInstance().setConfigureCallback(this::configureLogging);
    Loggerhead.getInstance().initializeLogging();
  }

  private void configureTeleopBindings() {}

  public void configureTeleopDefaultCommands() {}

  public void configureTestDefaultCommands() {
    turret.setDefaultCommand(new TestTurret(turret));
    intakeExtension.setDefaultCommand(new TestIntakeExtension(intakeExtension));
  }

  private void configureLogging() {
    var rootTable = Loggerhead.getInstance().getRootTable();

    LogMode commandsLogMode = LogMode.NetworkOnly;
    LogMode loggableSubsystemLogMode = LogMode.NetworkOnly;
    var subsystemsTable = rootTable.getSubTable("Subsystems1");

    // spotless:off
    subsystemsTable
        .getSubTable("hood")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, hood))
        .addLoggable(hood, loggableSubsystemLogMode); 
    subsystemsTable
        .getSubTable("flywheel")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, flywheel))
        .addLoggable(flywheel, loggableSubsystemLogMode);
    subsystemsTable
        .getSubTable("turret")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, turret))
        .addLoggable(turret, loggableSubsystemLogMode);
    subsystemsTable
        .getSubTable("hook")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, hook))
        .addLoggable(hook, loggableSubsystemLogMode);
    subsystemsTable
        .getSubTable("kicker")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, kicker))
        .addLoggable(kicker, loggableSubsystemLogMode);
    subsystemsTable
        .getSubTable("intakeExtension")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, intakeExtension))
        .addLoggable(intakeExtension, loggableSubsystemLogMode);
    subsystemsTable
        .getSubTable("intakeRoller")
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, intakeRoller))
        .addLoggable(intakeRoller, loggableSubsystemLogMode);
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, hood))
    // .getParent()
    // .getSubTable("flywheel")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, flywheel))
    // .getParent()
    // .getSubTable("turret")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, turret))
    // .getParent()
    // .getSubTable("hook")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, hook))
    // .getParent()
    // .getSubTable("kicker")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, kicker))
    // .getParent()
    // .getSubTable("intakeExtension")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, intakeExtension))
    // .getParent()
    // .getSubTable("intakeRoller")
    // .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, intakeRoller))

    // spotless:on
  }

  public void registerPeriodics(Robot robot) {
    robot.addPeriodic(Loggerhead.getInstance()::update, 0.02);
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
