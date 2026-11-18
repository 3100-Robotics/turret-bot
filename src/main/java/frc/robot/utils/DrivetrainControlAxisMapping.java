package frc.robot.utils;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class DrivetrainControlAxisMapping {
    public final Supplier<Double> xSpeed;
    public final Supplier<Double> ySpeed;
    public final Supplier<Double> tSpeed;
    
    public DrivetrainControlAxisMapping(Supplier<Double> xSpeed, Supplier<Double> ySpeed, Supplier<Double> tSpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.tSpeed = tSpeed;
    }
}
