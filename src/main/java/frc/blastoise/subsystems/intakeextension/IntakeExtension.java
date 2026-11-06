package frc.blastoise.subsystems.intakeextension;

import java.util.function.Function;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeExtension extends SubsystemBase {

  private final IntakeExtensionIO io;
  private final IntakeExtensionIOInputsAutoLogged inputs = new IntakeExtensionIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new.
   *                  The subsystem passes itself in, because the IO class's
   *                  SmartMotorControllerConfig needs the subsystem.
   */
  public IntakeExtension(Function<SubsystemBase, IntakeExtensionIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("Arm", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  /**
   * Command to move the arm to a target angle and hold it.
   * This command does not end on its own.
   */
//   public Command setAngle(Angle angle) {
//     return run(() -> io.setTargetAngle(angle))
//         .withName("Arm.setAngle(" + angle + ")");
//   }

  /**
   * Command to move the arm to a target angle and finish when reached.
   * Be careful with default commands, see the hint below.
   */
//   public Command goToAngle(Angle angle, Angle tolerance) {
//     return run(() -> io.setTargetAngle(angle))
//         .until(() -> isNear(angle, tolerance))
//         .withName("Arm.goToAngle(" + angle + ")");
//   }

  /** Returns true if the logged arm position is within tolerance of a target, using WPILib's isNear(). */
//   public boolean isNear(Angle target, Angle tolerance) {
//     return getAngle().isNear(target, tolerance);
//   }

  /** The logged arm position, so it replays correctly. */
//   public Angle getAngle() {
//     return Rotations.of(inputs.positionRotations);
//   }

  /** Command to stop the arm. */
//   public Command stop() {
//     return runOnce(() -> io.stop()).withName("Arm.stop");
//   }

}