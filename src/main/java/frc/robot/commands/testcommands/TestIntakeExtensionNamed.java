package frc.robot.commands.testcommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.testcommands.TestCommands.SmartDashboardKeys;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;

class TestIntakeExtensionNamed extends Command {
  private final IntakeExtension intakeExtension;

  private IntakeExtensionTarget currentTarget = IntakeExtensionTarget.Half;

  public TestIntakeExtensionNamed(IntakeExtension intakeExtension) {
    this.intakeExtension = intakeExtension;

    SmartDashboard.putString(SmartDashboardKeys.IntakeExtensionNamed.getKeyTarget(), "Half");
    addRequirements(intakeExtension);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    try {
      currentTarget =
          IntakeExtensionTarget.valueOf(
              SmartDashboard.getString(
                  SmartDashboardKeys.IntakeExtensionNamed.getKeyTarget(), "Half"));
      SmartDashboard.putBoolean(
          SmartDashboardKeys.IntakeExtensionNamed.getKeyOther("Error"), false);
    } catch (Exception e) {
      SmartDashboard.putBoolean(SmartDashboardKeys.IntakeExtensionNamed.getKeyOther("Error"), true);
    }

    intakeExtension.setIntakeExtensionTarget(currentTarget);
  }

  @Override
  public void end(boolean inturrupted) {
    intakeExtension.stopIntakeExtension();
  }
}
