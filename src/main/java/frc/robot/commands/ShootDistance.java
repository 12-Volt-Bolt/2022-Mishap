// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.tools.Equations;
import frc.robot.tools.Timer;

public class ShootDistance extends CommandBase {

  public double distanceInFeet = 4;

  private PIDController shooterPID = new PIDController(0.00045, 0.001, 0);

  private boolean atSpeed = false;
  private Timer atSpeedTimer = new Timer(100);
  private Timer feederTimer = new Timer(5000);

  /** Creates a new ShootDistance. */
  public ShootDistance() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    atSpeed = false;
    feederTimer.reset();
    atSpeedTimer.reset();

    shooterPID.setTolerance(5, 5);
    shooterPID.setIntegratorRange(-0.1, 0.1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double goalRPM = 0;
    double feedForward = 0;
    
    if (distanceInFeet <= 05) { goalRPM = 2320; feedForward = 0.5; }
    if (distanceInFeet <= 04) { goalRPM = 2215; feedForward = 0.4; }
    if (distanceInFeet <= 03) { goalRPM = 2175; feedForward = 0.387; }
    // if (distanceInFeet <= 03) { goalRPM = 2120; feedForward = 0.375; }

    goalRPM += 0;
    shooterPID.setSetpoint(goalRPM);

    double t = shooterPID.calculate(Robot.shooter.getVelocity());
    t = Equations.clamp(t, -0.1, 0.1);
    double power = t + feedForward;
    Robot.shooter.spin(power);
    
    SmartDashboard.putNumber("Goal RPM", goalRPM);
    SmartDashboard.putNumber("Current RPM", Robot.shooter.getVelocity());

    if (shooterPID.atSetpoint() != true) {
      atSpeedTimer.reset();
    }

    if (atSpeedTimer.isFinished() == false && atSpeed == false) {
      feederTimer.reset();
    }

    if (atSpeedTimer.isFinished()) {
      atSpeed = true;
    }

    if (atSpeed == true && feederTimer.isFinished() == false) {
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
    return atSpeed == true && feederTimer.isFinished() == true;
  }
}
