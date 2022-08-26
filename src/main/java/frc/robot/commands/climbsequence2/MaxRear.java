// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence2;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MaxRear extends CommandBase {

  private FrontClimberHoldPercentage frontClimber = new FrontClimberHoldPercentage(1.0, 0.05, 0, new ArrayList<CommandBase>());
  private RearClimberHoldPercentage rearClimber = new RearClimberHoldPercentage(1.0, 0.05, 0, new ArrayList<CommandBase>());
  
  private List<CommandBase> cancelCommands;

  /** Creates a new MaxRear. */
  public MaxRear(List<CommandBase> cancelCommands) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.cancelCommands = cancelCommands;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    frontClimber.schedule();
    rearClimber.schedule();

    for (CommandBase command : cancelCommands) {
      command.cancel();
    }
    System.out.println("MaxRear" + ", tolerance: " + isFinished());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  public ArrayList<CommandBase> getChildCommands() {
    ArrayList<CommandBase> output = new ArrayList<CommandBase>();
    output.add(frontClimber);
    output.add(rearClimber);
    return output;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return frontClimber.withinTolerance() && rearClimber.withinTolerance();
  }
}
