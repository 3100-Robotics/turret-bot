package frc.robot.commands.intakecommands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.IntakeCommands;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;

public class Agitate extends SequentialCommandGroup {
  private final IntakeExtension intakeExtension;
  private final Trigger holdTrigger;

  public Agitate(IntakeExtension intakeExtension, Trigger holdTrigger) {
    this.holdTrigger = holdTrigger;
    this.intakeExtension = intakeExtension;

    addCommands(
        IntakeCommands.setIntakeState(intakeExtension, IntakeExtensionTarget.Full),
        Commands.waitUntil(holdTrigger.negate()),
        IntakeCommands.setIntakeState(intakeExtension, IntakeExtensionTarget.Stowed),
        Commands.waitSeconds(0.5));

    addRequirements(intakeExtension);
  }
}
