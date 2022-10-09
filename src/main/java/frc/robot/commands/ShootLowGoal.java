// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class ShootLowGoal extends CommandBase {

  private Timer speedUpTimer = new Timer(2000);
  private Timer emptyStorageTimer = new Timer(4000);

  /** Creates a new ShootLowGoal. */
  public ShootLowGoal() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    speedUpTimer.reset();
    emptyStorageTimer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    Robot.shooter.spin(0.275);

    if (speedUpTimer.isFinished() == false) {
      emptyStorageTimer.reset();
    }

    if (speedUpTimer.isFinished()) {
      Robot.shooter.spinFeeder(1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.shooter.spinFeeder(0);
    Robot.shooter.spin(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return emptyStorageTimer.isFinished();
  }
}
