// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.values.ClimberPointsArmEncoded;
import frc.robot.tools.Equations;

public class Climber extends SubsystemBase {

  public final CANSparkMax frontWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.FRONT_WINCH, MotorType.kBrushless);
  public final CANSparkMax rearWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.REAR_WINCH, MotorType.kBrushless);
  private final Servo releaseServo = new Servo(9);

  private final DigitalInput frontStop = new DigitalInput(0);
  private final DigitalInput rearStop = new DigitalInput(9);

  private final DutyCycleEncoder armEncoder = new DutyCycleEncoder(8);

  private double frontHomeValue = 0;
  private double rearHomeValue = 0;

  private double frontPowerSet = 0;
  private double rearPowerSet = 0;

  private boolean showDiagnostics = false;

  public void setDiagnostics(boolean state) {
    if (state == false) {
      SmartDashboard.delete("Front position");
      SmartDashboard.delete("Rear position");
      SmartDashboard.delete("Front velocity");
      SmartDashboard.delete("Rear velocity");
    }
    showDiagnostics = state;
  }

  public boolean getFrontStop() {
    return !frontStop.get();
  }

  public boolean getRearStop() {
    return !rearStop.get();
  }

  public double getArmEncoder() {
    return armEncoder.get();
  }

  public double getArmPercentage() {
    double bottomPercentage = getBottomPercentage();

    double minValue = ClimberPointsArmEncoded.FRONT_MAX_UP_REAR_DOWN - ClimberPointsArmEncoded.FRONT_MAX_UP_REAR_UP;
    minValue = ClimberPointsArmEncoded.FRONT_MAX_UP_REAR_UP + (minValue * bottomPercentage);

    double maxValue = ClimberPointsArmEncoded.FRONT_MAX_DOWN_REAR_DOWN - ClimberPointsArmEncoded.FRONT_MAX_DOWN_REAR_UP;
    maxValue = ClimberPointsArmEncoded.FRONT_MAX_DOWN_REAR_UP + (maxValue * bottomPercentage);

    double armRange = maxValue - minValue;
    double realPercentage = (getArmEncoder() - minValue) / armRange;

    return  1 - realPercentage;
  }

  public double getFrontPercentage() {
    return getArmPercentage();
  }

  public double getBottomPercentage() {
    double maxBottom = ClimberPointsArmEncoded.REAR_MAX_EXTEND - ClimberPointsArmEncoded.REAR_MIN_EXTEND;
    double currentBottom = getRearPosition() - ClimberPointsArmEncoded.REAR_MIN_EXTEND;
    return currentBottom / maxBottom;
  }

  public double getRearPercentage() {
    return getBottomPercentage();
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

    frontPowerSet = Equations.clamp(frontPowerSet, -0.3, 0.3);
    rearPowerSet = Equations.clamp(rearPowerSet, -0.3, 0.3);
    
    frontWinch.set(frontPowerSet);
    rearWinch.set(rearPowerSet);

    frontPowerSet = 0;
    rearPowerSet = 0;

    if (showDiagnostics == true) {
      SmartDashboard.putNumber("Arm encoder value", getArmEncoder());
      
      SmartDashboard.putNumber("Front position", getFrontPosition());
      SmartDashboard.putNumber("Rear position", getRearPosition());
      SmartDashboard.putNumber("Front velocity", getFrontVelocity());
      SmartDashboard.putNumber("Rear velocity", getRearVelocity());
    }
  }

  public void setFrontIdle(IdleMode idleMode) {
    frontWinch.setIdleMode(idleMode);
  }

  public void setFrontHome() {
    frontHomeValue = frontWinch.getEncoder().getPosition();
  }

  public void setFrontEncoderHome() {
    armEncoder.reset();;
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

  public double getFrontVelocity() {
    return frontWinch.getEncoder().getVelocity();
  }

  public double getRearVelocity() {
    return rearWinch.getEncoder().getVelocity();
  }

  public void setClimberPowers(double frontPower, double rearPower) {
    setFrontClimbPower(frontPower);
    setRearClimbPower(rearPower);
  }

  public void setFrontClimbPower(double frontPower) {
    frontPowerSet = -frontPower;
  }

  public void setRearClimbPower(double rearPower) {
    rearPowerSet = rearPower;
  }

  public void setServo(double position) {
    releaseServo.set(position);
  }

  public void disableServo() {
    releaseServo.setDisabled();
  }
}
