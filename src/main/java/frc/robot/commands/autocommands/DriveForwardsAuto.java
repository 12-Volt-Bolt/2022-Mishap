// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autocommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class DriveForwardsAuto extends CommandBase {

  private Timer timer = new Timer(1250);

  /** Creates a new DriveForwardsAuto. */
  public DriveForwardsAuto() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.drivetrain.arcadeDriveTurnThrottle(-0.25, 0, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.drivetrain.arcadeDriveTurnThrottle(0, 0, 0);}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.isFinished();
  }
}
