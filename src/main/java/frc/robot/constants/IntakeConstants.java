package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Kilograms;
import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;

public interface IntakeConstants {
  int topRollerMotorCanID1 = 30;
  int topRollerMotorCanID2 = 31;
  int bottomRollerMotorCanID = 32;
  int extensionMotorLeftCanID = 33;
  int extensionMotorRightCanID = 34;

  DCMotor topRollerMotorPhysical = DCMotor.getKrakenX60(2);
  DCMotor bottomRollerMotorPhysical = DCMotor.getKrakenX60(1);
  DCMotor extensionMotorPhysical = DCMotor.getKrakenX60(2);

  SmartMotorControllerConfig topRollerMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withGearing(1)
          .withClosedLoopController(0.0003, 0.0, 0.0)
          .withFeedforward(new SimpleMotorFeedforward(0.1, 0.002, 0.0))
          .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
          .withStatorCurrentLimit(Amps.of(80))
          .withTelemetry("topIntakeRollerMotor", TelemetryVerbosity.HIGH);
  
  SmartMotorControllerConfig bottomRollerMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withGearing(1)
          .withClosedLoopController(0.0003, 0.0, 0.0)
          .withFeedforward(new SimpleMotorFeedforward(0.1, 0.002, 0.0))
          .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
          .withStatorCurrentLimit(Amps.of(80))
          .withTelemetry("bottomIntakeRollerMotor", TelemetryVerbosity.HIGH);

  SmartMotorControllerConfig extensionMotorConfig =
      new SmartMotorControllerConfig()
          .withControlMode(ControlMode.CLOSED_LOOP)
          .withMotorInverted(false)
          .withIdleMode(MotorMode.BRAKE)
          .withGearing(1)
          .withClosedLoopController(50, 0.0, 0.0)
          .withSimClosedLoopController(50, 0.0, 0.0)
          .withFeedforward(new ElevatorFeedforward(0.0, 0.0, 0.0))
          .withSimFeedforward(new ElevatorFeedforward(0.0, 0.0, 0.0))
          .withMomentOfInertia(Meters.of(0.0508), Kilograms.of(0.18))
          .withStatorCurrentLimit(Amps.of(80))
          .withTelemetry("intakeExtensionMotor", TelemetryVerbosity.HIGH);
}
