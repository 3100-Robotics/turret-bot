package frc.robot.commands.testcommands;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.shooter.Turret;

public class TestCommands {
  public enum SmartDashboardKeys {
    Flywheel(Flywheel.class, Unit.RPM),
    Hood(Hood.class, Unit.Degrees),
    Hook(Hook.class, Unit.RPM),
    IntakeExtensionNamed(IntakeExtension.class, Unit.Enum),
    IntakeExtensionRaw(IntakeExtension.class, Unit.Meters),
    IntakeRollers(IntakeRollers.class, Unit.RPM),
    Kicker(Kicker.class, Unit.RPM),
    Turret(Turret.class, Unit.Degrees);

    public enum Unit {
      RPM,
      Degrees,
      Meters,
      Enum,
      Other,
      None;
    }

    private static final String testPath = "manualSubsystemControlPanel/";
    private static final String testPrefix = "test";

    private final Class<? extends Subsystem> subsystem;
    private final Unit unit;

    private String getKeyCommon() {
      return testPath
          + subsystem.getSimpleName()
          + "/"
          + testPrefix
          + subsystem.getSimpleName()
          + (unit != Unit.Enum ? unit.toString() : "");
    }

    private String getKeyCommon(String extraSpecifier) {
      String[] split = getKeyCommon().split("/" + testPrefix);
      return split[0] + "/" + testPrefix + extraSpecifier + split[1];
    }

    public String getKeyTarget() {
      return getKeyCommon() + "Target";
    }

    public String getKeyTarget(String extraSpecifier) {
      return getKeyCommon(extraSpecifier) + "Target";
    }

    public String getKeyOther(String other) {
      return getKeyCommon() + other;
    }

    public String getKeyOther(String other, String extraSpecifier) {
      return getKeyCommon(extraSpecifier) + other;
    }

    private SmartDashboardKeys(Class<? extends Subsystem> subsystem, Unit unit) {
      // this.key = makeKey(subsystem, unit);
      this.subsystem = subsystem;
      this.unit = unit;
    }
  }

  public static Command testFlywheel(Flywheel flywheel) {
    SmartDashboard.putNumber(SmartDashboardKeys.Flywheel.getKeyTarget(), 0);
    return Commands.runEnd(
        () ->
            flywheel.setSpeed(
                RPM.of(SmartDashboard.getNumber(SmartDashboardKeys.Flywheel.getKeyTarget(), 0))),
        flywheel::stopFlywheel,
        flywheel);
  }

  public static Command testHood(Hood hood) {
    SmartDashboard.putNumber(SmartDashboardKeys.Hood.getKeyTarget(), 0);
    return Commands.runEnd(
        () ->
            hood.setHoodAngle(
                Degrees.of(SmartDashboard.getNumber(SmartDashboardKeys.Hood.getKeyTarget(), 0))),
        hood::stopHood,
        hood);
  }

  public static Command testHook(Hook hook) {
    SmartDashboard.putNumber(SmartDashboardKeys.Hook.getKeyTarget(), 0);
    return Commands.runEnd(
        () ->
            hook.setSpeed(
                RPM.of(SmartDashboard.getNumber(SmartDashboardKeys.Hook.getKeyTarget(), 0))),
        hook::stopHook,
        hook);
  }

  public static Command testIntakeExtensionNamed(IntakeExtension intakeExtension) {
    return new TestIntakeExtensionNamed(intakeExtension);
  }

  public static Command testIntakeExtensionRaw(IntakeExtension intakeExtension) {
    SmartDashboard.putNumber(SmartDashboardKeys.IntakeExtensionRaw.getKeyTarget(), 0);
    return Commands.runEnd(
        () ->
            intakeExtension.setPositionRaw(
                Meters.of(
                    SmartDashboard.getNumber(
                        SmartDashboardKeys.IntakeExtensionRaw.getKeyTarget(), 0))),
        intakeExtension::stopIntakeExtension,
        intakeExtension);
  }

  public static Command testIntakeRollers(IntakeRollers intakeRoller) {
    SmartDashboard.putNumber(SmartDashboardKeys.IntakeRollers.getKeyTarget("Top"), 0);
    SmartDashboard.putNumber(SmartDashboardKeys.IntakeRollers.getKeyTarget("Bottom"), 0);
    return Commands.runEnd(
        () ->
            intakeRoller.setSpeeds(
                RPM.of(
                    SmartDashboard.getNumber(
                        SmartDashboardKeys.IntakeRollers.getKeyTarget("Top"), 0)),
                RPM.of(
                    SmartDashboard.getNumber(
                        SmartDashboardKeys.IntakeRollers.getKeyTarget("Bottom"), 0))),
        intakeRoller::stopIntakeRollers,
        intakeRoller);
  }

  public static Command testKicker(Kicker kicker) {
    SmartDashboard.putNumber(SmartDashboardKeys.Kicker.getKeyTarget(), 0);
    return Commands.runEnd(
        () ->
            kicker.setSpeed(
                RPM.of(SmartDashboard.getNumber(SmartDashboardKeys.Kicker.getKeyTarget(), 0))),
        kicker::stopKicker,
        kicker);
  }

  public static Command testTurret(Turret turret) {
    return new TestTurret(turret);
  }
}
