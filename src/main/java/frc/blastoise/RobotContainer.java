// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.blastoise;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.blastoise.constants.AKitConstants;
import frc.blastoise.generated.TunerConstantsBlastoise1022;
import frc.blastoise.subsystems.drive.Drivetrain;
import frc.blastoise.subsystems.drive.GyroIO;
import frc.blastoise.subsystems.drive.GyroIOPigeon2;
import frc.blastoise.subsystems.drive.ModuleIO;
import frc.blastoise.subsystems.drive.ModuleIOSim;
import frc.blastoise.subsystems.drive.ModuleIOTalonFX;
import frc.blastoise.subsystems.intakeextension.IntakeExtension;

public class RobotContainer {
  // Subsystems
  private final Drivetrain drivetrain;
  private final IntakeExtension intakeExtension;

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
        break;
      case default:
        break;
    }
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
