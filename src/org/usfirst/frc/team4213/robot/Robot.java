
package org.usfirst.frc.team4213.robot;



import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4213.robot.SlideDrive;
import org.usfirst.frc.team4213.robot.AIRFLOController;

import com.kauailabs.nav6.frc.IMU; 
import com.kauailabs.nav6.frc.IMUAdvanced;

public class Robot extends IterativeRobot {
    SlideDrive drivetrain = new SlideDrive(new Jaguar(1), new Jaguar(2), new Jaguar(3));
	
    AIRFLOController driverController = new AIRFLOController(1);
    
  
    public Robot() {
    	
    	
    }
    
	public void robotInit() {
		drivetrain.gyroInit();
    }
    public void autonomousPeriodic() {
    	drivetrain.regulatedDrive(0.2, 0 , 0);
    }
    
    public void teleopPeriodic() {
	    // Will, FYI, you just needed to import the SmartDashboard. See line 4.
    	
        //drivetrain.rawDrive(driverController.getLY(), driverController.getRawAxis(0), driverController.getRX());
    	drivetrain.regulatedDrive(driverController.getLY(), 0, driverController.getRX());
    	
    	if (driverController.getRawButton(1))
    		drivetrain.setHeading(180);
    	if (driverController.getRawButton(2))
    		drivetrain.setHeading(90);
    	if (driverController.getRawButton(3))
    		drivetrain.setHeading(-90);
    	if (driverController.getRawButton(4))
    		drivetrain.setHeading(0);
    	
    	if (driverController.getRawButton(11))
    		drivetrain.imu.zeroYaw();
    }
    
    /*public void testPeriodic() {
    	SmartDashboard.putNumber("LX", driverController.getLX());
    	SmartDashboard.putNumber("LY", driverController.getLY());
    	SmartDashboard.putNumber("RX", driverController.getRX());
    	SmartDashboard.putNumber("RY", driverController.getRY());
    }*/
}
