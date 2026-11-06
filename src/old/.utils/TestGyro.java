package frc.robot.utils;

import com.sbdc.loggerhead.logging.LogMode;
import com.sbdc.loggerhead.logging.OneShot;
import com.sbdc.loggerhead.logging.Table;
import com.sbdc.loggerhead.logging.compoundlogger.CompoundLogger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TestGyro implements CompoundLogger {
  public TestGyro() {
    SmartDashboard.putNumber("test15", 0);
  }

  public LogMode getLogMode() {
    return LogMode.NetworkOnly;
  }

  public void initialize(Table parentTable) {
    OneShot.setBoolean(parentTable.path + ".controllable", true);
    OneShot.setString(parentTable.path + ".name", "Robot Gyro");
    OneShot.setString(parentTable.path + ".type", "Gyro");

    parentTable.addDoubleLogger("Value", getLogMode(), () -> SmartDashboard.getNumber("test15", 0));
  }
}
