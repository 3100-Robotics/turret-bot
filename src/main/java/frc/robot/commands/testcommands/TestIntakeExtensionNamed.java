package frc.robot.commands.testcommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;

class TestIntakeExtensionNamed extends Command {
  private final IntakeExtension intakeExtension;

  private IntakeExtensionTarget currentTarget = IntakeExtensionTarget.Half;

  public TestIntakeExtensionNamed(IntakeExtension intakeExtension) {
    this.intakeExtension = intakeExtension;

    SmartDashboard.putString("testIntakeExtensionNamedTarget", "Half");
    addRequirements(intakeExtension);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    try {
      currentTarget =
          IntakeExtensionTarget.valueOf(
              SmartDashboard.getString("testIntakeExtensionNamedTarget", "Half"));
      SmartDashboard.putBoolean("testIntakeExtensionNamedError", false);
    } catch (Exception e) {
      SmartDashboard.putBoolean("testIntakeExtensionNamedError", true);
    }

    intakeExtension.setIntakeExtensionTarget(currentTarget);
  }

  @Override
  public void end(boolean inturrupted) {
    intakeExtension.stopIntakeExtension();
  }
}
