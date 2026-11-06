package frc.robot.subsystems.drive;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.generated.TunerConstantsBlastoise1022;
import java.util.Queue;

/** IO implementation for Pigeon 2. */
public class GyroIOPigeon2 implements GyroIO {
  private final Pigeon2 pigeon =
      new Pigeon2(
          TunerConstantsBlastoise1022.DrivetrainConstants.Pigeon2Id,
          TunerConstantsBlastoise1022.kCANBus);
  private final StatusSignal<Angle> yaw = pigeon.getYaw();
  private final StatusSignal<Angle> pitch = pigeon.getPitch();
  private final StatusSignal<Angle> roll = pigeon.getRoll();

  private final Queue<Double> yawPositionQueue;
  private final Queue<Double> pitchPositionQueue;
  private final Queue<Double> rollPositionQueue;

  private final Queue<Double> yawTimestampQueue;
  private final Queue<Double> pitchTimestampQueue;
  private final Queue<Double> rollTimestampQueue;

  private final StatusSignal<AngularVelocity> yawVelocity = pigeon.getAngularVelocityZWorld();
  private final StatusSignal<AngularVelocity> pitchVelocity = pigeon.getAngularVelocityYWorld();
  private final StatusSignal<AngularVelocity> rollVelocity = pigeon.getAngularVelocityXWorld();

  public GyroIOPigeon2() {
    if (TunerConstantsBlastoise1022.DrivetrainConstants.Pigeon2Configs != null) {
      pigeon
          .getConfigurator()
          .apply(TunerConstantsBlastoise1022.DrivetrainConstants.Pigeon2Configs);
    } else {
      pigeon.getConfigurator().apply(new Pigeon2Configuration());
    }

    pigeon.getConfigurator().setYaw(0.0);

    yaw.setUpdateFrequency(Drivetrain.ODOMETRY_FREQUENCY);
    pitch.setUpdateFrequency(Drivetrain.ODOMETRY_FREQUENCY);
    roll.setUpdateFrequency(Drivetrain.ODOMETRY_FREQUENCY);

    yawVelocity.setUpdateFrequency(50.0);
    pitchVelocity.setUpdateFrequency(50.0);
    rollVelocity.setUpdateFrequency(50.0);

    pigeon.optimizeBusUtilization();

    yawTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
    pitchTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
    rollTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();

    yawPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(yaw.clone());
    pitchPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(pitch.clone());
    rollPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(roll.clone());
  }

  @Override
  public void updateInputs(GyroIOInputs inputs) {
    inputs.connected = BaseStatusSignal.refreshAll(yaw, yawVelocity).equals(StatusCode.OK);

    inputs.yawPosition = Rotation2d.fromDegrees(yaw.getValueAsDouble());
    inputs.yawPosition = Rotation2d.fromDegrees(yaw.getValueAsDouble());
    inputs.yawPosition = Rotation2d.fromDegrees(yaw.getValueAsDouble());

    inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());
    inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());
    inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());

    inputs.odometryYawTimestamps =
        yawTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryPitchTimestamps =
        pitchTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryRollTimestamps =
        rollTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();

    inputs.odometryYawPositions =
        yawPositionQueue.stream()
            .map((Double value) -> Rotation2d.fromDegrees(value))
            .toArray(Rotation2d[]::new);
    inputs.odometryPitchPositions =
        pitchPositionQueue.stream()
            .map((Double value) -> Rotation2d.fromDegrees(value))
            .toArray(Rotation2d[]::new);
    inputs.odometryRollPositions =
        rollPositionQueue.stream()
            .map((Double value) -> Rotation2d.fromDegrees(value))
            .toArray(Rotation2d[]::new);

    yawTimestampQueue.clear();
    pitchTimestampQueue.clear();
    rollTimestampQueue.clear();

    yawPositionQueue.clear();
    pitchPositionQueue.clear();
    rollPositionQueue.clear();
  }
}
