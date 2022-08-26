// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class HomeClimbArms extends CommandBase {

  boolean rearHomeReleased = false;
  boolean rearHomeDone = false;
  boolean frontHomeDone = false;

  Timer rearReleaseTimer = new Timer(200);
  Timer frontCheckDelay = new Timer(200);

  boolean frontHomeReleased = false;
  Timer frontReleaseTimer = new Timer(200);

  boolean endCommand = false;

  /** Creates a new HomeClimbArms. */
  public HomeClimbArms() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.climber.setFrontIdle(IdleMode.kCoast);

    rearHomeReleased = false;
    rearHomeDone = false;
    frontHomeDone = false;

    frontHomeReleased = false;

    endCommand = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = 0;

    if (rearHomeReleased == false && Robot.climber.getRearStop() == false) {
      rearHomeReleased = true;
      rearReleaseTimer.reset();
    }

    if (rearHomeReleased == true && rearReleaseTimer.isFinished()) {
      rearPower = -0.2;
    }

    if (rearHomeReleased == true && Robot.climber.getRearStop() == true && rearHomeDone == false && rearReleaseTimer.isFinished()) {
      rearHomeDone = true;
      rearPower = -0.02;
      Robot.climber.setRearHome();
    }

    // if (rearHomeDone == true && frontHomeReleased == false) {
    //   frontPower = 0.2;
    //   frontCheckDelay.reset();
    //   Robot.climber.setFrontIdle(IdleMode.kBrake);
    // }

    // if (rearHomeDone == true && frontHomeReleased == false && frontCheckDelay.isFinished() && Robot.climber.getFrontVelocity() < 1000) {
    //   frontHomeReleased = true;
    //   frontReleaseTimer.reset();
    // }

    // if (rearHomeDone == true && frontHomeReleased == false) {
    //   frontHomeReleased = true;
    //   frontCheckDelay.reset();
    // }

    // if (rearHomeDone == true) {
    //   frontPower = -0.2;
    // }

    // System.out.println("Front home: " + frontHomeReleased + ", front delay: " + frontCheckDelay.isFinished() + ", front velocity: " + Robot.climber.getFrontVelocity());

    // if (frontHomeReleased == true && frontCheckDelay.isFinished() == true && Robot.climber.getFrontVelocity() < 1100) {
    //   System.out.println("Front climber home position: " + Robot.climber.getFrontPosition());
    //   Robot.climber.setFrontHome();
    //   frontPower = 0;
    //   endCommand = true;
    // }

    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rearHomeDone;
  }
}
