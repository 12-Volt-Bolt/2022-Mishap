// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.values.ClimberPointsBigSprial;
import frc.robot.tools.Timer;

public class PullUp extends CommandBase {

  private boolean frontPulledUp = false;
  private boolean rearPulledUp = false;
  private boolean end = false;
  private boolean rearDelay = false;

  private Timer swingWait;
  private Timer rearReleaseDelay = new Timer(500);

  /** Creates a new PullUp. */
  public PullUp(double swingWaitTimeSeconds) {
    swingWait = new Timer(((long) swingWaitTimeSeconds * 1000) + 1);
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.climber.setFrontIdle(IdleMode.kBrake);
    frontPulledUp = false;
    rearPulledUp = false;
    end = false;
    rearDelay = false;
    
    System.out.println("Pull Up");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = 0;

    if (frontPulledUp == false) {
      frontPower = -0.4;
    }

    if (Robot.climber.getFrontPosition() >= ClimberPointsBigSprial.FRONT_PULL_UP_SLOW_DOWN) {
      //frontPower = -0.3;
    }

    if (Robot.climber.getFrontPosition() >= ClimberPointsBigSprial.FRONT_PULL_UP || Robot.climber.getFrontStop() == true) {
      frontPulledUp = true;
    }

    if (frontPulledUp == true) {
      frontPower = 0;
    }

    if (Robot.climber.getFrontStop() == true) {
      frontPower = 0.2;
    }

    if (frontPulledUp == true) {
      rearPower = -0.3;
    }

    if (Robot.climber.getRearPosition() <= ClimberPointsBigSprial.REAR_PULL_UP && rearPulledUp == false) {
      rearPulledUp = true;
      swingWait.reset();
    }

    if (rearDelay == false && swingWait.isFinished() && rearPulledUp == true) {
      rearDelay = true;
      rearReleaseDelay.reset();
    }

    if (frontPulledUp == true && rearPulledUp == true && swingWait.isFinished()) {
      if (rearReleaseDelay.isFinished()) {
        rearPower = 0.2;
      }
      frontPower = 0.2;
    }

    if (frontPulledUp == true && rearPulledUp == true) {
      if (Robot.climber.getFrontPosition() < ClimberPointsBigSprial.FRONT_SAFE_EXTEND) {
        frontPower = 0;
      }
      if (Robot.climber.getRearPosition() > ClimberPointsBigSprial.REAR_SAFE_EXTEND) {
        rearPower = 0;
      }
      if (Robot.climber.getFrontPosition() < ClimberPointsBigSprial.FRONT_SAFE_EXTEND && Robot.climber.getRearPosition() > ClimberPointsBigSprial.REAR_SAFE_EXTEND)
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