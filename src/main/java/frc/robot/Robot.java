// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoLockClimber;
import frc.robot.commands.ClimberControl;
import frc.robot.commands.LockServoRelease;
import frc.robot.commands.ShootHighGoal;
import frc.robot.commands.ShootLowGoal;
import frc.robot.commands.UnlockServoRelease;
import frc.robot.commands.autocommands.DoShootLowDrive;
import frc.robot.commands.autocommands.DoShootHighDrive;
import frc.robot.commands.autocommands.DoLowShootAuto;
import frc.robot.commands.autocommands.DriveForwardsAuto;
import frc.robot.commands.autocommands.ShootHighDrive;
import frc.robot.commands.autocommands.ShootLowDrive;
import frc.robot.commands.climbsequence.InitializeClimbSequence;
import frc.robot.commands.climbsequence.MainClimbSequence;
import frc.robot.commands.climbsequence.Step2Climp;
import frc.robot.constants.SpinShooterFromSmartdashboard;
import frc.robot.subsystems.Auto;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake.IntakePosition;
import frc.robot.tools.EasyPov;
import frc.robot.tools.Equations;

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

  public static SpinShooterFromSmartdashboard spinShooterFromSmartdashboard = new SpinShooterFromSmartdashboard();

  public static UnlockServoRelease unlockServoRelease = new UnlockServoRelease();
  public static LockServoRelease lockServoRelease = new LockServoRelease();

  // public static ClimberControl manualClimberControl = new ClimberControl();
  public static AutoLockClimber autoLockClimber = new AutoLockClimber();
  public static InitializeClimbSequence climbSequence = new InitializeClimbSequence();
  public static MainClimbSequence mainClimbSequence = new MainClimbSequence();
  
  public static ShootHighGoal shootHighGoal = new ShootHighGoal();
  public static ShootLowGoal shootLowGoal = new ShootLowGoal();

  // Auto Commands
  public static DoShootLowDrive doShootLowDrive = new DoShootLowDrive();
  public static DoShootHighDrive doShootHighDrive = new DoShootHighDrive();

  public static ShootLowDrive shootLowDrive = new ShootLowDrive();
  public static ShootHighDrive shootHighDrive = new ShootHighDrive();

  @Override
  public void robotInit() {
    SmartDashboard.putData(doShootLowDrive);
    SmartDashboard.putData(doShootHighDrive);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    if (doShootLowDrive.isScheduled()) {
      shootLowDrive.schedule();
    } else if (doShootHighDrive.isScheduled()) {
      shootHighDrive.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    SmartDashboard.putData(autoLockClimber);
    SmartDashboard.putData(climbSequence);
    SmartDashboard.putData(mainClimbSequence);
    
    SmartDashboard.putData(shootLowGoal);
    SmartDashboard.putData(shootHighGoal);
  }

  @Override
  public void teleopPeriodic() {

    drivetrain.arcadeDriveTurnThrottle(
        joy_1.getRawAxis(frc.robot.constants.controllermap.axis.Drivetrain.Y_AXIS) * 0.7
      , joy_1.getRawAxis(frc.robot.constants.controllermap.axis.Drivetrain.Z_AXIS) * 0.7
      , 0
      );

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

    /*
    SmartDashboard.putData(spinShooterFromSmartdashboard);
    */

    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

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
