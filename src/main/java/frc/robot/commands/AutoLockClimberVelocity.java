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
  private Timer endTimer = new Timer(3000);

  // private UnlockServoRelease unlockServoRelease = new UnlockServoRelease();
  private VelocityHomeRearArm velocityHomeRearArm = new VelocityHomeRearArm();

  /** Creates a new AutoLockClimberVelocity. */
  public AutoLockClimberVelocity() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // unlockServoRelease.schedule();
    Robot.climber.setServo(0.1);
    velocityHomeRearArm.schedule();
    startLock = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (startLock == true) {
      Robot.climber.setServo(Robot.climber.releaseServo.get() + 0.05);
    }
    if (velocityHomeRearArm.isScheduled() == false && startLock == false) {
      startLock = true;
      endTimer.reset();
    }
    
    Robot.climber.setClimberPowers(0, -0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.climber.disableServo();
    Robot.climber.setClimberPowers(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return startLock == true && endTimer.isFinished();
  }
}
