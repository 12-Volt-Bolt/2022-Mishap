package frc.robot.tools;

public class RollingAverage
{
  private double[] rollingValues;
  private int currentValue = 0;

  public boolean resetWhenApproachingZero = true;
  public int speedReductionAmount = 1;

  public RollingAverage(int sampleAmount)
  {
    rollingValues = new double[sampleAmount];
    speedReductionAmount = sampleAmount;
  }

  public void addSample(double sample)
  {
    rollNewSample(sample);
  }

  private void rollNewSample(double sample) {
    if (Equations.closestToZero(sample, getAverage()) && resetWhenApproachingZero) {
      for (int i = 0; i < speedReductionAmount; i++) {
        rollingValues[currentValue] = sample;
        currentValue += 1;
        currentValue = Equations.wrap(currentValue, 0, rollingValues.length - 1);
      }
    } else {
      rollingValues[currentValue] = sample;
      currentValue += 1;
      currentValue = Equations.wrap(currentValue, 0, rollingValues.length - 1);
    }
  }

  public void fillWithValue(double value) {
    for (int i = 0; i < rollingValues.length; i++) {
      rollingValues[i] = value;
    }
  }

  public double getAverage() {
    return Equations.arraySum(rollingValues) / rollingValues.length;
  }
}