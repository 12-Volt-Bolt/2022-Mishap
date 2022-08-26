// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence2;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MainClimbSequence extends SequentialCommandGroup {

  private InitializeClimbSequence initializeClimbSequence = new InitializeClimbSequence();

  private FrontPullDown frontPullDown = new FrontPullDown(new ArrayList<CommandBase>());
  private RearPullDown rearPullDown = new RearPullDown(frontPullDown.getChildCommands());
  private FrontLetUp frontLetUp = new FrontLetUp(rearPullDown.getChildCommands());
  private WaitTimeReleaseClimbers wait = new WaitTimeReleaseClimbers(3000, new ArrayList<>());
  private RearLetUp rearLetUp = new RearLetUp(frontLetUp.getChildCommands());
  private MidFront midFront = new MidFront(rearLetUp.getChildCommands());
  private MidRear midRear = new MidRear(midFront.getChildCommands());
  private MaxFront maxFront = new MaxFront(midRear.getChildCommands());
  private MaxRear maxRear = new MaxRear(maxFront.getChildCommands());
  private StopArms finalize = new StopArms(maxRear.getChildCommands());

  /** Creates a new MainClimbSequence. */
  public MainClimbSequence() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(frontPullDown, rearPullDown, frontLetUp, wait, rearLetUp, maxFront, maxRear, finalize);
  }

  public void putOnSmartDashboard() {
    SmartDashboard.putData(initializeClimbSequence);
    SmartDashboard.putData(this);
  }

  @Override public void schedule() {
    initializeClimbSequence.endClimberControl();
    super.schedule();
  }
}
