// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.generated.TunerConstantsBlastoise1022;
import frc.robot.subsystems.drive.Drivetrain;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;

public class RobotContainer {
  // Subsystems
  private final Drivetrain drive;

  public RobotContainer() {
    WebServer.start(5800, Filesystem.getDeployDirectory().getPath());
    
    switch (Constants.currentMode) {
      case REAL:
        drive =
            new Drivetrain(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.FrontLeft),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.FrontRight),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.BackLeft),
                new ModuleIOTalonFX(TunerConstantsBlastoise1022.BackRight));
        break;
      case SIM:
        drive =
            new Drivetrain(
                new GyroIO() {},
                new ModuleIOSim(TunerConstantsBlastoise1022.FrontLeft),
                new ModuleIOSim(TunerConstantsBlastoise1022.FrontRight),
                new ModuleIOSim(TunerConstantsBlastoise1022.BackLeft),
                new ModuleIOSim(TunerConstantsBlastoise1022.BackRight));
        break;
      default:
        drive =
            new Drivetrain(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        break;
    }
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
