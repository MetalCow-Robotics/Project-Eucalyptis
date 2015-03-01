
package org.usfirst.frc.team4213.robot;



import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4213.robot.SlideDrive;
import org.usfirst.frc.team4213.robot.AIRFLOController;
import org.usfirst.frc.team4213.robot.Unit;



public class Robot extends IterativeRobot {
    SlideDrive drivetrain = new SlideDrive();
    CameraServer server;
    
    Compressor compressor = new Compressor();
    
    AIRFLOController driverController = new AIRFLOController(1);
    Joystick operatorController = new Joystick(2);
    
    Grabber grabber = new Grabber();
    Elevator elevator = new Elevator();
    
  
    public Robot() {
        server = CameraServer.getInstance();
        server.setQuality(10);
        server.startAutomaticCapture("cam0");
    	
    }
    
	public void robotInit() {
		drivetrain.gyroInit();
		Timer.delay(0.5);
		drivetrain.zeroGyro();
		compressor.start();
    }
	public void disabledInit() {
		drivetrain.putRequiredDashboardValues();
		elevator.putRequiredDashboardValues();
		MCRSpeedController.putRequiredDashboardValues();
	}
	public void autonomousInit() {		
		drivetrain.zeroGyro();
		drivetrain.putRequiredDashboardValues();
		elevator.putRequiredDashboardValues();
		MCRSpeedController.putRequiredDashboardValues();
	}
	public void teleopInit() {
		drivetrain.putRequiredDashboardValues();
		elevator.putRequiredDashboardValues();
		MCRSpeedController.putRequiredDashboardValues();
	}
    public void autonomousPeriodic() {
    	//drivetrain.regulatedDrive(0.2, 0 , 0);
    }
    
    public void teleopPeriodic() {
    	drivetrain.rawDrive(driverController.getLY(), driverController.getLX(), driverController.getRX(), (driverController.getRawButton(7) || driverController.getRawButton(8)) ? 0.7:0.3, driverController.getButtonToggled(9));
    	
    	/*if (driverController.getRawButton(1))
    		drivetrain.setHeading(180);
    	if (driverController.getRawButton(2))
    		drivetrain.setHeading(90);
    	if (driverController.getRawButton(3))
    		drivetrain.setHeading(-90);
    	if (driverController.getRawButton(4))
    		drivetrain.setHeading(0);*/
    	
    	/*if (driverController.getRawButton(11))
    		drivetrain.zeroGyro();
    	else if (driverController.getHeadingPadPressed())
    		drivetrain.setHeading(driverController.getHeadingPadDirection());*/
    	
    	
    	
    	if (operatorController.getTrigger())
    		grabber.grip();
    	else
    		grabber.release();
    	
    	
    	elevator.rawDrive(operatorController.getY());
    	
    }
}
