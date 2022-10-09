// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoLockClimberVelocity;
import frc.robot.commands.ShootHighGoal;
import frc.robot.commands.autocommands.ShootHighDrive;
import frc.robot.commands.climbsequence3.InitializeClimbSequence;
import frc.robot.commands.climbsequence3.MainClimbSequence;
import frc.robot.subsystems.Auto;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake.IntakePosition;
import frc.robot.tools.EasyPov;
import frc.robot.tools.Equations;
import frc.robot.tools.IOTools;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  public static Drivetrain drivetrain = new Drivetrain();
  public static Intake intake = new Intake();
  public static Shooter shooter = new Shooter();
  public static Climber climber = new Climber();
  public static Auto auto = new Auto();

  public static Joystick joy_1 = new Joystick(0);

  public static EasyPov feederPov = new EasyPov(new int[] { 1, 0, 0, 0, -1, 0, 0, 0 }, 0);

  // public static ClimberControl manualClimberControl = new ClimberControl();
  // public static AutoLockClimber autoLockClimber = new AutoLockClimber();
  public static InitializeClimbSequence initializeClimbSequence = new InitializeClimbSequence();
  public static MainClimbSequence mainClimbSequence = new MainClimbSequence();

  public static AutoLockClimberVelocity autoLockClimberVelocity = new AutoLockClimberVelocity();
  
  public static ShootHighGoal shootHighGoal = new ShootHighGoal();

  // Auto Commands
  public static ShootHighDrive shootHighDrive = new ShootHighDrive();

  public double driveSpeedMultiplier = 0.7;
  public double turnSpeedMultiplier = 0.7;

  @Override
  public void robotInit() {
    // SmartDashboard.putData(autoLockClimberVelocity); // The robot can go participate in a water game.
    SmartDashboard.putData(initializeClimbSequence);
    SmartDashboard.putData(mainClimbSequence);
    
    SmartDashboard.putData(shootHighGoal);

    // SmartDashboard.putData(manualClimberControl);
    // SmartDashboard.putData(velocityHomeClimbArms);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    shootHighDrive.schedule();
  }

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    driveSpeedMultiplier = 0.7;
    turnSpeedMultiplier = 0.7;
  }

  @Override
  public void teleopPeriodic() {
    // SmartDashboard.putNumber("Front percentage", climber.getFrontPercentage());
    // SmartDashboard.putNumber("Rear percentage", climber.getRearPercentage());
    // SmartDashboard.putNumber("Front value", climber.getArmEncoder());

    if (initializeClimbSequence.isScheduled()) {
      driveSpeedMultiplier = 0.4;
      turnSpeedMultiplier = 0.5;
    }

    drivetrain.arcadeDriveTurnRollover(
        IOTools.axisDeadzoneRemap(
            joy_1.getRawAxis(frc.robot.constants.controllermap.axis.Drivetrain.Y_AXIS) * driveSpeedMultiplier
          , 0.0
          , 1.0
          , 0.1
          , 1.0
        )
          , IOTools.axisDeadzoneRemap(
            joy_1.getRawAxis(frc.robot.constants.controllermap.axis.Drivetrain.Z_AXIS) * turnSpeedMultiplier
          , 0.0
          , 1.0
          , 0.1
          , 1.0
        )
      , 0
    );

    // System.out.println("Front velocity: " + climber.getFrontVelocity());

    intake.setConveyorPower(
        Equations.triggersAsJoy(
            frc.robot.constants.controllermap.axis.Intake.CONVEYOR_IN
          , frc.robot.constants.controllermap.axis.Intake.CONVEYOR_OUT
          , joy_1
          )
      , 0);

    if (joy_1.getRawButtonPressed(frc.robot.constants.controllermap.button.Intake.TOGGLE)) {
      switch (intake.getGoalPosition()) {
        case Up:
          intake.setGoalPosition(IntakePosition.Down, 0);
          break;
        case Down:
          intake.setGoalPosition(IntakePosition.Up, 0);
          break;
        default:
          intake.setGoalPosition(IntakePosition.Down, 0);
          break;
      }
    }

    shooter.spinFeeder(feederPov.getPovValue(joy_1.getPOV()));

    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
    SmartDashboard.delete("shootHighGoal");    
    SmartDashboard.delete("shootLowGoal");
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
