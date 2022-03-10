package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  
  private final CANSparkMax shooter1 = new CANSparkMax(frc.robot.constants.robotmap.motor.Shooter.RIGHT, MotorType.kBrushless);
  private final CANSparkMax shooter2 = new CANSparkMax(frc.robot.constants.robotmap.motor.Shooter.LEFT, MotorType.kBrushless);
  private final VictorSPX feeder = new VictorSPX(frc.robot.constants.robotmap.motor.Storage.FEEDER);

  public Shooter() {
    shooter1.setIdleMode(IdleMode.kCoast);
    shooter2.setIdleMode(IdleMode.kCoast);
  }
  
  /**
   * Sets the power of the shooter wheels.
   * @param power The power value the wheels will be set to.
   * @author Lucas Brunner
   */
  public void spin(double power)
  {
    shooter1.set(-power);
    shooter2.set(+power);
  }

  public void spinFeeder(double power) {
    feeder.set(ControlMode.PercentOutput, -power);
  }
  
  /**
   * Sets the power of the shooter wheels to 0.
   * @author Lucas Brunner
   */
  public void stop()
  {
    shooter1.set(0);
    shooter2.set(0);
  }
  
  /**
   * @return The rpm of the shooter wheels.
   * @author Lucas Brunner
   */
  public double getVelocity()
  {
    return -shooter1.getEncoder().getVelocity();
  }
}
