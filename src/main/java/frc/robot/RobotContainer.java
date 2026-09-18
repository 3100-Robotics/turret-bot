// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.compoundlogger.LogSubsystemCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.testcommands.TestCommands;
import frc.robot.generated.TunerConstantsFake0621;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.shooter.Turret;

public class RobotContainer {

  private final Drivetrain drivetrain = TunerConstantsFake0621.createDrivetrain();

  // Shooter
  private final Hood hood = new Hood();
  private final Flywheel flywheel = new Flywheel();
  private final Turret turret = new Turret(drivetrain.getPigeon2().getYaw()::getValue);

  // Dye Rotor
  private final Hook hook = new Hook();
  private final Kicker kicker = new Kicker();

  // Intake
  private final IntakeExtension intakeExtension = new IntakeExtension();
  private final IntakeRollers intakeRollers = new IntakeRollers();

  public RobotContainer() {
    configureTeleopBindings();
    configureTeleopDefaultCommands();

    Loggerhead.getInstance().setConfigureCallback(this::configureLogging);
    Loggerhead.getInstance().initializeLogging();
  }

  private void configureTeleopBindings() {}

  public void configureTeleopDefaultCommands() {}

  public void configureTestDefaultCommands() {
    flywheel.setDefaultCommand(TestCommands.testFlywheel(flywheel));
    hood.setDefaultCommand(TestCommands.testHood(hood));
    turret.setDefaultCommand(TestCommands.testTurret(turret));

    hook.setDefaultCommand(TestCommands.testHook(hook));
    kicker.setDefaultCommand(TestCommands.testKicker(kicker));

    intakeExtension.setDefaultCommand(TestCommands.testIntakeExtensionNamed(intakeExtension));
    intakeRollers.setDefaultCommand(TestCommands.testIntakeRollers(intakeRollers));
  }

  private void configureLogging() {
    var rootTable = Loggerhead.getInstance().getRootTable();

    LogMode commandsLogMode = LogMode.NetworkOnly;
    LogMode loggableSubsystemLogMode = LogMode.NetworkOnly;
    var subsystemsTable = rootTable.getSubTable("Subsystems");

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
        .addCompoundLogger(new LogSubsystemCommands("Commands", commandsLogMode, intakeRollers))
        .addLoggable(intakeRollers, loggableSubsystemLogMode);
    // spotless:on
  }

  public void registerPeriodics(Robot robot) {
    robot.addPeriodic(Loggerhead.getInstance()::update, 0.02);
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
