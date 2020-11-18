/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
// import java.lang.System;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.lang.Math; 
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Robot extends TimedRobot {
  private NetworkTable m_limelight;
	private NetworkTableEntry m_pipeline;
	private double m_rightCommand;
	private double m_leftCommand;
  private DifferentialDrive m_myRobot;
  private Joystick m_stick;
  public Talon m_front_left, m_front_right, m_back_left, m_back_right, m_leftShoot, m_rightShoot, m_kick;
  public SpeedController left_motors, right_motors;

  @Override
  public void robotInit() {
    m_front_left = new Talon(2);
    m_back_left = new Talon(3);
    m_front_right = new Talon(0);
    m_back_right = new Talon(1);
    // m_leftShoot = new Talon(4);
    // m_rightShoot = new Talon(1);
    // m_kick = new Talon(7);
    left_motors = new SpeedControllerGroup(m_front_left, m_back_left);
    right_motors = new SpeedControllerGroup(m_front_right, m_back_right);
    m_myRobot = new DifferentialDrive(right_motors, left_motors);
    m_stick = new Joystick(0);
    m_limelight = NetworkTableInstance.getDefault().getTable("limelight");
		m_pipeline = m_limelight.getEntry("pipeline");
		m_pipeline.setNumber(2);
		m_rightCommand = 0.0;
		m_leftCommand = 0.0;
  }

  @Override
  public void teleopPeriodic() {
    boolean m_target = m_stick.getRawButton(4);
    double tx = m_limelight.getEntry("tx").getDouble(0);
		double ty = m_limelight.getEntry("ty").getDouble(0);
		double driveAdjust = ty * 0.2;
		double aimAdjust = tx * 55;
		m_rightCommand += driveAdjust - aimAdjust;
		m_leftCommand += driveAdjust + aimAdjust;
    if (m_target) {
      m_myRobot.tankDrive(m_leftCommand, m_rightCommand);
    }
		
    // boolean m_shootLow = m_stick.getRawButton(7);
    // boolean m_shootMed1 = m_stick.getRawButton(8);
    // boolean m_shootMed2 = m_stick.getRawButton(5);
    // boolean m_shootHigh = m_stick.getRawButton(6);
    // boolean m_intake = m_stick.getRawButton(3);
    // boolean m_doKick = m_stick.getRawButton(4);
    // if (m_shootLow) {
    //   m_leftShoot.set(-0.5);
    //   m_rightShoot.set(0.5);
    // } else if (m_shootMed1) {
    //   m_leftShoot.set(-0.65);
    //   m_rightShoot.set(0.65);
    // } else if (m_shootMed2) {
    //   m_leftShoot.set(-0.8);
    //   m_rightShoot.set(0.8);
    // } else if (m_shootHigh) {
    //   m_leftShoot.set(-1);
    //   m_rightShoot.set(1);
    // } else if (m_intake) {
    //   m_leftShoot.set(0.4);
    //   m_rightShoot.set(-0.4);
    // } else {
    //   m_leftShoot.set(0);
    //   m_rightShoot.set(0);
    //   m_kick.set(0.1);
    // }
    // if (m_doKick) {
    //   m_kick.set(-0.5);
    // }
    // m_myRobot.tankDrive(m_stick.getRawAxis(5), m_stick.getRawAxis(1));
    // m_myRobot.tankDrive(0.5, 0.5);
    //m_circle.whenPressed(new RunCommand(()->m_myRobot.tankDrive(0.25,-0.25)));

  }
}
