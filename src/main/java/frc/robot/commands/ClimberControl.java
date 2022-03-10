// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ClimberControl extends CommandBase {
  /** Creates a new ClimberControl. */
  public ClimberControl() {
    SmartDashboard.putNumber("Bottom power", 0);
    SmartDashboard.putNumber("Top power", 0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.climber.setClimberPowers(
        SmartDashboard.getNumber("Top power", 0)
      , SmartDashboard.getNumber("Bottom power", 0)
      );
    }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
