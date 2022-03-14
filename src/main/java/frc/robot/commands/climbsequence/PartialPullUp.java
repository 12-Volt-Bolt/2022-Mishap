// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.values.ClimberPointsBigSprial;
import frc.robot.tools.Timer;

public class PartialPullUp extends CommandBase {

  private boolean pulledUp = false;
  private boolean letDown = false;

  private Timer testBrakeWait = new Timer(1000);

  /** Creates a new PartialPullUp. */
  public PartialPullUp() {
    // Use addRequirements() here to declare subsystem dependencies.
  } 

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pulledUp = false;
    letDown = false;
    Robot.climber.setFrontIdle(IdleMode.kBrake);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = 0;
    double rearPower = 0;

    if (Robot.climber.getFrontPosition() < ClimberPointsBigSprial.FRONT_FINAL_EXTEND && pulledUp == false) {
      frontPower = -0.4;
    }

    if (Robot.climber.getFrontPosition() > ClimberPointsBigSprial.FRONT_FINAL_EXTEND && pulledUp == false) {
      pulledUp = true;
    }

    if (pulledUp == true) {
      frontPower = 0.4;
    } 

    if (Robot.climber.getFrontPosition() < ClimberPointsBigSprial.FRONT_MAX_EXTEND && letDown == false) {
      frontPower = 0;
      testBrakeWait.reset();
      letDown = true;
    }
    
    Robot.climber.setClimberPowers(frontPower, rearPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.climber.setClimberPowers(0, 0);
    Robot.climber.setFrontIdle(IdleMode.kCoast);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return letDown == true && testBrakeWait.isFinished() && pulledUp == true;
  }
}
