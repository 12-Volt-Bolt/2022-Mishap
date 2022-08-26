// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence2;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Timer;

public class RearClimberHoldPercentage extends CommandBase {
  /** Creates a new RearClimberHoldPercentage. */
  public RearClimberHoldPercentage(double percentage, double tolerance, double feedForward, List<CommandBase> cancelCommands) {
    // Use addRequirements() here to declare subsystem dependencies.
    pid.setSetpoint(percentage);
    pid.setTolerance(tolerance);
    this.feedForward = feedForward;
    this.cancelCommands = cancelCommands;
  }

  private PIDController pid = new PIDController(1, 0, 0);
  private double feedForward;
  private List<CommandBase> cancelCommands;
  private Timer minRunTime = new Timer(100);

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    minRunTime.reset();
    pid.reset();
    for (CommandBase command : cancelCommands) {
      command.cancel();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double pidPower = pid.calculate(Robot.climber.getBottomPercentage());
    // System.out.println("PID power: " + -pidPower + ", Current error: " + pid.getPositionError());
    // SmartDashboard.putNumber("PID power", pidPower);
    // SmartDashboard.putNumber("Current error", pid.getPositionError());
    Robot.climber.setRearClimbPower(feedForward + pidPower);
  }

  public boolean withinTolerance() {
    return pid.atSetpoint() && minRunTime.isFinished();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
