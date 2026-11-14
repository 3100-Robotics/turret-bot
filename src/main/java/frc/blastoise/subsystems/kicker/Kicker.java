package frc.blastoise.subsystems.kicker;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class Kicker extends SubsystemBase {

  public final KickerIO io;
  private final KickerIOInputsAutoLogged inputs = new KickerIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new. The
   *     subsystem passes itself in, because the IO class's SmartMotorControllerConfig needs the
   *     subsystem.
   */
  public Kicker(Function<SubsystemBase, KickerIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  public Kicker(KickerIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("Kicker", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  public Command setSpeed(AngularVelocity speed) {
    return runEnd(() -> io.setSpeed(speed), io::stop);
  }
}
