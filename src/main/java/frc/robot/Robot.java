/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Joystick stick = new Joystick(0);
  private Joystick buttons = new Joystick(1);
  
  private AnalogInput distance1 = new AnalogInput(0);
  private double kValueToInches = 0.125;

  private WPI_TalonSRX leftMaster  = new WPI_TalonSRX(3);
  private WPI_TalonSRX leftSlave   = new WPI_TalonSRX(1);
  private WPI_TalonSRX rightMaster = new WPI_TalonSRX(2);
  private WPI_TalonSRX rightSlave  = new WPI_TalonSRX(0);
  private DifferentialDrive drive  = new DifferentialDrive(leftMaster, rightMaster);

  private WPI_TalonSRX leftBallMotor1  = new WPI_TalonSRX(4);
  private WPI_TalonSRX rightBallMotor1  = new WPI_TalonSRX(5);
  private WPI_TalonSRX leftBallMotor2  = new WPI_TalonSRX(6);
  private WPI_TalonSRX rightBallMotor2 = new WPI_TalonSRX(7);
  private WPI_TalonSRX wheelArm = new WPI_TalonSRX(8);
  private WPI_TalonSRX wheelSpinner = new WPI_TalonSRX(9);
  //private WPI_TalonSRX EXTRA = new WPI_TalonSRX(10);
  private double startTime;
  //private static final double kHoldDistance = 12.0; //These are for Distance Sensors
  //private static final double kValueToInches = 0.125;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster); 
    CameraServer.getInstance();
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    double currentTime = Timer.getFPGATimestamp();
    double time = currentTime - startTime;

    if(time < 2){
      manualDrive(0.5, 0);

    }else if(time < 3){
      manualDrive(0, 0.3);

    }else{
      manualDrive(0, 0);
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    double currentDistance = distance1.getValue() * kValueToInches;
    System.out.println(currentDistance);
    //Drive Train
    final double move = (-stick.getY());
    final double turn = (stick.getZ());
    double speed = 1;
    if(buttons.getRawButton(4)){
      speed = 0.5;
      
    }

    manualDrive(move*speed, turn*0.8*speed);
    //End Drive Train


    //Railgun Commands
    if(buttons.getRawButton(10)){
      startFront(-0.7);
    }else if(buttons.getRawButton(9)){
      startFront(-1);
    }else if(buttons.getRawButton(11)){
      startFront(0);
    }

    if(buttons.getRawButton(1)){
      manualShoot(-0.3);
    }else if(buttons.getRawButton(5)){
      manualShoot(0.3);
    }else{
      manualShoot(0);
    }
    //End Railgun Commands

    //Start Colorwheel Commands Test
    if(buttons.getRawButton(3)){
      wheelSpinner.set(0.3);

    }else if(buttons.getRawButton(7)){
      wheelSpinner.set(0.2);

    }else{
      wheelSpinner.set(0);
    }

    if(buttons.getRawButton(2)){
      wheelArm.set(0.4);
    }else if(buttons.getRawButton(6)){
      wheelArm.set(-0.4);
    }else{
      wheelArm.set(0);
    }

    
    //End Colorwheel Commands Stuff
  }


  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  //Command Functions for the Teleop

  public void manualDrive(double move, double turn) {
    drive.arcadeDrive(move, turn);
    drive.setDeadband(0.05);
  }

  public void manualShoot(final double shoot) {
    leftBallMotor2.set(-shoot); //Port 6
    rightBallMotor2.set(shoot); //Port 7
  }

  public void startFront(final double shoot){
    leftBallMotor1.set(shoot); //Port 4
    rightBallMotor1.set(-shoot); //Port 5
  }
}