package frc.robot.tools;

import java.util.HashMap;
import java.util.Set;

public class PriorityHandler<T> {

  private final HashMap<Double, T> priorityRequests = new HashMap<Double, T>();

  public void setRequest(double priority, T value) {
    priorityRequests.put(priority, value);
  }

  public void clearRequests() {
    priorityRequests.clear();
  }

  public T getHighestPriorityRequest() {
    Set<Double> priorities = priorityRequests.keySet();
    if (priorities.size() <= 0) {
      return null;
    } else {
      double highestPriority = Double.NEGATIVE_INFINITY;
      for (Double d : priorities) {
        if (d > highestPriority) {
          highestPriority = d;
        }
      }
      return priorityRequests.get(highestPriority);
    }
  }
}
