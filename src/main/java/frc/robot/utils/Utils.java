package frc.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;

public class Utils {
  public static void warnIfDriverStationOutOfTest(String message) {
    String callerMethod =
        StackWalker.getInstance()
            .walk(
                stream ->
                    stream
                        .skip(1)
                        .findFirst()
                        .map(StackWalker.StackFrame::getMethodName)
                        .orElse("Unknown"));

    if (!DriverStation.isTest()) {
      DriverStation.reportWarning(
          "Test method called out of test mode! Method: " + callerMethod + " Message:" + message,
          false);
    }
  }

  public static void warnIfDriverStationOutOfTest() {
    String callerMethod =
        StackWalker.getInstance()
            .walk(
                stream ->
                    stream
                        .skip(1)
                        .findFirst()
                        .map(StackWalker.StackFrame::getMethodName)
                        .orElse("Unknown"));

    if (!DriverStation.isTest()) {
      DriverStation.reportWarning(
          "Test method called out of test mode! Method: " + callerMethod, false);
    }
  }
}
