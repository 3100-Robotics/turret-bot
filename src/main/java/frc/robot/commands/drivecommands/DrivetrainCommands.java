package frc.robot.commands.drivecommands;

public class DrivetrainCommands {
  // public static Command goToPose(Drivetrain drivetrain) {
  //   return Commands.parallel(Commands.run(() -> drivetrain.goToPose(), drivetrain))
  //       .finallyDo(() -> drivetrain.setControl(new SwerveRequest.Idle()));
  // }

  // public static Command goToPose(Supplier<Pose2d> newPose, Drivetrain drivetrain) {
  //   return Commands.parallel(
  //       Commands.runOnce(() -> drivetrain.poseSetpoint = newPose), goToPose(drivetrain));
  // }

  // public static Command goToPose(Pose2d newPose, Drivetrain drivetrain) {
  //   return Commands.parallel(
  //       Commands.runOnce(() -> drivetrain.poseSetpoint = () -> newPose), goToPose(drivetrain));
  // }
}
