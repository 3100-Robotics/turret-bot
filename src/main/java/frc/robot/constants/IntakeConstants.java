package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Kilograms;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Pounds;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.ElevatorConfig;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;

public interface IntakeConstants {
  int topRollerMotorLeftCanID1 = 32;
  int topRollerMotorRightCanID2 = 34;
  int bottomRollerMotorCanID = 33;
  int extensionMotorLeftCanID = 31;
  int extensionMotorRightCanID = 30;

  DCMotor topRollerMotorPhysical = DCMotor.getKrakenX60(2);
  DCMotor bottomRollerMotorPhysical = DCMotor.getKrakenX60(1);
  DCMotor extensionMotorPhysical = DCMotor.getKrakenX60(1);

  SmartMotorControllerConfig topRollerMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withGearing(new MechanismGearing(GearBox.fromReductionStages(36 / 11)))
          .withClosedLoopController(1, 0.0, 0.0)
          .withFeedforward(new SimpleMotorFeedforward(0.0, 0.00, 0.0))
          .withSimClosedLoopController(0.0, 0.0, 0.0)
          .withSimFeedforward(new SimpleMotorFeedforward(0.0, 0.10, 0.0))
          .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
          .withStatorCurrentLimit(Amps.of(80))
          .withTelemetry("topIntakeRollerMotor", TelemetryVerbosity.LOW);

  SmartMotorControllerConfig bottomRollerMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withGearing(new MechanismGearing(GearBox.fromReductionStages(18 / 11)))
          .withClosedLoopController(1, 0.0, 0.0)
          .withFeedforward(new SimpleMotorFeedforward(0.0, 0.00, 0.0))
          .withSimClosedLoopController(0.0, 0.0, 0.0)
          .withSimFeedforward(new SimpleMotorFeedforward(0.0, 0.10, 0.0))
          .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
          .withStatorCurrentLimit(Amps.of(80))
          .withTelemetry("bottomIntakeRollerMotor", TelemetryVerbosity.LOW);

  SmartMotorControllerConfig baseExtensionMotorConfig =
      new SmartMotorControllerConfig()
          .withMechanismCircumference(Inches.of(Math.PI))
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withIdleMode(MotorMode.BRAKE)
          .withStartingPosition(Meters.of(0))
          .withGearing(1)
          .withClosedLoopController(10, 0.0, 0.0)
          .withSimClosedLoopController(1, 0.0, 0.0)
          .withFeedforward(new SimpleMotorFeedforward(0.0, 0.0, 0.0))
          .withSimFeedforward(new SimpleMotorFeedforward(0.0, 0.0, 0.0))
          .withTrapezoidalProfile(MetersPerSecond.of(100), MetersPerSecondPerSecond.of(50))
          .withMomentOfInertia(Meters.of(0.04), Kilograms.of(0.09))
          .withStatorCurrentLimit(Amps.of(80));

  SmartMotorControllerConfig extensionMotorConfigLeft =
      baseExtensionMotorConfig
          .clone()
          .withMotorInverted(false)
          .withTelemetry("intakeExtensionMotorLeft", TelemetryVerbosity.LOW);

  SmartMotorControllerConfig extensionMotorConfigRight =
      baseExtensionMotorConfig
          .clone()
          .withMotorInverted(!extensionMotorConfigLeft.getMotorInverted().get())
          .withTelemetry("intakeExtensionMotorRight", TelemetryVerbosity.LOW);

  ElevatorConfig extensionMechanismConfig =
      new ElevatorConfig()
          .withHardLimits(Meters.of(0), Meters.of(2))
          .withTelemetry("intakeExtensionMechanism", TelemetryVerbosity.LOW)
          .withCarriageWeight(Pounds.of(10));
}
