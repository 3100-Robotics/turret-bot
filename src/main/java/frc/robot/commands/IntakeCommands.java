package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;
import frc.robot.targets.IntakeTargets.IntakeRollerTarget;

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
        .withTimeout(0.5)
        .andThen(setIntakeState(intakeExtension, IntakeExtensionTarget.Stowed).withTimeout(0.5));
  }

  public static Command agitate(IntakeExtension intakeExtension, Trigger holdTrigger) {
    // return new ConditionalCommand(
    //         IntakeCommands.holdIn(intakeExtension).until(holdTrigger.negate()),
    //         IntakeCommands.agitateOneCycle(intakeExtension)
    //             .repeatedly()
    //             .until(intakeExtension.isAtSetpoint),
    //         holdTrigger)
    //     .repeatedly();
    // return IntakeCommands.agitateOneCycle(intakeExtension).repeatedly();
    return Commands.runEnd(
        () -> {
          intakeExtension.setIntakeExtensionTarget(IntakeExtensionTarget.Full);
        },
        null,
        intakeExtension);
  }

  public static Command runIntakeRollers(IntakeRollers rollers) {
    return Commands.runEnd(
        () -> rollers.setState(IntakeRollerTarget.On), rollers::stopIntakeRollers, rollers);
  }
}
