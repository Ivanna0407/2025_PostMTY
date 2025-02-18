// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Swerve;
import frc.robot.subsystems.Sub_Swerve;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Cmd_giro extends Command {
  /** Creates a new Cmd_giro. */
  private final Sub_Swerve sub_swerve;
  private final double setpoint;
  double error_p;
  public Cmd_giro(Sub_Swerve sub_Swerve,double setpoint) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.sub_swerve=sub_Swerve;
    this.setpoint=setpoint;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() { 
    double posicion= sub_swerve.getYawrad();
     error_p = setpoint-posicion;
    ChassisSpeeds chassisSpeeds;
    double speed= error_p* Swerve.giro_p;
    chassisSpeeds = new ChassisSpeeds(0,0,speed);
    
    SwerveModuleState[] moduleStates=Swerve.swervekinematics.toSwerveModuleStates(chassisSpeeds);
    sub_swerve.setModuleStates(moduleStates);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (error_p<.5){
      return true;
    }
    else{
      return false;
    }
  }
}
