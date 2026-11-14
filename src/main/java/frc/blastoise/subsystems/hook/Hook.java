package frc.blastoise.subsystems.hook;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class Hook extends SubsystemBase {

  public final HookIO io;
  private final HookIOInputsAutoLogged inputs = new HookIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new. The
   *     subsystem passes itself in, because the IO class's SmartMotorControllerConfig needs the
   *     subsystem.
   */
  public Hook(Function<SubsystemBase, HookIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  public Hook(HookIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("Hook", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  public Command setSpeed(AngularVelocity speed) {
    return runEnd(() -> io.setSpeed(speed), io::stop);
  }
}
