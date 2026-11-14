package frc.blastoise.subsystems.hood;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class Hood extends SubsystemBase {

  public final HoodIO io;
  private final HoodIOInputsAutoLogged inputs = new HoodIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new. The
   *     subsystem passes itself in, because the IO class's SmartMotorControllerConfig needs the
   *     subsystem.
   */
  public Hood(Function<SubsystemBase, HoodIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  public Hood(HoodIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("Hood", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  public Command setAngle(Angle angle) {
    return runEnd(() -> io.setAngle(angle), io::stop);
  }
}
