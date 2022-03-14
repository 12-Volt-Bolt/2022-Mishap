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
  Timer rearReleaseTimer = new Timer(200);

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

    if (rearHomeReleased && rearReleaseTimer.isFinished()) {
      rearPower = -0.2;
    }

    if (rearHomeReleased && Robot.climber.getRearStop() && rearHomeDone == false && rearReleaseTimer.isFinished()) {
      rearHomeDone = true;
      rearPower = -0.02;
      Robot.climber.setRearHome();
    }

    if (rearHomeDone && frontHomeReleased == false && Robot.climber.getFrontStop() == true) {
      frontPower = 0.2;
      Robot.climber.setFrontIdle(IdleMode.kBrake);
    }

    if (rearHomeDone && frontHomeReleased == false && Robot.climber.getFrontStop() == false) {
      frontHomeReleased = true;
      frontReleaseTimer.reset();
    }

    if (frontHomeReleased && frontReleaseTimer.isFinished() && rearHomeDone == true) {
      frontPower = -0.2;
    }

    if (frontHomeReleased && Robot.climber.getFrontStop() && frontReleaseTimer.isFinished()) {
      Robot.climber.setFrontHome();
      frontPower = 0;
      endCommand = true;
    }

    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return endCommand;
  }
}
