package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.targets.DyeRotorTargets.HookTargets;
import frc.robot.targets.DyeRotorTargets.KickerTarget;

public class DyeRotorCommands {
  public static Command runDyeRotor(Hook hook, Kicker kicker) {

    return Commands.parallel(Commands.waitSeconds(0.09).andThen(runKicker(kicker)), runHook(hook));
  }

  public static Command runHook(Hook hook) {
    return Commands.runEnd(
        () -> hook.setTarget(HookTargets.On), () -> hook.setTarget(HookTargets.Off), hook);
  }

  public static Command runKicker(Kicker kicker) {
    return Commands.runEnd(
        () -> kicker.setState(KickerTarget.On), () -> kicker.setState(KickerTarget.Off), kicker);
  }
}
