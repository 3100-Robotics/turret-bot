package frc.robot.commands.testcommands;

import static edu.wpi.first.units.Units.Degrees;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.testcommands.TestCommands.SmartDashboardKeys;
import frc.robot.subsystems.shooter.Turret;

class TestTurret extends Command {
  private final Turret turret;

  public TestTurret(Turret turret) {
    this.turret = turret;

    SmartDashboard.putNumber(SmartDashboardKeys.Turret.getKeyTarget(), 0);
    SmartDashboard.putBoolean(SmartDashboardKeys.Turret.getKeyOther("WrappingOn"), true);
    addRequirements(turret);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if (SmartDashboard.getBoolean(SmartDashboardKeys.Turret.getKeyOther("WrappingOn"), true)) {
      turret.setTurretAngleMod360(
          Degrees.of(SmartDashboard.getNumber(SmartDashboardKeys.Turret.getKeyTarget(), 0)));
    } else {
      turret.setTurretAngleRaw(
          Degrees.of(SmartDashboard.getNumber(SmartDashboardKeys.Turret.getKeyTarget(), 0)));
    }
  }

  @Override
  public void end(boolean inturrupted) {
    turret.stopTurret();
  }
}
