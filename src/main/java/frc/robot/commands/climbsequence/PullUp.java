// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.values.ClimberPointsSmallHex;

public class PullUp extends CommandBase {

  private boolean frontPulledUp = false;
  private boolean rearPulledUp = false;
  private boolean end = false;

  /** Creates a new PullUp. */
  public PullUp() {
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    frontPulledUp = false;
    rearPulledUp = false;
    end = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = 0;

    if (frontPulledUp == false) {
      frontPower = -0.3;
    }

    if (Robot.climber.getFrontPosition() >= ClimberPointsSmallHex.FRONT_PULL_UP) {
      frontPulledUp = true;
    }

    if (frontPulledUp == true) {
      frontPower = 0.1;
    }

    if (frontPulledUp == true && rearPulledUp == false) {
      rearPower = -0.3;
    }

    if (Robot.climber.getRearPosition() <= ClimberPointsSmallHex.REAR_PULL_UP) {
      rearPulledUp = true;
    }

    if (frontPulledUp == true && rearPulledUp == true) {
      rearPower = -0.05;
      frontPower = 0.1;
    }

    if (frontPulledUp == true && rearPulledUp == true && Robot.climber.getFrontPosition() < ClimberPointsSmallHex.FRONT_SAFE_EXTEND) {
      end = true;
    }
    
    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return end;
  }
}