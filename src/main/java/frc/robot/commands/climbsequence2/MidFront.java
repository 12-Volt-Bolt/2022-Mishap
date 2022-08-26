// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence2;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MidFront extends CommandBase {

  private FrontClimberHoldPercentage frontClimberDown = new FrontClimberHoldPercentage(0.4, 0.05, 0, new ArrayList<CommandBase>());
  private RearClimberHoldPercentage rearClimberDown = new RearClimberHoldPercentage(1.0, 0.05, 0, new ArrayList<CommandBase>());
  
  private List<CommandBase> cancelCommands;

  /** Creates a new MidFront. */
  public MidFront(List<CommandBase> cancelCommands) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.cancelCommands = cancelCommands;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    frontClimberDown.schedule();
    rearClimberDown.schedule();

    for (CommandBase command : cancelCommands) {
      command.cancel();
    }
    System.out.println("MidFront" + ", tolerance: " + isFinished());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  public ArrayList<CommandBase> getChildCommands() {
    ArrayList<CommandBase> output = new ArrayList<CommandBase>();
    output.add(frontClimberDown);
    output.add(rearClimberDown);
    return output;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return frontClimberDown.withinTolerance() && rearClimberDown.withinTolerance();
  }
}
