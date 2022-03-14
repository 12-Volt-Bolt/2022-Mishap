// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  private final CANSparkMax frontWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.FRONT_WINCH, MotorType.kBrushless);
  private final CANSparkMax rearWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.REAR_WINCH, MotorType.kBrushless);
  private final Servo releaseServo = new Servo(9);

  private final DigitalInput frontStop = new DigitalInput(0);
  private final DigitalInput rearStop = new DigitalInput(9);

  private double frontHomeValue = 0;
  private double rearHomeValue = 0;

  private double frontPowerSet = 0;
  private double rearPowerSet = 0;

  public boolean getFrontStop() {
    return !frontStop.get();
  }

  public boolean getRearStop() {
    return !rearStop.get();
  }

  public Climber() {}

  @Override
  public void periodic() {
    if (frontStop.get() == false && frontPowerSet < 0) {
      frontWinch.set(0);
      // setFrontHome();
    } else {
      frontWinch.set(frontPowerSet);
    }

    if (rearStop.get() == false && rearPowerSet < 0) {
      rearWinch.set(-0.05);
    } else {
      rearWinch.set(rearPowerSet);
    }
    
    frontWinch.set(frontPowerSet);

    frontPowerSet = 0;
    rearPowerSet = 0;

    SmartDashboard.putNumber("Front position", getFrontPosition());
    SmartDashboard.putNumber("Rear position", getRearPosition());
  }

  public void setFrontIdle(IdleMode idleMode) {
    frontWinch.setIdleMode(idleMode);
  }

  public void setFrontHome() {
    frontHomeValue = frontWinch.getEncoder().getPosition();
  }

  public void setRearHome() {
    rearHomeValue = rearWinch.getEncoder().getPosition();
  }

  public double getFrontPosition() {
    return (frontWinch.getEncoder().getPosition() - frontHomeValue);
  }

  public double getRearPosition() {
    return rearWinch.getEncoder().getPosition() - rearHomeValue;
  }

  public void setClimberPowers(double frontPower, double rearPower) {

    frontPowerSet = -frontPower;
    rearPowerSet = rearPower;
  }

  public void setServo(double position) {
    releaseServo.set(position);
  }

  public void disableServo() {
    releaseServo.setDisabled();
  }
}
