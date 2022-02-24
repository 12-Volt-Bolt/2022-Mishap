// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  
  private final CANSparkMax rightMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax rightMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);
  private final CANSparkMax leftMotor2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Drivetrain.RIGHT1, MotorType.kBrushless);

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
   * Sets the power of the drivetrain's motors to 0.
   * @author Lucas Brunner
   */
  public void stop() {
    setMotorPowers(0, 0);
  }
  
  /**
   * Arcade-style drivetrain input. Max values are 1, min values are -1.
   * @param powerZ The robot's power forward and backward. Positive is forward.
   * @param powerY The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDrive(double powerZ, double powerY) {
    double rightOutput = -powerZ;
    double leftOutput = -powerZ;

    rightOutput += powerY;
    leftOutput -= powerY;
    
    setMotorPowers(leftOutput, rightOutput);
  }

  /**
   * Arcade-style drivetrain input. Max values are 1, min values are -1.
   * Math is applied to limit how often the motors run at 100%.
   * @param powerZ The robot's power forward and backward. Positive is forward.
   * @param powerY The robot's rotation around the vertical axis. Positive is clockwise.
   * @author Lucas Brunner
   */
  public void arcadeDriveTurnThrottle(double powerZ, double powerY) {
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
    
    setMotorPowers(leftOutput, rightOutput);
  }

  /**
   * Tank-style drivetrain input. Positive is forward. Max values are 1, min values are -1.
   * @param left power of the left wheels.
   * @param right power of the right wheels.
   * @author Lucas Brunner
   */
  public void tankDrive(double left, double right) {
    setMotorPowers(left, right);
  }
}
