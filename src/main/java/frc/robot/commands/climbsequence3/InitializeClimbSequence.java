// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence3;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.commands.climbsequence.ReleaseRear;
import frc.robot.commands.climbsequence.VelocityHomeRearArm;
import frc.robot.tools.Timer;

public class InitializeClimbSequence extends CommandBase {
  
  private ReleaseRear releaseRear = new ReleaseRear();
  private VelocityHomeRearArm velocityHomeRearArm = new VelocityHomeRearArm();

  private Timer rearLetUpTimer = new Timer(300);
  private boolean startedRearHome = false;
  private boolean finishedRearHome = false;

  /** Creates a new InitializeClimbSequence. */
  public InitializeClimbSequence() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    releaseRear.schedule();
    rearLetUpTimer.reset();
    startedRearHome = false;
    finishedRearHome = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (releaseRear.isScheduled()) {
      rearLetUpTimer.reset();
    }

    if (rearLetUpTimer.isFinished() && startedRearHome == false) {
      startedRearHome = true;
      velocityHomeRearArm.schedule();
      rearLetUpTimer.reset();
    }

    if (velocityHomeRearArm.isScheduled() && velocityHomeRearArm.isFinished() == false) {
      rearLetUpTimer.reset();
    } 

    if (startedRearHome == true && velocityHomeRearArm.isScheduled() == false && rearLetUpTimer.isFinished()) {
      finishedRearHome = true;
      if (Robot.climber.getRearPercentage() < 1) {
        Robot.climber.setRearClimbPower(0.4);
      } else {
        Robot.climber.setRearClimbPower(0);
        if (Robot.climber.getFrontPercentage() < 1.3) {
          Robot.climber.setFrontClimbPower(0.3);
        } else {
          Robot.climber.setFrontClimbPower(0);
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Robot.climber.getRearPercentage() > 1 && Robot.climber.getFrontPercentage() > 1.3 && finishedRearHome == true;
  }
}
