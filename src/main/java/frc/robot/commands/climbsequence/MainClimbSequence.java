// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MainClimbSequence extends SequentialCommandGroup {

  public PullUp pullUp1 = new PullUp(0);
  public PullUp pullUp2 = new PullUp(1);

  public PartialPullUp pullUp3 = new PartialPullUp();

  public MaxExtend maxExtend1 = new MaxExtend();
  public MaxExtend maxExtend2 = new MaxExtend();

  public Wait3Seconds wait3Seconds1 = new Wait3Seconds(1000);
  public Wait3Seconds wait3Seconds2 = new Wait3Seconds(3000);

  /** Creates a new MainClimbSequence. */
  public MainClimbSequence() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(pullUp1, wait3Seconds1, maxExtend1 /*, pullUp2, maxExtend2, pullUp3*/ );
  }
}
