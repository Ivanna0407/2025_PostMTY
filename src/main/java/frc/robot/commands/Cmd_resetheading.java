// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Sub_LEDs;
import frc.robot.subsystems.Sub_Swerve;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Cmd_resetheading extends Command {
  /** Creates a new Cmd_resetheading. */
  private final Sub_Swerve sub_Swerve;
  private Sub_LEDs leds;
  public Cmd_resetheading(Sub_Swerve sub_Swerve, Sub_LEDs leds) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.sub_Swerve=sub_Swerve;
    this.leds = leds;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {leds.set_water();}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    sub_Swerve.zeroHeading();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
