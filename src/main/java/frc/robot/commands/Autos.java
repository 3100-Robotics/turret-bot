package frc.robot.commands;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.shooter.Turret;

public class Autos {
  private final AutoFactory autoFactory;
  private final AutoChooser autoChooser;

  private final RobotContainer rc;
  private final Drivetrain drivetrain;
  private final Hook hook;
  private final Kicker kicker;
  private final IntakeExtension intakeExtension;
  private final IntakeRollers intakeRollers;
  private final Flywheel flywheel;
  private final Hood hood;
  private final Turret turret;

  public Autos(
      RobotContainer rc,
      Drivetrain drivetrain,
      Hook hook,
      Kicker kicker,
      IntakeExtension intakeExtension,
      IntakeRollers intakeRollers,
      Flywheel flywheel,
      Hood hood,
      Turret turret) {
    this.rc = rc;
    this.drivetrain = drivetrain;
    this.hook = hook;
    this.kicker = kicker;
    this.intakeExtension = intakeExtension;
    this.intakeRollers = intakeRollers;
    this.flywheel = flywheel;
    this.hood = hood;
    this.turret = turret;

    autoFactory =
        new AutoFactory(
            () -> drivetrain.getState().Pose, // A function that returns the current robot pose
            drivetrain
                ::resetPose, // A function that resets the current robot pose to the provided Pose2d
            drivetrain::followTrajectory, // The drive subsystem trajectory follower
            true, // If alliance flipping should be enabled
            drivetrain // The drive subsystem
            );

    CommandScheduler.getInstance().schedule(autoFactory.warmupCmd());
    configureNamedCommands();

    autoChooser = new AutoChooser("Do Nothing? I have nothing!");
    autoChooser.addRoutine("rightTrench", this::rightTrench);

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  public Command getSelectedCommand() {
    return autoChooser.selectedCommand();
  }

  private void configureNamedCommands() {
    // Bind commands for Choreo event markers and path events
    // autoFactory.bind("SpinUpShooter", shooter.runVelocityCommand(3766));
    // autoFactory.bind("AimTurretHub", turret.aimAtHubCommand());
    autoFactory.bind(
        "Shoot",
        BlastoiseCommands.shootCommand(
                flywheel,
                hood,
                turret,
                hook,
                kicker,
                intakeExtension,
                intakeRollers,
                rc.dynamicShotMap,
                new Trigger(() -> false))
            .withTimeout(10));
  }

  public AutoRoutine rightTrench() {
    AutoRoutine routine = autoFactory.newRoutine("rightTrench");

    AutoTrajectory main = routine.trajectory("rightTrench");

    routine.active().onTrue(Commands.sequence(main.resetOdometry(), main.cmd()));
    main.atTime("startIntake")
        .onTrue(
            IntakeCommands.holdDeploy(intakeExtension)
                .alongWith(IntakeCommands.runIntakeRollers(intakeRollers))
                .until(main.atTime("endIntake")));

    return routine;
  }
}
