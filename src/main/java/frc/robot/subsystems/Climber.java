// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  private final CANSparkMax frontWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.FRONT_WINCH, MotorType.kBrushless);
  private final CANSparkMax rearWinch = new CANSparkMax(frc.robot.constants.robotmap.motor.Climber.REAR_WINCH, MotorType.kBrushless);

  private final DigitalInput frontStop = new DigitalInput(1);
  private final DigitalInput rearStop = new DigitalInput(2);

  private double frontPowerSet = 0;
  private double rearPowerSet = 0;

  public Climber() {}

  @Override
  public void periodic() {
    if (frontStop.get() != true && frontPowerSet > 0) {
      frontWinch.set(frontPowerSet);
    } else {
      frontWinch.set(0);
    }
    if (rearStop.get() != true && rearPowerSet > 0) {
      rearWinch.set(rearPowerSet);
    } else {
      rearWinch.set(0);
    }
  }

  public void setClimberPowers(double frontPower, double rearPower) {
    frontPowerSet = frontPower;
    rearPowerSet = rearPower;
  }
}
