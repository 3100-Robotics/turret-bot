// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Degrees;

import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.Loggerhead;
import com.sbdc.loggerhead.logging.compoundlogger.LogSubsystemCommands;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.BlastoiseCommands;
import frc.robot.commands.drivecommands.DrivePointAtAngle;
import frc.robot.commands.drivecommands.DriveTeleop;
import frc.robot.commands.testcommands.TestCommands;
import frc.robot.generated.TunerConstantsFake0621;
import frc.robot.sotm.DashboardStaticShotMap;
import frc.robot.sotm.DynamicShotMap;
import frc.robot.sotm.SOTMStateTracker;
import frc.robot.sotm.ShotMap;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.shooter.Turret;
import frc.robot.vision.HPoseEstimator;
import frc.robot.vision.MainVision;
import java.util.EnumSet;
import java.util.HashMap;

public class RobotContainer {
  public static enum BlastoiseActions {
    shootCommand,
    autoAlignCommand,
    intakeHighCommand,
    intakeMidLowToggleCommand,
    intakeRunCommand,

    resetHeadingAllCommand,
    resetHeadingSimCommand,
    resetHeadingCTRECommand,
    resetHeadingGyroCommand,
    resetHeadingGyroWrapperCommand,
    resetHeadingHPoseCommand,
    ;
  }

  public static enum PoseHeadingResetParams {
    gyroReset,
    gyroWrapperReset,
    simPoseReset,
    CTREReset,
    hPoseReset;
  }

  private final HashMap<String, Command> blastoiseCommandMap = new HashMap<>();

  private final Drivetrain drivetrain = TunerConstantsFake0621.createDrivetrain();
  private final ShotMap dashShotMap = new DashboardStaticShotMap();
  private final SOTMStateTracker sotmstate = new SOTMStateTracker(drivetrain);
  private final DynamicShotMap dynamicShotMap = new DynamicShotMap(sotmstate);

  private final HPoseEstimator hPoseEstimator = new HPoseEstimator(drivetrain);

  public final MainVision mainVision =
      new MainVision(
          (Pose2d visionRobotPoseMeters,
              double timestampSeconds,
              Matrix<N3, N1> visionMeasurementStdDevs) -> {
            hPoseEstimator.addVisionMeasurement(
                visionRobotPoseMeters, timestampSeconds, visionMeasurementStdDevs);
          },
          Robot.isReal()
              ? () -> Pose2d.kZero
              : () ->
                  drivetrain.mapleSimSwerveDrivetrain.mapleSimDrive.getSimulatedDriveTrainPose(),
          drivetrain);

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

  private final PowerDistribution pdh = new PowerDistribution(14, ModuleType.kRev);

  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController coDriverController = new CommandXboxController(1);

  public RobotContainer() {
    WebServer.start(5800, Filesystem.getDeployDirectory().getPath());
    createActionMap();

    // DO replace these function calls with the new ones you currently want to use then redeploy
    // code
    configureTeleopCoDriverBindings();
    configureTeleopDriverBindings();

    configureTeleopDefaultCommands();

    // Loggerhead.getInstance().setConfigureCallback(this::configureLogging);
    // Loggerhead.getInstance().initializeLogging();
    Loggerhead.getInstance()
        .applyToConfigurator(
            configurator ->
                configurator
                    .setConfigureCallback(this::configureLogging)
                    .addHook(DriverStation::isFMSAttached))
        .initializeLogging(); // Must be the final call in the configuration chain
  }

