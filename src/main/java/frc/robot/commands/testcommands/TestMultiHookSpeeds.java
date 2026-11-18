package frc.robot.commands.testcommands;

import com.sbdc.loggerhead.logging.OneShot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.dyerotor.Hook;

public class TestMultiHookSpeeds extends SequentialCommandGroup {
  private static final double[] rpmSpeeds = {40, 50, 60, 70, 80, 90, 100, 110};

  private final Hook hook;

  public TestMultiHookSpeeds(Hook hook) {
    this.hook = hook;

    for (double rpmSpeed : rpmSpeeds) {
      addCommands(getOneRunCommand(rpmSpeed));
    }

    addRequirements(hook);
  }

  private Command getOneRunCommand(double rpmSpeed) {
    return Commands.runOnce(
            () -> {
              SmartDashboard.putNumber(
                  TestCommands.SmartDashboardKeys.Hook.getKeyTarget(), rpmSpeed);
              OneShot.setDouble("MultiHookTestRPM", rpmSpeed);
            })
        .andThen(TestCommands.testHook(hook).withTimeout(1.5));
  }
}
