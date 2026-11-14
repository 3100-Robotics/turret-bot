package frc.blastoise.subsystems.intakerollers;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class IntakeRollers extends SubsystemBase {

  public final IntakeRollersIO io;
  private final IntakeRollersIOInputsAutoLogged inputs = new IntakeRollersIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new. The
   *     subsystem passes itself in, because the IO class's SmartMotorControllerConfig needs the
   *     subsystem.
   */
  public IntakeRollers(Function<SubsystemBase, IntakeRollersIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  public IntakeRollers(IntakeRollersIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("IntakeRollers", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  public Command setSpeed(AngularVelocity speed) {
    return runEnd(() -> io.setSpeed(speed), io::stop);
  }
}
