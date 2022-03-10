// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.values.ClimberPointsBareHex;

public class PartialPullUp extends CommandBase {
  /** Creates a new PartialPullUp. */
  public PartialPullUp() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPower = -0.4;;
    double rearPower = 0;
    
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
    return Robot.climber.getFrontPosition() < ClimberPointsBareHex.FRONT_FINAL_EXTEND;
  }
}
