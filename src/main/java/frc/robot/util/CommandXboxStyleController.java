package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.OperatorConstants.OperatorsControllerType;

public class CommandXboxStyleController {
    private final CommandGenericHID internal;
    private final GenericHID internalHID;
    private final OperatorsControllerType type;
    private final int port;
    private final boolean isXbox;

    public CommandXboxStyleController(int port, OperatorsControllerType type) {
        this.port = port;
        this.type = type;
        this.isXbox = type == OperatorsControllerType.Xbox;
        this.internal = isXbox ? new CommandXboxController(port) : new CommandPS4Controller(port);
        this.internalHID = internal.getHID();
    }

    // Right thumb buttons

    public Trigger north() {
        return internal.button(isXbox ? XboxController.Button.kY.value : PS4Controller.Button.kTriangle.value);
    }

    public Trigger south() {
        return internal.button(isXbox ? XboxController.Button.kA.value : PS4Controller.Button.kCross.value);
    }

    public Trigger east() {
        return internal.button(isXbox ? XboxController.Button.kB.value : PS4Controller.Button.kCircle.value);
    }

    public Trigger west() {
        return internal.button(isXbox ? XboxController.Button.kX.value : PS4Controller.Button.kSquare.value);
    }

    // DPad

    public Trigger dpadCenter() {
        return internal.povCenter();
    }

    public Trigger dpadDown() {
        return internal.povDown();
    }

    public Trigger dpadDownLeft() {
        return internal.povDownLeft();
    }

    public Trigger dpadDownRight() {
        return internal.povDownRight();
    }

    public Trigger dpadLeft() {
        return internal.povLeft();
    }

    public Trigger dpadRight() {
        return internal.povRight();
    }

    public Trigger dpadUp() {
        return internal.povUp();
    }

    public Trigger dpadUpLeft() {
        return internal.povUpLeft();
    }

    public Trigger dpadUpRight() {
        return internal.povUpRight();
    }

    // Sticks

    public double leftStickX() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kLeftX.value : PS4Controller.Axis.kLeftX.value);
    }

    public double leftStickY() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kLeftY.value : PS4Controller.Axis.kLeftY.value);
    }

    public double rightStickX() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kRightX.value : PS4Controller.Axis.kRightX.value);
    }

    public double rightStickY() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kRightY.value : PS4Controller.Axis.kRightY.value);
    }

    // Trigger axies

    public double rightTriggerAxis() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kRightTrigger.value : PS4Controller.Axis.kR2.value);
    }

    public double leftTriggerAxis() {
        return internal.getRawAxis(isXbox ? XboxController.Axis.kLeftTrigger.value : PS4Controller.Axis.kL2.value);
    }

    // Bumpers
    public Trigger rightBumper() {
        return internal.button(isXbox ? XboxController.Button.kRightBumper.value : PS4Controller.Button.kR1.value);
    }

    public Trigger leftBumper() {
        return internal.button(isXbox ? XboxController.Button.kLeftBumper.value : PS4Controller.Button.kL1.value);
    }

    // Trigger, triggers
    public Trigger rightTrigger(double threshold) {
        return internal.axisGreaterThan(isXbox ? XboxController.Axis.kRightTrigger.value : PS4Controller.Axis.kR2.value, threshold);
    }

    public Trigger rightTrigger() {
        return rightTrigger(0.5);
    }

    public Trigger leftTrigger(double threshold) {
        return internal.axisGreaterThan(isXbox ? XboxController.Axis.kLeftTrigger.value : PS4Controller.Axis.kL2.value, threshold);
    }

    public Trigger leftTrigger() {
        return leftTrigger(0.5);
    }
}
