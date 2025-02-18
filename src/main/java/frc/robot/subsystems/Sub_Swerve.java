// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Swerve;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

public class Sub_Swerve extends SubsystemBase {
  //En este subsistema se unen los 4 modulos y el giroscopio 
  private final Sub_Modulo Modulo_1 = new Sub_Modulo(3, 4, true, true, 10, false);
  private final Sub_Modulo Modulo_2 = new Sub_Modulo(1, 2, true, true, 9,  false);
  private final Sub_Modulo Modulo_3 = new Sub_Modulo(5, 6, true, true, 11,  false);
  private final Sub_Modulo Modulo_4 = new Sub_Modulo(7, 8, false, true, 12 , false);
  private final AHRS gyro = new AHRS(NavXComType.kMXP_SPI);
  private final Pigeon2 Pigeon= new Pigeon2(13);
  private final StructArrayPublisher<SwerveModuleState> publisher;
  private final SwerveDriveOdometry odometry= new SwerveDriveOdometry(Swerve.swervekinematics,gyro.getRotation2d(), getModulePositions());
  private Field2d field= new Field2d();
  

  public Sub_Swerve() {
    new Thread(()->{try {Thread.sleep(1000); zeroHeading();}catch(Exception e ){}}).start();
   // CameraServer.startAutomaticCapture("Sprite_Cam",0);
   
    
    publisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("/SwerveStates", SwerveModuleState.struct).publish(); 
      
     try{
      RobotConfig config = RobotConfig.fromGUISettings();

      // Configure AutoBuilder
      AutoBuilder.configure(
        this::getPose, 
        this::resetPose, 
        this::getChassisSpeeds, 
        this::driveRobotRelative, 
        new PPHolonomicDriveController(
          new PIDConstants(5,0,0),
          new PIDConstants(.26,0.0,0)
        ),
        config,
        () -> {
            // Boolean supplier that controls when the path will be mirrored for the red alliance
            // This will flip the path being followed to the red side of the field.
            // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

            var alliance = DriverStation.getAlliance();
            if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
            }
            return false;
        },
        this
      );
    }catch(Exception e){
      DriverStation.reportError("Failed to load PathPlanner config and configure AutoBuilder", e.getStackTrace());
    }

    // Set up custom logging to add the current path to a field 2d widget
    PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));
    

    SmartDashboard.putData("Field", field);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Heading", getHeadding());
    SmartDashboard.putNumber("Pigeon heading", getHead());
    publisher.set(new SwerveModuleState[]{Modulo_1.getState(),Modulo_2.getState(),Modulo_3.getState(),Modulo_4.getState()}); 
    field.setRobotPose(getPose());  
    SmartDashboard.putNumber("AmperajeTM1", Modulo_1.getcorrienteturn());
    SmartDashboard.putNumber("AmperajeTM2", Modulo_2.getcorrienteturn());
    SmartDashboard.putNumber("AmperajeTM3", Modulo_3.getcorrienteturn());
    SmartDashboard.putNumber("AmperajeTM4", Modulo_4.getcorrienteturn());
    SmartDashboard.putNumber("AmperajeDM1", Modulo_1.getcorrientedrive());
    SmartDashboard.putNumber("AmperajeDM2", Modulo_2.getcorrientedrive());
    SmartDashboard.putNumber("AmperajeDM3", Modulo_3.getcorrientedrive());
    SmartDashboard.putNumber("AmperajeDM4", Modulo_4.getcorrientedrive());
  }

  public void zeroHeading(){
    gyro.reset();
  }

  public double getYawrad(){
    return gyro.getYaw()*(Math.PI/180);
  }

  public double getHeadding(){
    return Pigeon.getYaw().getValueAsDouble()%360;
  }

  public double getHead(){
    return Math.IEEEremainder(Pigeon.getYaw().getValueAsDouble(),360);
  }
  public void resetHead(){
    Pigeon.reset();
  }

  public Rotation2d get2Drotation(){
    //Permite cambiar de angulos a un objeto de Rotation 2D
    return Rotation2d.fromDegrees(getHeadding());
  }

  public Rotation2d get2DPigeonRotation(){
    return Pigeon.getRotation2d();
  }

  public Pose2d getPose(){
    return odometry.getPoseMeters();
  }

  public void resetPose(Pose2d pose2d){
    odometry.resetPosition(get2Drotation(), getModulePositions(), pose2d);
  }

  public ChassisSpeeds getChassisSpeeds(){
    return Swerve.swervekinematics.toChassisSpeeds(getModuleStates());
  }

  
  public void stopModules(){
    Modulo_1.alto();
    Modulo_2.alto();
    Modulo_3.alto();
    Modulo_4.alto();
  }

  public void setModuleStates(SwerveModuleState[] desiredModuleStates){
    //Se genera un arreglo de swerve module state para poder mandarlos a los diferentes modulos de acuerdo a posición 
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredModuleStates, 4);// velocidad
    Modulo_1.setDesiredState(desiredModuleStates[0]);
    Modulo_2.setDesiredState(desiredModuleStates[1]);
    Modulo_3.setDesiredState(desiredModuleStates[2]);
    Modulo_4.setDesiredState(desiredModuleStates[3]);
  }

  public void driveRobotRelative(ChassisSpeeds robotRelativeSpeeds) {
    ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(robotRelativeSpeeds, 0.02);

    SwerveModuleState[] targetStates = Swerve.swervekinematics.toSwerveModuleStates(targetSpeeds);
    setModuleStates(targetStates);
  }

  public void resetAllEncoders(){
    Modulo_1.resetEncoders();
    Modulo_2.resetEncoders();
    Modulo_3.resetEncoders();
    Modulo_4.resetEncoders();
  }

  public void setSpecificState(SwerveModuleState specificState,SwerveModuleState specificState_1,SwerveModuleState specificState_2,SwerveModuleState specificState_3){
    Modulo_1.setDesiredState(specificState);
    Modulo_2.setDesiredState(specificState_1);
    Modulo_3.setDesiredState(specificState_2);
    Modulo_4.setDesiredState(specificState_3);
  }

  public SwerveModulePosition[] getModulePositions(){
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    positions[0]=Modulo_1.getModulePosition();
    positions[1]=Modulo_2.getModulePosition();
    positions[2]=Modulo_3.getModulePosition();
    positions[3]=Modulo_4.getModulePosition();
    return positions;
    }

    public SwerveModuleState[] getModuleStates(){
    SwerveModuleState[] positions = new SwerveModuleState[4];
    positions[0]=Modulo_1.getState();
    positions[1]=Modulo_2.getState();
    positions[2]=Modulo_3.getState();
    positions[3]=Modulo_4.getState();
    return positions;
    }
  }