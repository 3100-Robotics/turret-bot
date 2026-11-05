package frc.robot.commands.testcommands;

import com.sbdc.loggerhead.logging.OneShot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.shooter.Flywheel;

class TestMultiFlywheelSpeeds extends SequentialCommandGroup {
  private static final double[] rpmSpeeds = {1000, 2000, 3000, 4000};

  private final Flywheel flywheel;

  public TestMultiFlywheelSpeeds(Flywheel flywheel) {
    this.flywheel = flywheel;

    for (double rpmSpeed : rpmSpeeds) {
      addCommands(getOneRunCommand(rpmSpeed));
    }

    addRequirements(flywheel);
  }

  private Command getOneRunCommand(double rpmSpeed) {
    return Commands.runOnce(
            () -> {
              SmartDashboard.putNumber(
                  TestCommands.SmartDashboardKeys.Flywheel.getKeyTarget(), rpmSpeed);
              OneShot.setDouble("MultiFlywheelTestRPM", rpmSpeed);
            })
        .andThen(TestCommands.testFlywheel(flywheel).withTimeout(4));
  }
}
