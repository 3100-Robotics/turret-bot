package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pound;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.telemetry.SmartMotorControllerTelemetryConfig;

public interface DyeRotorConstants {
  int kickerMotorCanID1 = 40;
  int kickerMotorCanID2 = 41;
  int hookMotorCanID = 42;

  DCMotor hookMotorPhysical = DCMotor.getNEO(1);

  SmartMotorControllerConfig hookMotorConfig =
      new SmartMotorControllerConfig()
          // Direction / Current stuff
          .withMotorInverted(false)
          .withStatorCurrentLimit(Amps.of(80))
          // Gearing
          .withGearing(new MechanismGearing(GearBox.fromReductionStages(80 / 16, 80 / 10)))
          // PID / FF
          .withClosedLoopController(new PIDController(5, 0, 0))
          .withFeedforward(new SimpleMotorFeedforward(0, 0, 0))
          // Sim PID / FF
          .withSimClosedLoopController(new PIDController(5, 0, 0))
          .withSimFeedforward(new SimpleMotorFeedforward(0, 0, 0))
          .withTelemetry("hookMotor", TelemetryVerbosity.LOW)
          // Sim props
          .withMomentOfInertia(Inches.of(14.724154), Pound.of(7.8858569));

  DCMotor kickerMotorPhysical = DCMotor.getNEO(2);

  SmartMotorControllerTelemetryConfig kickerMotorTelemetryConfig =
      new SmartMotorControllerTelemetryConfig().withTelemetryVerbosity(TelemetryVerbosity.LOW);

  SmartMotorControllerConfig kickerMotorConfig =
      new SmartMotorControllerConfig()
          // Direction / Current stuff
          .withMotorInverted(false)
          .withStatorCurrentLimit(Amps.of(80))
          // Gearing
          .withGearing(new MechanismGearing(GearBox.fromReductionStages(36 / 18, 32 / 16)))
          // PID / FF
          .withClosedLoopController(new PIDController(5, 0, 0))
          .withFeedforward(new SimpleMotorFeedforward(0, 0, 0))
          // Sim PID / FF
          .withSimClosedLoopController(new PIDController(5, 0, 0))
          .withSimFeedforward(new SimpleMotorFeedforward(0, 0, 0))
          .withTelemetry("kickerMotor", kickerMotorTelemetryConfig)
          // Sim props
          .withMomentOfInertia(Inches.of(1.724154), Pound.of(0.9));
}
