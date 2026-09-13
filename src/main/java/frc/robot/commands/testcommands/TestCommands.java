package frc.robot.commands.testcommands;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.RPM;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.shooter.Flywheel;
import frc.robot.subsystems.shooter.Hood;
import frc.robot.subsystems.dyerotor.Hook;
import frc.robot.subsystems.intake.IntakeExtension;
import frc.robot.subsystems.intake.IntakeRollers;
import frc.robot.subsystems.dyerotor.Kicker;
import frc.robot.subsystems.shooter.Turret;

public class TestCommands {
    public static Command testFlywheel(Flywheel flywheel) {
        SmartDashboard.putNumber("testFlywheelRPMTarget", 0);
        return Commands.runEnd(() -> flywheel.setSpeed(RPM.of(SmartDashboard.getNumber("testFlywheelRPMTarget", 0))), flywheel::stopFlywheel, flywheel);
    }

    public static Command testHood(Hood hood) {
        SmartDashboard.putNumber("testHoodDegreesTarget", 0);
        return Commands.runEnd(() -> hood.setHoodAngle(Degrees.of(SmartDashboard.getNumber("testHoodDegreesTarget", 0))), hood::stopHood, hood);
    }

    public static Command testHook(Hook hook) {
        SmartDashboard.putNumber("testHookRPMTarget", 0);
        return Commands.runEnd(() -> hook.setSpeed(RPM.of(SmartDashboard.getNumber("testHookRPMTarget", 0))), hook::stopHook, hook);
    }

    public static Command testIntakeExtensionNamed(IntakeExtension intakeExtension) {
        return new TestIntakeExtensionNamed(intakeExtension);
    }

    public static Command testIntakeExtensionRaw(IntakeExtension intakeExtension) {
        SmartDashboard.putNumber("testIntakeExtensionMetersTarget", 0);
        return Commands.runEnd(() -> intakeExtension.setPositionRaw(Meters.of(SmartDashboard.getNumber("testIntakeExtensionMetersTarget", 0))), intakeExtension::stopIntakeExtension, intakeExtension);
    }

    public static Command testIntakeRollers(IntakeRollers intakeRoller) {
        SmartDashboard.putNumber("testTopIntakeRollerRPMTarget", 0);
        SmartDashboard.putNumber("testBottomIntakeRollerRPMTarget", 0);
        return Commands.runEnd(() -> intakeRoller.setSpeeds(
            RPM.of(SmartDashboard.getNumber("testTopIntakeRollerRPMTarget", 0)),
            RPM.of(SmartDashboard.getNumber("testBottomIntakeRollerRPMTarget", 0))
        ), intakeRoller::stopIntakeRollers, intakeRoller);
    }

    public static Command testKicker(Kicker kicker) {
        SmartDashboard.putNumber("testKickerRPMTarget", 0);
        return Commands.runEnd(() -> kicker.setSpeed(RPM.of(SmartDashboard.getNumber("testKickerRPMTarget", 0))), kicker::stopKicker, kicker);
    }

    public static Command testTurret(Turret turret) {
        return new TestTurret(turret);
    }
}
