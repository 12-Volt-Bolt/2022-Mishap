// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class AutoLockClimber extends CommandBase {

  private Timer endTimer = new Timer(3000);
  private Timer rearHomeSwitchWait = new Timer(2000);
  private boolean rearHomeSwitchHit = false;

  private UnlockServoRelease unlockServoRelease = new UnlockServoRelease();
  private LockServoRelease lockServoRelease = new LockServoRelease();

  /** Creates a new AutoLockClimber. */
  public AutoLockClimber() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    rearHomeSwitchHit = false;
    unlockServoRelease.schedule();
    Robot.climber.setFrontIdle(IdleMode.kCoast);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = -0.1;

    if (Robot.climber.getRearStop()) {
      rearPower = -0.03;
      frontPower = -0.1;
      if (rearHomeSwitchHit == false) {
        rearHomeSwitchWait.reset();
        endTimer.reset();
      }
      rearHomeSwitchHit = true;
    }

    if (Robot.climber.getFrontStop()) {
      frontPower = 0;
    }

    if (rearHomeSwitchWait.isFinished() && rearHomeSwitchHit == true) {
      lockServoRelease.schedule();
    }

    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rearHomeSwitchHit && endTimer.isFinished() && Robot.climber.getFrontStop();
  }
}
