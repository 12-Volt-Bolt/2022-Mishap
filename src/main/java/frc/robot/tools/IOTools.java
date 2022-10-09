package frc.robot.tools;

public class IOTools {
  public static double deadzoneRemap(double value, double minIn, double maxIn, double deadzone, double maxOut) {
    value = Equations.clamp(value, minIn, maxIn);
    if (value < deadzone) {
      return 0;
    }
    maxIn -= minIn + deadzone;
    value -= minIn + deadzone;
    double percentage = value / maxIn;
    return percentage * maxOut;
  }

  public static double axisDeadzoneRemap(double value, double center, double maxIn, double deadzone, double maxOut) {
    double absoluteValue = Math.abs(value);
    double output = deadzoneRemap(absoluteValue, center, maxIn, deadzone, maxOut);
    return Math.copySign(output, value);
  }
}
