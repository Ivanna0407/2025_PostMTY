// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class Cmd_Wait extends Command {
  /** Creates a new Cmd_Wait. */
  private final double seconds;
  boolean ending=false;
  public Cmd_Wait(double seconds) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.seconds=seconds;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Timer.delay(1);
    ending=true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (ending==true){
      return true;
    }
    else{
      return false;
    }
}
}