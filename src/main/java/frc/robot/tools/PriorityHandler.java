package frc.robot.tools;

public class PriorityHandler<T> {

  private Double currentPriority = 0d;
  private T currentValue = null;

  public void setRequest(double priority, T value) {
    if (currentPriority < priority) {
      currentPriority = priority;
      currentValue = value;
    }
  }

  public void clearRequests() {
    currentPriority = Double.NEGATIVE_INFINITY;
    currentValue = null;
  }

  /**
   * May return null.
   */
  public T getHighestPriorityRequest() {
    return currentValue;
  }
}
