package frc.robot.tools;

public class SlewRateLimiter {
  
  private double previousValue = 0;
  private double maxChange;

  public SlewRateLimiter(double maxChange) {
    this.maxChange = Math.abs(maxChange);
  }

  public double getOutputValue(double newValue) {
    double output = previousValue;
    double change = Math.abs(newValue - previousValue);
    if (change > maxChange) {
      output += Math.copySign(maxChange, newValue - previousValue);
    } else {
      output += newValue - previousValue;
    }
    previousValue = output;
    return output;
  }
}
