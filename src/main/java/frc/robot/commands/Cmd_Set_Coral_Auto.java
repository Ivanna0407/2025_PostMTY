// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.io.Serial;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Sub_Elevador;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Cmd_Set_Coral_Auto extends Command {
  /** Creates a new Cmd_Set_Coral_Auto. */
  private final Sub_Elevador Elevador;
  double speed;
  public Cmd_Set_Coral_Auto(Sub_Elevador elevador, double _speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.Elevador=elevador;
    addRequirements(elevador);
    speed = _speed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("initComand");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Elevador.set_Coral(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("initComand");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Elevador.get_Speed_Coral()==speed);
  }
}
