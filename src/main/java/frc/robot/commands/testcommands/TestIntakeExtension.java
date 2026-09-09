package frc.robot.commands.testcommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeExtension;
import frc.robot.targets.IntakeTargets.IntakeExtensionTarget;

public class TestIntakeExtension extends Command {
  private final IntakeExtension intakeExtension;

  private IntakeExtensionTarget currentTarget = IntakeExtensionTarget.Half;

  public TestIntakeExtension(IntakeExtension intakeExtension) {
    this.intakeExtension = intakeExtension;

    SmartDashboard.putString("testIntakeExtensionTarget", "Half");
    addRequirements(intakeExtension);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    try {
      currentTarget =
          IntakeExtensionTarget.valueOf(
              SmartDashboard.getString("testIntakeExtensionTarget", "Half"));
      SmartDashboard.putBoolean("testIntakeExtensionError", false);
    } catch (Exception e) {
      SmartDashboard.putBoolean("testIntakeExtensionError", true);
    }

    intakeExtension.setIntakeExtensionTarget(currentTarget);
  }

  @Override
  public void end(boolean inturrupted) {
    intakeExtension.stopIntakeExtension();
  }
}
