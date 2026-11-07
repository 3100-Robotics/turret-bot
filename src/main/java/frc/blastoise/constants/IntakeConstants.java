package frc.blastoise.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Kilograms;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Pounds;

import edu.wpi.first.math.controller.ElevatorFeedforward;
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
          .withTelemetry("topIntakeRollerMotor", TelemetryVerbosity.HIGH);

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
          .withTelemetry("bottomIntakeRollerMotor", TelemetryVerbosity.HIGH);

  SmartMotorControllerConfig baseExtensionMotorConfig =
      new SmartMotorControllerConfig()
          .withMechanismCircumference(Inches.of(1).times(Math.PI))
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withIdleMode(MotorMode.BRAKE)
          .withStartingPosition(Meters.of(0))
          .withGearing(1)
          .withClosedLoopController(10, 0.0, 0.0)
          .withSimClosedLoopController(1, 0.0, 0.0)
          .withFeedforward(new ElevatorFeedforward(0.0, 0.0, 0.0))
          .withSimFeedforward(new ElevatorFeedforward(0.0, 0.0, 0.0))
          .withMomentOfInertia(Meters.of(0.04), Kilograms.of(0.09))
          .withStatorCurrentLimit(Amps.of(80));

  SmartMotorControllerConfig rightExtensionMotorConfig =
      baseExtensionMotorConfig
          .clone()
          .withMotorInverted(false)
          .withTelemetry("rightIntakeExtensionMotor", TelemetryVerbosity.HIGH);

  SmartMotorControllerConfig leftExtensionMotorConfig =
      baseExtensionMotorConfig
          .clone()
          .withMotorInverted(!rightExtensionMotorConfig.getMotorInverted().get())
          .withTelemetry("leftIntakeExtensionMotor", TelemetryVerbosity.HIGH);

  // Extension mechanism configs
  ElevatorConfig baseExtensionMechanismConfig =
      new ElevatorConfig()
          .withHardLimits(Meters.of(0), Meters.of(2))
          .withCarriageWeight(Pounds.of(10));

  ElevatorConfig rightExtensionMechanismConfig =
      baseExtensionMechanismConfig
          .clone()
          .withTelemetry("rightIntakeExtensionMechanism", TelemetryVerbosity.HIGH);

  ElevatorConfig leftExtensionMechanismConfig =
      baseExtensionMechanismConfig
          .clone()
          .withTelemetry("leftIntakeExtensionMechanism", TelemetryVerbosity.HIGH);
}
