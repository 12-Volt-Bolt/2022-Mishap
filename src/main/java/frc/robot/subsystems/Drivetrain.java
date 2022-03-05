// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.tools.PriorityHandler;

public class Drivetrain extends SubsystemBase {
  
  private final CANSparkMax rightMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax rightMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);

  private final PriorityHandler<DrivePower> priorityHandler = new PriorityHandler<DrivePower>();

  @Override
  public void periodic() {
    DrivePower request = priorityHandler.getHighestPriorityRequest();

    if (request == null) {
      request = new DrivePower(0, 0);
    }

    setMotorPowers(request.right, request.left);

    priorityHandler.clearRequests();
  }

  public void register(Object requester, int priority) {
    priorityHandler.register(requester, priority);
  }

  /**
   * Sets the power of the drivetrain's motors. Max values are 1, min values are -1.
   * @param right power of the motors on the right side.
   * @param left power of the motors on the left side.
   * @author Lucas Brunner
   */
  public void setMotorPowers(double right, double left) {
    rightMotor1.set(right);
    rightMotor2.set(right);
    leftMotor1.set(-left);
    leftMotor2.set(-left);
  }
  
  /**
   * Arcade-style drivetrain input. Max values are 1, min values are -1.
   * @param powerZ The robot's power forward and backward. Positive is forward.
   * @param powerY The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDrive(double powerZ, double powerY, Object requester) {
    double rightOutput = -powerZ;
    double leftOutput = -powerZ;

    rightOutput += powerY;
    leftOutput -= powerY;
    
    priorityHandler.setRequest(requester, new DrivePower(rightOutput, leftOutput));
  }

  /**
   * Arcade-style drivetrain input. Max values are 1, min values are -1.
   * Math is applied to limit how often the motors run at 100%.
   * @param powerZ The robot's power forward and backward. Positive is forward.
   * @param powerY The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDriveTurnThrottle(double powerZ, double powerY, Object requester) {
    double rightOutput = 0;
    double leftOutput = 0;
    
    double maxInput = Math.copySign(Math.max(Math.abs(powerZ), Math.abs(powerY)), powerZ);
    
    if (powerZ >= 0.0) {
      // First quadrant, else second quadrant
      if (powerY >= 0.0) {
        leftOutput = maxInput;
        rightOutput = powerZ - powerY;
      } else {
        leftOutput = powerZ + powerY;
        rightOutput = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (powerY >= 0.0) {
        leftOutput = powerZ + powerY;
        rightOutput = maxInput;
      } else {
        leftOutput = maxInput;
        rightOutput = powerZ - powerY;
      }
    }
    
    priorityHandler.setRequest(requester, new DrivePower(rightOutput, leftOutput));
  }

  /**
   * Tank-style drivetrain input. Positive is forward. Max values are 1, min values are -1.
   * @param left power of the left wheels.
   * @param right power of the right wheels.
   * @author Lucas Brunner
   */
  public void tankDrive(double right, double left, Object requester) {
    priorityHandler.setRequest(requester, new DrivePower(right, left));
  }
}

class DrivePower {
  public double right;
  public double left;

  public DrivePower(double rightPower, double leftPower) {
    right = rightPower;
    left = leftPower;
  }
}