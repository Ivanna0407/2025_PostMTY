// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Subsitemas;

public class Sub_Algas extends SubsystemBase {
  /** Creates a new Sub_Algas. */
  private final SparkMax Motor_BrazoR = new SparkMax(17, MotorType.kBrushless);
  private final SparkMax Motor_BrazoL = new SparkMax(18, MotorType.kBrushless);
  private final SparkMax Motor_Alga = new SparkMax(19, MotorType.kBrushless);
  private final SparkMaxConfig Config_Brazo = new SparkMaxConfig();
  private final SparkMaxConfig Config_Alga = new SparkMaxConfig();
  public Sub_Algas() {
    //Config Motor brazo 
    Config_Brazo.idleMode(IdleMode.kBrake);
    Config_Brazo.inverted(false);
    Motor_BrazoL.configure(Config_Brazo, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    Motor_BrazoR.configure(Config_Brazo, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    //Config motor Intake alga 
    Config_Alga.idleMode(IdleMode.kBrake);
    Config_Alga.inverted(false);
    Motor_Alga.configure(Config_Alga, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }

  public void set_Brazo(double speed_right, double speed_left){
    if(Motor_BrazoL.getEncoder().getPosition()<0 && speed_left>0){
      speed_left=0;
      speed_right=0;
    }
    Motor_BrazoR.set(speed_right);
    Motor_BrazoL.set(-speed_left);
  }
  public void set_Alga(double in){
    Motor_Alga.set(in);
  }

  public double getEncoderBrazo(){
    return Motor_BrazoR.getEncoder().getPosition()*Subsitemas.conversion_brazo;// Se necesita la conversión
  }

  public double getAlgaCurrent(){
    return Motor_Alga.getOutputCurrent();
  }
}
