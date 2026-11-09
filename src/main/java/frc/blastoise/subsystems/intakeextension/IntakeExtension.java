package frc.blastoise.subsystems.intakeextension;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.Function;
import org.littletonrobotics.junction.Logger;

public class IntakeExtension extends SubsystemBase {

  public final IntakeExtensionIO io;
  private final IntakeExtensionIOInputsAutoLogged inputs = new IntakeExtensionIOInputsAutoLogged();

  /**
   * @param ioFactory Builds the IO class for this subsystem, for example ArmIOTalonFX::new. The
   *     subsystem passes itself in, because the IO class's SmartMotorControllerConfig needs the
   *     subsystem.
   */
  public IntakeExtension(Function<SubsystemBase, IntakeExtensionIO> ioFactory) {
    this.io = ioFactory.apply(this);
  }

  public IntakeExtension(IntakeExtensionIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // Update the arm's telemetry
    io.updateTelemetry();
    // Update and log inputs every cycle
    io.updateInputs(inputs);
    Logger.processInputs("IntakeExtension", inputs);
  }

  @Override
  public void simulationPeriodic() {
    io.simIterate();
  }

  public Command setDistance(Distance distance) {
    return runEnd(() -> io.setDistance(distance), io::stop);
  }
}
