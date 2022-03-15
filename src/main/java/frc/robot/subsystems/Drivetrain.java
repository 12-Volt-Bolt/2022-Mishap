// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.tools.Equations;
import frc.robot.tools.PriorityHandler;
import frc.robot.tools.RollingAverage;
import frc.robot.tools.SlewRateLimiter;

public class Drivetrain extends SubsystemBase {
  
  private final CANSparkMax rightMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax rightMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT2, MotorType.kBrushless);
  private final CANSparkMax leftMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.LEFT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.LEFT2, MotorType.kBrushless);

  private final PriorityHandler<DrivePower> priorityHandler = new PriorityHandler<DrivePower>();

  

  private SlewRateLimiter ySlewRateLimiter = new SlewRateLimiter(0.04);
  private SlewRateLimiter zSlewRateLimiter = new SlewRateLimiter(0.04);

  @Override
  public void periodic() {
    DrivePower request = priorityHandler.getHighestPriorityRequest();

    if (request == null) {
      request = new DrivePower(0, 0);
    }

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
   * @param powerY The robot's power forward and backward. Positive is forward.
   * @param powerZ The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDrive(double powerY, double powerZ, double priority) {

    powerZ *= 0.5;

    double rightOutput = powerY + powerZ;
    double leftOutput = powerY - powerZ;
    
    priorityHandler.setRequest(priority, new DrivePower(rightOutput, leftOutput));
  }

  public void arcadeDriveTurnRollover(double powerY, double powerZ, double priority) {

    powerZ *= 0.5;

    double[] normalizedPower = Equations.normalize(new double[] { powerY, powerZ });

    powerY = normalizedPower[0];
    powerZ = normalizedPower[1];
    
    powerY = ySlewRateLimiter.getOutputValue(powerY);
    powerZ = zSlewRateLimiter.getOutputValue(powerZ);

    double rightOutput = powerY + powerZ;
    double leftOutput = powerY - powerZ;

    if (Math.abs(rightOutput) > 1) {
      leftOutput -= Equations.floatModulo(rightOutput, 1);
    } else if (Math.abs(leftOutput) > 1) {
      rightOutput -= Equations.floatModulo(leftOutput, 1);
    }
    
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
    
    powerY = ySlewRateLimiter.getOutputValue(powerY);
    powerZ = zSlewRateLimiter.getOutputValue(powerZ);

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