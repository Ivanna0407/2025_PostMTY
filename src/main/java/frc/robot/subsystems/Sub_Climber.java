// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Sub_Climber extends SubsystemBase {
  /** Creates a new Sub_Climber. */
  private final SparkMax Motor_Climer = new SparkMax(20, MotorType.kBrushless);
  private final SparkMaxConfig Config_Climber = new SparkMaxConfig();
  public Sub_Climber() {
    Config_Climber.idleMode(IdleMode.kBrake);
    Config_Climber.inverted(false);
    Motor_Climer.configure(Config_Climber, null, null);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setClimber(double speed_climb){
    Motor_Climer.set(speed_climb);
  }

  public double getClimberEncoder(){
    return Motor_Climer.getEncoder().getPosition();
  }
}
