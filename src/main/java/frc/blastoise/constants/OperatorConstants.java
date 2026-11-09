package frc.blastoise.constants;

public class OperatorConstants {
  public final OperatorsControllerType DEFAUL_CONTROLLER = OperatorsControllerType.Xbox;

  public enum OperatorsControllerType {
    Xbox,
    PlayStation;
  }

  public static final OperatorsControllerType CONTROLLER_TYPE = OperatorsControllerType.PlayStation;
}