  private void createActionMap() {
    Command shootCommand =
        BlastoiseCommands.shootCommand(
            flywheel, hood, turret, hook, kicker, intakeExtension, intakeRollers, dashShotMap);

    Command autoAlignCommand =
        new ProxyCommand(
            new DrivePointAtAngle(
                drivetrain,
                driverController::getLeftY,
                driverController::getLeftX,
                driverController::getRightTriggerAxis,
                sotmstate::getHeading));

    // Command intakeHighCommand = IntakeCommands.pivotHigh(intakePivot);
    // Command intakeMidLowToggleCommand = IntakeCommands.pivotMidLowToggle(intakePivot);
    // Command intakeRunCommand = IntakeCommands.rollerForward(intakeRoller);

    Command resetHeadingAllCommand =
        Commands.runOnce(() -> resetPoseAndHeadings(EnumSet.allOf(PoseHeadingResetParams.class)));

    Command resetHeadingSimCommand =
        Commands.runOnce(
            () -> resetPoseAndHeadings(EnumSet.of(PoseHeadingResetParams.simPoseReset)));

    Command resetHeadingCTRECommand =
        Commands.runOnce(() -> resetPoseAndHeadings(EnumSet.of(PoseHeadingResetParams.CTREReset)));

    Command resetHeadingGyroCommand =
        Commands.runOnce(() -> resetPoseAndHeadings(EnumSet.of(PoseHeadingResetParams.gyroReset)));

    Command resetHeadingGyroWrapperCommand =
        Commands.runOnce(
            () -> resetPoseAndHeadings(EnumSet.of(PoseHeadingResetParams.gyroWrapperReset)));

    Command resetHeadingHPoseCommand =
        Commands.runOnce(() -> resetPoseAndHeadings(EnumSet.of(PoseHeadingResetParams.hPoseReset)));

    blastoiseCommandMap.put("shootCommand", shootCommand);
    blastoiseCommandMap.put("autoAlignCommand", autoAlignCommand);
    // blastoiseCommandMap.put("intakeHighCommand", intakeHighCommand);
    // blastoiseCommandMap.put("intakeMidLowToggleCommand", intakeMidLowToggleCommand);
    // blastoiseCommandMap.put("intakeRunCommand", intakeRunCommand);

    blastoiseCommandMap.put("resetHeadingAllCommand", resetHeadingAllCommand);
    blastoiseCommandMap.put("resetHeadingSimCommand", resetHeadingSimCommand);
    blastoiseCommandMap.put("resetHeadingCTRECommand", resetHeadingCTRECommand);
    blastoiseCommandMap.put("resetHeadingGyroCommand", resetHeadingGyroCommand);
    blastoiseCommandMap.put("resetHeadingGyroWrapperCommand", resetHeadingGyroWrapperCommand);
    blastoiseCommandMap.put("resetHeadingHPoseCommand", resetHeadingHPoseCommand);
  }

  private void resetPoseAndHeadings(EnumSet<PoseHeadingResetParams> resetParams) {
    if (resetParams.contains(PoseHeadingResetParams.CTREReset)) {
      drivetrain.resetPose(new Pose2d(1, 1, Rotation2d.kZero));
    }

    if (resetParams.contains(PoseHeadingResetParams.hPoseReset)) {
      hPoseEstimator.reset(new Pose2d(1, 1, Rotation2d.kZero), true, true);
    }

    if (resetParams.contains(PoseHeadingResetParams.gyroWrapperReset)) {
      drivetrain.getPigeon2().setYaw(Degrees.of(0));
      drivetrain.getPigeon2().reset();
    }

    if (resetParams.contains(PoseHeadingResetParams.gyroReset)) {
      mainVision.p2vwrapper.zero();
    }

    if (!Robot.isReal() && resetParams.contains(PoseHeadingResetParams.simPoseReset)) {
      drivetrain.mapleSimSwerveDrivetrain.mapleSimDrive.setSimulationWorldPose(
          new Pose2d(1, 1, Rotation2d.kZero));
    }
  }

  private void configureTeleopDriverBindings() {}

  private void configureTeleopCoDriverBindings() {}

  public void configureTeleopDefaultCommands() {
    drivetrain.setDefaultCommand(
        new DriveTeleop(
            drivetrain,
            driverController::getLeftY,
            driverController::getLeftX,
            () -> -driverController.getRightX(),
            driverController::getRightTriggerAxis));
  }

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
    robot.addPeriodic(MatchContext.getInstance()::update, 0.02);
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
