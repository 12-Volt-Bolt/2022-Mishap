// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence2;

import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class StopArms extends CommandBase {
  /** Creates a new StopArms. */
  public StopArms(List<CommandBase> cancelCommands) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.cancelCommands = cancelCommands;
  }
  
  private List<CommandBase> cancelCommands;

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    for (CommandBase command : cancelCommands) {
      command.cancel();
    }
    System.out.println("StopArms");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
