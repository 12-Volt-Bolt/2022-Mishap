// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.values.ClimberPointsBareHex;

public class MaxExtend extends CommandBase {

  boolean frontInPosition = false;

  /** Creates a new MaxExtend. */
  public MaxExtend() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.climber.setFrontIdle(IdleMode.kBrake);

    frontInPosition = false;

    System.out.println("Max Extend");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = -0.3;
    
    if (Robot.climber.getFrontPosition() > ClimberPointsBareHex.FRONT_MAX_EXTEND && frontInPosition == false) {
      frontPower = 0.2;
    }

    if (Robot.climber.getFrontPosition() > ClimberPointsBareHex.FRONT_MAX_EXTEND_SLOW_DOWN && frontInPosition == false) {
      frontPower = 0.5;
    }

    if (Robot.climber.getFrontPosition() <= ClimberPointsBareHex.FRONT_MAX_EXTEND && frontInPosition == false) {
      frontInPosition = true;
      frontPower = 0;
    }

    if (frontInPosition == true) {
      rearPower = 0.4;
    }

    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return frontInPosition && Robot.climber.getRearPosition() >= ClimberPointsBareHex.REAR_MAX_EXTEND;
  }
}
