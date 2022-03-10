// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.tools.PriorityHandler;
import frc.robot.tools.RollingAverage;

public class Drivetrain extends SubsystemBase {
  
  private final CANSparkMax rightMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax rightMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT2, MotorType.kBrushless);
  private final CANSparkMax leftMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.LEFT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.LEFT2, MotorType.kBrushless);

  private final PriorityHandler<DrivePower> priorityHandler = new PriorityHandler<DrivePower>();
  private final RollingAverage rightRollingAverage = new RollingAverage(50);
  private final RollingAverage leftRollingAverage = new RollingAverage(50);
  private final RollingAverage yRollingAverage = new RollingAverage(50);
  private final RollingAverage zRollingAverage = new RollingAverage(50);

  public Drivetrain() {
    rightRollingAverage.resetWhenApproachingZero = true;
    leftRollingAverage.resetWhenApproachingZero = true;

    rightRollingAverage.speedReductionAmount = 10;
    leftRollingAverage.speedReductionAmount = 10;
    
    yRollingAverage.resetWhenApproachingZero = true;
    zRollingAverage.resetWhenApproachingZero = true;

    yRollingAverage.speedReductionAmount = 10;
    zRollingAverage.speedReductionAmount = 10;
  }

  @Override
  public void periodic() {
    DrivePower request = priorityHandler.getHighestPriorityRequest();

    if (request == null) {
      request = new DrivePower(0, 0);
    }

    rightRollingAverage.addSample(request.right);
    leftRollingAverage.addSample(request.left);

    //setMotorPowers(rightRollingAverage.getAverage(), leftRollingAverage.getAverage());
    setMotorPowers(request.right, request.left);

    priorityHandler.clearRequests();
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
  public void arcadeDrive(double powerZ, double powerY, double priority) {
    double rightOutput = -powerZ;
    double leftOutput = -powerZ;

    rightOutput += powerY;
    leftOutput -= powerY;
    
    priorityHandler.setRequest(priority, new DrivePower(rightOutput, leftOutput));
  }

  /**
   * Arcade-style drivetrain input. Max values are 1, min values are -1.
   * Math is applied to limit how often the motors run at 100%.
   * @param powerY The robot's power forward and backward. Positive is forward.
   * @param powerZ The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDriveTurnThrottle(double powerY, double powerZ, double priority) {
    yRollingAverage.addSample(powerY);
    zRollingAverage.addSample(powerZ);

    powerY = yRollingAverage.getAverage();
    powerZ = zRollingAverage.getAverage();

    double rightOutput = 0;
    double leftOutput = 0;

    powerZ = -powerZ;
    
    double maxInput = Math.copySign(Math.max(Math.abs(powerY), Math.abs(powerZ)), powerY);
    
    if (powerY >= 0.0) {
      // First quadrant, else second quadrant
      if (powerZ >= 0.0) {
        leftOutput = maxInput;
        rightOutput = powerY - powerZ;
      } else {
        leftOutput = powerY + powerZ;
        rightOutput = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (powerZ >= 0.0) {
        leftOutput = powerY + powerZ;
        rightOutput = maxInput;
      } else {
        leftOutput = maxInput;
        rightOutput = powerY - powerZ;
      }
    }
    
    priorityHandler.setRequest(priority, new DrivePower(rightOutput, leftOutput));
  }

  /**
   * Tank-style drivetrain input. Positive is forward. Max values are 1, min values are -1.
   * @param left power of the left wheels.
   * @param right power of the right wheels.
   * @author Lucas Brunner
   */
  public void tankDrive(double right, double left, double priority) {
    priorityHandler.setRequest(priority, new DrivePower(right, left));
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