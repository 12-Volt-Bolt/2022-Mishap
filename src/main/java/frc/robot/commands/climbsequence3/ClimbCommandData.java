// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climbsequence3;

/** Add your docs here. */
public class ClimbCommandData {

  public enum DirectionRestriction {
      NONE
    , EXTEND
    , RETRACT
    , ALL
  }

  private double frontPercentage;
  private double frontTolerance;
  private double frontFeedForward;
  public DirectionRestriction frontRestriction;

  private double rearPercentage;
  private double rearTolerance;
  private double rearFeedForward;
  public DirectionRestriction rearRestriction;
  
  public long minRunTime;
  public String name;

  public ClimbCommandData(double frontPercentage, double rearPercentage, String name, long minRunTime) {
    this.frontPercentage = frontPercentage;
    this.frontTolerance = Double.NaN;
    this.frontFeedForward = Double.NaN;
    this.frontRestriction = DirectionRestriction.NONE;

    this.rearPercentage = rearPercentage;
    this.rearTolerance = Double.NaN;
    this.rearFeedForward = Double.NaN;
    this.rearRestriction = DirectionRestriction.NONE;

    this.minRunTime = minRunTime;
    this.name = name;
    System.out.println(this.name);
  }

  public ClimbCommandData(double frontPercentage, double rearPercentage, String name) {
    this(frontPercentage, rearPercentage, name, 100);
  }

  public static ClimbCommandData getDefault() {
    return new ClimbCommandData(0.0, 0.0, "Default").setFrontSettings(0.05, 0.0, DirectionRestriction.NONE).setRearSettings(0.05, 0.0, DirectionRestriction.NONE);
  }

  public ClimbCommandData setFrontSettings(double Tolerance, double FeedForward, DirectionRestriction restriction) {
    this.frontTolerance = Tolerance;
    this.frontFeedForward = FeedForward;
    this.frontRestriction = restriction;
    return this;
  }

  public ClimbCommandData setRearSettings(double Tolerance, double FeedForward, DirectionRestriction restriction) {
    this.rearTolerance = Tolerance;
    this.rearFeedForward = FeedForward;
    this.rearRestriction = restriction;
    return this;
  }

  public double getFrontPercentage(ClimbCommandData previous) {
    if (((Double) this.frontPercentage).isNaN()) {
      this.frontPercentage = previous.frontPercentage;
    }
    return this.frontPercentage;
  }

  public double getRearPercentage(ClimbCommandData previous) {
    if (((Double) this.rearPercentage).isNaN()) {
      this.rearPercentage = previous.rearPercentage;
    }
    return this.rearPercentage;
  }

  public double getFrontTolerance(ClimbCommandData previous) {
    if (((Double) this.frontTolerance).isNaN()) {
      this.frontTolerance = previous.frontTolerance;
    }
    return this.frontTolerance;
  }

  public double getRearTolerance(ClimbCommandData previous) {
    if (((Double) this.rearTolerance).isNaN()) {
      this.rearTolerance = previous.rearTolerance;
    }
    return this.rearTolerance;
  }

  public double getFrontFeedForward(ClimbCommandData previous) {
    if (((Double) this.frontFeedForward).isNaN()) {
      this.frontFeedForward = previous.frontFeedForward;
    }
    return this.frontFeedForward;
  }

  public double getRearFeedForward(ClimbCommandData previous) {
    if (((Double) this.rearFeedForward).isNaN()) {
      this.rearFeedForward = previous.rearFeedForward;
    }
    return this.rearFeedForward;
  }

  // public ClimbCommandData keepFrontSettings() {
  //   this.frontTolerance = Double.NaN;
  //   this.frontFeedForward = Double.NaN;
  //   return this;
  // }

  // public ClimbCommandData keepRearSettings() {
  //   this.rearTolerance = Double.NaN;
  //   this.rearFeedForward = Double.NaN;
  //   return this;
  // }
}
