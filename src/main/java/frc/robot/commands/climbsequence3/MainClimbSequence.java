// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence3;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.commands.climbsequence3.ClimbCommandData.DirectionRestriction;
import frc.robot.tools.Equations;
import frc.robot.tools.Timer;

public class MainClimbSequence extends CommandBase {
  private List<ClimbCommandData> commandList = new ArrayList<>();

  private int currentCommand = 0;
  private boolean isFinished = false;

  private PIDController frontPid = new PIDController(1, 0, 0);
  private double frontFeedForward = 0;

  private PIDController rearPid = new PIDController(1, 0, 0);
  private double rearFeedForward = 0;

  private Timer minRunTimer = new Timer(100);

  /** Creates a new MainClimbSequence. */
  public MainClimbSequence() {
    addRequirements(Robot.climber);
  }

  private void setClimbers(ClimbCommandData current, ClimbCommandData previous) {
    System.out.println(current.name);
    frontPid.setSetpoint(current.getFrontPercentage(previous));
    frontPid.setTolerance(current.getFrontTolerance(previous));
    frontFeedForward = current.getFrontFeedForward(previous);

    rearPid.setSetpoint(current.getRearPercentage(previous));
    rearPid.setTolerance(current.getRearTolerance(previous));
    rearFeedForward = current.getRearFeedForward(previous);

    minRunTimer.setLength(current.minRunTime);
    minRunTimer.reset();

    // System.out.println("Front goal: " + frontPid.getSetpoint() + ", Rear goal: " + rearPid.getSetpoint());
    // System.out.println("Front tolerance: " + current.getFrontTolerance(previous) + ", Rear tolerance: " + current.getRearTolerance(previous));
    // System.out.println("Front ff: " + current.getFrontFeedForward(previous) + ", Rear ff: " + current.getRearFeedForward(previous));

    // SmartDashboard.putString("Current climb command", current.name);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    commandList.clear();

    commandList.add(new ClimbCommandData(-0.07, 1.0, "Front Pull Down").setFrontSettings(0.05, -0.125, DirectionRestriction.NONE).setRearSettings(0.05, 0.0, DirectionRestriction.NONE));
    commandList.add(new ClimbCommandData(Double.NaN, 0.1, "Rear Pull Down"));
    commandList.add(new ClimbCommandData(0.0, 0.1, "Front Let Up").setFrontSettings(0.05, 0.0, DirectionRestriction.NONE));
    commandList.add(new ClimbCommandData(Double.NaN, Double.NaN, "Settle", 500));
    commandList.add(new ClimbCommandData(Double.NaN, 0.5, "Rear Let Up"));
    commandList.add(new ClimbCommandData(0.4, Double.NaN, "Front Mid Position"));
    commandList.add(new ClimbCommandData(Double.NaN, 0.0, "Rear Pull Down"));
    commandList.add(new ClimbCommandData(0.9, Double.NaN, "Front Max Position").setFrontSettings(0.05, 0.0, DirectionRestriction.EXTEND));
    commandList.add(new ClimbCommandData(Double.NaN, 1.0, "Rear Max Position").setFrontSettings(10.0, 0.0, DirectionRestriction.ALL));

    currentCommand = 0;
    isFinished = false;

    for (ClimbCommandData data : commandList) {
      
      System.out.println(data.name);
    }
    setClimbers(commandList.get(currentCommand), ClimbCommandData.getDefault());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double frontPidPower = frontPid.calculate(Robot.climber.getFrontPercentage());
    frontPidPower += frontFeedForward;

    switch (commandList.get(currentCommand).frontRestriction) {
      case ALL:
        frontPidPower = 0;
        break;
      case EXTEND:
        frontPidPower = (frontPidPower > 0) ? frontPidPower : 0;
        break;
      case NONE:
        break;
      case RETRACT:
      frontPidPower = (frontPidPower < 0) ? frontPidPower : 0;
        break;
      default:
        break;
    }
    
    Robot.climber.setFrontClimbPower(frontPidPower);
    
    double rearPidPower = rearPid.calculate(Robot.climber.getRearPercentage());
    rearPidPower += rearFeedForward;

    switch (commandList.get(currentCommand).rearRestriction) {
      case ALL:
        rearPidPower = 0;
        break;
      case EXTEND:
        rearPidPower = (rearPidPower > 0) ? rearPidPower : 0;
        break;
      case NONE:
        break;
      case RETRACT:
        rearPidPower = (rearPidPower < 0) ? rearPidPower : 0;
        break;
      default:
        break;
    }

    // SmartDashboard.putNumber("Front PID power", frontPidPower);
    // SmartDashboard.putNumber("Front current error", frontPid.getPositionError());

    // SmartDashboard.putNumber("Rear PID power", rearPidPower);
    // SmartDashboard.putNumber("Rear current error", rearPid.getPositionError());

    Robot.climber.setRearClimbPower(rearPidPower);

    // System.out.println(frontPid.atSetpoint() + ", " + rearPid.atSetpoint() + ", " + minRunTimer.isFinished());
    if (frontPid.atSetpoint() && rearPid.atSetpoint() && minRunTimer.isFinished()) {
      System.out.print("Next command: ");
      currentCommand += 1;
      if (currentCommand >= commandList.size()) {
        isFinished = true;
      } else {
        setClimbers(commandList.get(currentCommand), commandList.get(currentCommand - 1));
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
