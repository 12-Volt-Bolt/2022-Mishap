// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class VelocityHomeRearArm extends CommandBase {

  private boolean endCommand = false;

  private Timer checkVelocityStartTime = new Timer(500);

  /** Creates a new VelocityHomeClimber. */
  public VelocityHomeRearArm() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    endCommand = false;
    checkVelocityStartTime.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
    if (Robot.climber.getRearVelocity() > -400 && checkVelocityStartTime.isFinished()) {
      endCommand = true;
    }

    Robot.climber.setClimberPowers(0, -0.2);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted == false) {
      Robot.climber.setRearHome();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return endCommand;
  }
}
