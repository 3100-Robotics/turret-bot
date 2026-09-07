package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Pound;
import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Angle;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.PivotConfig;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;

public interface ShooterConstants {
  int hoodMotorCanID = 50;
  int leftFlywheelMotorCanID = 51;
  int rightFlywheelMotorCanID = 52;
  int turretMotorCanID = 53;

  Angle maxHoodAngle = Degrees.of(46.0);
  Angle minHoodAngle = Degrees.of(7.721437);

  Angle maxTurretAngle = Degrees.of(360+21.76);
  Angle minTurretAngle = Degrees.of(0);
  Angle safeMaxTurretAngle = maxTurretAngle.minus(Degrees.of(2));

  DCMotor hoodMotorPhysical = DCMotor.getKrakenX60(1);

  SmartMotorControllerConfig hoodMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          // Feedback Constants (PID Constants)
          .withClosedLoopController(50, 0, 0)
          .withSimClosedLoopController(50, 0, 0)
          .withTrapezoidalProfile(DegreesPerSecond.of(90), DegreesPerSecondPerSecond.of(45))
          .withStartingPosition(minHoodAngle)
          // Feedforward Constants
          .withFeedforward(new ArmFeedforward(0, 0, 0))
          .withSimFeedforward(new ArmFeedforward(0, 0, 0))
          // Telemetry name and verbosity level
          .withTelemetry("hoodMotor", TelemetryVerbosity.LOW)
          // Gearing from the motor rotor to final shaft.
          // In this example GearBox.fromReductionStages(3,4) is the same as
          // GearBox.fromStages("3:1","4:1") which corresponds to the gearbox attached to your
          // motor.
          .withGearing(1)
          // Motor properties to prevent over currenting.
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withStatorCurrentLimit(Amps.of(40))
          .withClosedLoopRampRate(Seconds.of(0.25))
          .withOpenLoopRampRate(Seconds.of(0.25))
          // MOI
          .withMomentOfInertia(KilogramSquareMeters.of(0.0190245794));

  DCMotor flywheelMotorPhysical = DCMotor.getKrakenX60(2);

  SmartMotorControllerConfig flywheelMotorConfig =
      new SmartMotorControllerConfig()
          .withStatorCurrentLimit(Amps.of(60))
          .withIdleMode(MotorMode.COAST)
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withGearing(1)
          .withMomentOfInertia(Inches.of(1.9825395), Pound.of(0.9))

          .withMotorInverted(true)
          // PID / FF
          .withClosedLoopController(new PIDController(0.003, 0, 0))
          .withFeedforward(new SimpleMotorFeedforward(0, 0.137))
          // Sim PID / FF
          .withSimClosedLoopController(new PIDController(0.003, 0, 0))
          .withSimFeedforward(new SimpleMotorFeedforward(0, 0.137))
          // Telemetry
          .withTelemetry("flywheelMotor", TelemetryVerbosity.LOW);

  DCMotor turretMotorPhysical = DCMotor.getKrakenX60(1);

  SmartMotorControllerConfig turretMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          // Feedback Constants (PID Constants)
          .withClosedLoopController(50, 0, 0)
          .withSimClosedLoopController(50, 0, 0)
          .withTrapezoidalProfile(DegreesPerSecond.of(90), DegreesPerSecondPerSecond.of(45))
          .withStartingPosition(minHoodAngle)
          // Feedforward Constants
          .withFeedforward(new ArmFeedforward(0, 0, 0))
          .withSimFeedforward(new ArmFeedforward(0, 0, 0))
          // Telemetry name and verbosity level
          .withTelemetry("turretMotor", TelemetryVerbosity.HIGH)
          // Gearing from the motor rotor to final shaft.
          // In this example GearBox.fromReductionStages(3,4) is the same as
          // GearBox.fromStages("3:1","4:1") which corresponds to the gearbox attached to your
          // motor.
          .withGearing(new MechanismGearing(GearBox.fromStages("48:16", "175:10")))
          // Motor properties to prevent over currenting.
          .withMotorInverted(false)
          .withIdleMode(MotorMode.COAST)
          .withStatorCurrentLimit(Amps.of(40))
          .withClosedLoopRampRate(Seconds.of(0.25))
          .withOpenLoopRampRate(Seconds.of(0.25))
          // MOI
          .withMomentOfInertia(KilogramSquareMeters.of(0.0190245794))
          .withSoftLimits(minTurretAngle, safeMaxTurretAngle);

  PivotConfig turretConfig = new PivotConfig()
    .withHardLimits(minTurretAngle, maxTurretAngle)
    .withTelemetry("turretMech", TelemetryVerbosity.HIGH);
}
