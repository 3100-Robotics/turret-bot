package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;

public class IntakeCommands {
  public static Command setIntakeState(
      IntakeExtension intakeExtension, IntakeExtensionTarget state) {
    return Commands.runEnd(
        () -> intakeExtension.setIntakeExtensionTarget(state),
        intakeExtension::stopIntakeExtension,
        intakeExtension);
  }

  public static Command holdIn(IntakeExtension intakeExtension) {
    return setIntakeState(intakeExtension, IntakeExtensionTarget.Stowed);
  }

  public static Command agitateOneCycle(IntakeExtension intakeExtension) {
    return setIntakeState(intakeExtension, IntakeExtensionTarget.Full)
        .withTimeout(1)
        .andThen(setIntakeState(intakeExtension, IntakeExtensionTarget.Stowed));
  }
}
