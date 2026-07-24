package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Kilograms;
import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;

public interface IntakeConstants {
    DCMotor intakeRollerMotorPhysical = DCMotor.getKrakenX60(2);
    DCMotor bottomIntakeRollerMotorPhysical = DCMotor.getKrakenX60(1);

    SmartMotorControllerConfig topIntakeRollerMotorConfig =
    new SmartMotorControllerConfig()
        .withMotorInverted(false)
        .withIdleMode(MotorMode.COAST)
        .withGearing(1)
        .withClosedLoopController(0.0003, 0.0, 0.0)
        .withFeedforward(new SimpleMotorFeedforward(0.1, 0.002, 0.0))
        .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
        .withStatorCurrentLimit(Amps.of(80))
        .withTelemetry("IntakeRollerMotor", TelemetryVerbosity.HIGH);

    SmartMotorControllerConfig bottomIntakeRollerMotorConfig =
    new SmartMotorControllerConfig()
        .withMotorInverted(false)
        .withIdleMode(MotorMode.COAST)
        .withGearing(1)
        .withClosedLoopController(0.0003, 0.0, 0.0)
        .withFeedforward(new SimpleMotorFeedforward(0.1, 0.002, 0.0))
        .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
        .withStatorCurrentLimit(Amps.of(80))
        .withTelemetry("IntakeRollerMotor", TelemetryVerbosity.HIGH);

    int topIntakeRollerMotorCanID1 = 30;
    int topIntakeRollerMotorCanID2 = 31;

    int bottomIntakeRollerMotorCanID = 32;
}
