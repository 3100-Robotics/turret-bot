package frc.robot.constants;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class OperatorConstants {
    public enum OperatorsControllerType {
        Xbox, PlayStation;
    }

    public static final OperatorsControllerType CONTROLLER_TYPE = OperatorsControllerType.PlayStation;
}
