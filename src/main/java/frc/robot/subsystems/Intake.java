package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.tools.PriorityHandler;

public class Intake extends SubsystemBase {
  public enum IntakePosition {
      Up (3300)
    , Down (280)
    , None (3300);

    int position;

    private IntakePosition(int position) {
      this.position = position;
    }

    public int GetPosition() {
      return position;
    }
  }

  private final CANSparkMax conveyorMotor = new CANSparkMax(frc.robot.constants.robotmap.motor.Intake.CONVEYOR, MotorType.kBrushless);
  private final TalonSRX armMotor = new TalonSRX(frc.robot.constants.robotmap.motor.Intake.ARTICULATION);

  private PIDController PID = new PIDController(.075, 0, 0);
  private IntakePosition currentPosition = IntakePosition.None;

  private PriorityHandler<Double> conveyorPriority = new PriorityHandler<Double>();

  public boolean doIntakeArticulation = true;
  
  public Intake() {
    armMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    PID.setTolerance(20);
  }

  public void restIntakeArticulation() {
    armMotor.setNeutralMode(NeutralMode.Coast);
  }

  /**
   * Sets the power of the conveyor. Max value is 1, min value is -1.
   * @param power Power of the conveyor.
   * @author Lucas Brunner
   */
  public void setConveyorPower(double power, double priority) {
    conveyorPriority.setRequest(priority, power);
  }
  
  /**
   * Returns the value of the intake's encoder.
   * @return The value of the intake's encoder.
   * @author Lucas Brunner
   */
  public double getIntakePos() {
    return armMotor.getSelectedSensorPosition();
  }

  /**
   * Sets the power of the arm's movement motor. Max value is 1, min value is -1.
   * @param power Power of the arm's movement motor.
   * @author Lucas Brunner
   */
  public void setArmPower(double power) {
    armMotor.set(ControlMode.PercentOutput, power);
  }

  /**
   * Sets goal position state of the intake.
   * @param goalPosition Goal intake position state.
   * @author Lucas Brunner
   */
  public void setGoalPosition(IntakePosition goalPosition, double priority) {
    currentPosition = goalPosition;
  }

  /**
   * Returns the current intake position state.
   * @author Lucas Brunner
   */
  public IntakePosition getGoalPosition() {
    return currentPosition;
  }

  private double calculateIntakeArticulationPower() {
    double moveSpeed = 0;
    switch (currentPosition) {
      case Up:
        armMotor.setNeutralMode(NeutralMode.Coast);
        if (getIntakePos() < IntakePosition.Up.GetPosition() - 500) {
          moveSpeed = 0.3;
        } else if (getIntakePos() < IntakePosition.Up.GetPosition() - 25) {
          moveSpeed = 0.1;
        }
        break;
      case Down:
        armMotor.setNeutralMode(NeutralMode.Coast);
        if (getIntakePos() > IntakePosition.Down.GetPosition() + 700) {
          moveSpeed = -0.3;
        } else if (getIntakePos() > IntakePosition.Down.GetPosition() + 100) {
          moveSpeed = -0.1;
        }
        break;
      default:
        armMotor.setNeutralMode(NeutralMode.Coast);
        break;
    }
    return moveSpeed;
  }

  @Override
  public void periodic() {
    if (doIntakeArticulation) {
      setArmPower(calculateIntakeArticulationPower());
    }

    if (currentPosition != IntakePosition.Down) {
      conveyorPriority.setRequest(Double.POSITIVE_INFINITY, 0.0);
    }

    double intakePower = 0;
    if (conveyorPriority.getHighestPriorityRequest() != null) {
      intakePower = conveyorPriority.getHighestPriorityRequest();
    }
    
    conveyorMotor.set(-intakePower);
    conveyorPriority.clearRequests();
  }
}
