// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.blastoise;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.blastoise.constants.OperatorConstants.OperatorsControllerType;
import frc.blastoise.generated.TunerConstantsBlastoise1022;
import frc.blastoise.subsystems.drive.Drivetrain;
import frc.blastoise.subsystems.drive.GyroIO;
import frc.blastoise.subsystems.drive.GyroIOPigeon2;
import frc.blastoise.subsystems.drive.ModuleIO;
import frc.blastoise.subsystems.drive.ModuleIOSim;
import frc.blastoise.subsystems.drive.ModuleIOTalonFX;
import frc.blastoise.subsystems.flywheel.Flywheel;
import frc.blastoise.subsystems.hood.Hood;
import frc.blastoise.subsystems.hook.Hook;
import frc.blastoise.subsystems.intakeextension.IntakeExtension;
import frc.blastoise.subsystems.intakeextension.IntakeExtensionIO.IntakeExtensionIOReplay;
import frc.blastoise.subsystems.turret.Turret;
import frc.blastoise.subsystems.intakeextension.IntakeExtensionIOTalonFX;
import frc.blastoise.subsystems.intakerollers.IntakeRollers;
import frc.blastoise.subsystems.kicker.Kicker;
import frc.blastoise.util.CommandXboxStyleController;

public class RobotContainer {
  // Subsystems
  private final Drivetrain drivetrain;

  private final Hood hood;
  private final Flywheel flywheel;
  private final Turret turret;

  private final Hook hook;
  private final Kicker kicker;

  private final IntakeExtension intakeExtension;
  private final IntakeRollers intakeRollers;

  private final CommandXboxStyleController driverController =
      new CommandXboxStyleController(0, OperatorsControllerType.PlayStation);

  public RobotContainer() {
    WebServer.start(5800, Filesystem.getDeployDirectory().getPath());

    switch (AKitConstants.currentMode) {
      case REAL:
        drivetrain =
            new Drivetrain(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.FrontLeft),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.FrontRight),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.BackLeft),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.BackRight));
        break;
      case SIM:
        drivetrain =
            new Drivetrain(
                new GyroIO() {},
                new ModuleIOSim(TunerConstantsBlastoise1022.FrontLeft),
                new ModuleIOSim(TunerConstantsBlastoise1022.FrontRight),
                new ModuleIOSim(TunerConstantsBlastoise1022.BackLeft),
                new ModuleIOSim(TunerConstantsBlastoise1022.BackRight));
        break;
      default:
        drivetrain =
            new Drivetrain(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        break;
    }

    switch (AKitConstants.currentMode) {
      case REPLAY:
        intakeExtension = new IntakeExtension(new IntakeExtensionIOReplay());
        intakeRollers = new IntakeRollers(new IntakeRollersIOReplay());
        break;
      default:
        intakeExtension = new IntakeExtension(IntakeExtensionIOTalonFX::new);
        break;
    }

    configureBindings();
  }

  public void configureBindings() {
    driverController.north().whileTrue(intakeExtension.setDistance(Meters.of(1)));
    driverController.south().whileTrue(intakeExtension.setDistance(Meters.of(0)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
