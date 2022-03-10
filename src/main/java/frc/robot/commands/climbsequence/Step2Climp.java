// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Step2Climp extends SequentialCommandGroup {

  public static MaxExtend maxExtend = new MaxExtend();
  public static PullUp pullUp = new PullUp(0);

  /** Creates a new Step2Climp. */
  public Step2Climp() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(maxExtend, pullUp);
  }
}
