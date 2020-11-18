/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Robot extends TimedRobot {
  // DECLARING ALL VARIABLES

  // FIRST: LIMLIGHT
  private NetworkTable m_limelight;  
  private NetworkTableEntry m_pipeline;
	private double m_rightCommand; // THE VALUE TO ADJUST THE RIGHT
  private double m_leftCommand; // THE VALUE TO ADJUST THE LEFT

  // OUR ROBOT OBJECT
  private DifferentialDrive m_myRobot;

  // JOYSTICK
  private Joystick m_stick;

  // MOTOR CONTROLLERS
  public Talon m_front_left, m_front_right, m_back_left, m_back_right, m_leftShoot, m_rightShoot, m_kick;
  public SpeedController left_motors, right_motors; // GROUPING SIDES OF ROBOT

  @Override
  public void robotInit() {

    // INITIALIZING THE DRIVE TRAIN
    m_front_left = new Talon(2); // Front left motor on index 2...
    m_back_left = new Talon(3); // Back left motor on index 3...
    m_front_right = new Talon(0);
    m_back_right = new Talon(1);

    // DUKES SHOOTER
    // m_leftShoot = new Talon(4);
    // m_rightShoot = new Talon(1);
    // m_kick = new Talon(7);

    // GROUPING THE SIDES
    left_motors = new SpeedControllerGroup(m_front_left, m_back_left);
    right_motors = new SpeedControllerGroup(m_front_right, m_back_right);
    // ROBOT OBJECT WITH THE TWO SIDES
    m_myRobot = new DifferentialDrive(right_motors, left_motors);

    // JOYSTICK AT INDEX 0
    m_stick = new Joystick(0);
    
    // WHERE TO GET THE VALUES OF THE LIMELIGHT FROM
    m_limelight = NetworkTableInstance.getDefault().getTable("limelight");
    // OUR UNIQUE PIPELINE WITH THE RIGHT SETTINGS: 2
		m_pipeline = m_limelight.getEntry("pipeline");
    m_pipeline.setNumber(2);
    // START WITH NO VISION ADJUSTMENT
		m_rightCommand = 0.0;
		m_leftCommand = 0.0;
  }

  @Override
  public void teleopPeriodic() {
    // START TRACKING BUTTON: SQUARE?
    boolean m_target = m_stick.getRawButton(4);
    // THE DISTANCE FROM THE CROSSHAIR OF THE CENTER OF THE TARGET: X
    double tx = m_limelight.getEntry("tx").getDouble(0);
    // THE DISTANCE FROM THE CROSSHAIR OF THE CENTER OF THE TARGET: Y
    double ty = m_limelight.getEntry("ty").getDouble(0);
    // BOLLEAN, 0 or 1, IF THE LIMELIHGT SEES A TARGET
    double tv = m_limelight.getEntry("tv").getDouble(0);

    // USING ARCADE DRIVE
		double driveAdjust = ty * 0.2; // FORWARD BACKWARD
    double aimAdjust = tx * 55; // TURNING
    
    // USING TANK DRIVE
		// m_rightCommand += 0.2*driveAdjust - aimAdjust;
    // m_leftCommand += 0.2*driveAdjust + aimAdjust;
    
    if (m_target && tv == 1) { // CHECK FOR TARGETS AND TARGET BUTTON PUSHED
      // m_myRobot.tankDrive(m_leftCommand, m_rightCommand); // TANK DRIVE

      m_myRobot.arcadeDrive(driveAdjust, aimAdjust); // ARCADE DRIVE
    } else {
      m_myRobot.tankDrive(-0.3, 0.3); // IF NO TARGETS, TURN UNTIL FOUND
    }

		// DUKE COMMANDS
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
