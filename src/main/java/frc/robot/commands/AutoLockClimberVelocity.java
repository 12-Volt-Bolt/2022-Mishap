// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.commands.climbsequence.VelocityHomeRearArm;
import frc.robot.tools.Timer;

public class AutoLockClimberVelocity extends CommandBase {

  private boolean startLock = false;
  private boolean aleadyLocked = false;
  private Timer endTimer = new Timer(1000);

  private LockServoRelease lockServoRelease = new LockServoRelease();
  private VelocityHomeRearArm velocityHomeRearArm = new VelocityHomeRearArm();

  /** Creates a new AutoLockClimberVelocity. */
  public AutoLockClimberVelocity() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    velocityHomeRearArm.schedule();
    startLock = false;
    aleadyLocked = Robot.climber.getRearStop();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (velocityHomeRearArm.isScheduled() == false && startLock == false) {
      startLock = true;
      if (aleadyLocked == false) {
        endTimer.reset();
        lockServoRelease.schedule();
      }
    }
    
    if (startLock == true) {
    }
    Robot.climber.setClimberPowers(0, -0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.climber.setClimberPowers(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return startLock == true && lockServoRelease.isScheduled() == false && endTimer.isFinished();
  }
}
