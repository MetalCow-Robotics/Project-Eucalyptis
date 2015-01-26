
package org.usfirst.frc.team4213.robot;



import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4213.robot.SlideDrive;
import org.usfirst.frc.team4213.robot.AIRFLOController;

public class Robot extends IterativeRobot {
    SlideDrive drivetrain = new SlideDrive(new CANJaguar(5), new CANJaguar(2), new CANJaguar(3));
    CameraServer server;
	
    AIRFLOController driverController = new AIRFLOController(1);
    
  
    public Robot() {
        server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
    	
    }
    
	public void robotInit() {
		drivetrain.gyroInit();
		drivetrain.zeroGyro();
    }
	public void disabledInit() {
		drivetrain.putRequiredDashboardValues();
	}
	public void autonomousInit() {
		drivetrain.putRequiredDashboardValues();
	}
	public void teleopInit() {
		drivetrain.putRequiredDashboardValues();
	}
    public void autonomousPeriodic() {
    	drivetrain.regulatedDrive(0.2, 0 , 0);
    }
    
    public void teleopPeriodic() {
    	drivetrain.regulatedDrive(driverController.getLY(), driverController.getLX(), driverController.getRX(), (driverController.getRawButton(7) || driverController.getRawButton(8)) ? 1:0.5, true);
    	
    	/*if (driverController.getRawButton(1))
    		drivetrain.setHeading(180);
    	if (driverController.getRawButton(2))
    		drivetrain.setHeading(90);
    	if (driverController.getRawButton(3))
    		drivetrain.setHeading(-90);
    	if (driverController.getRawButton(4))
    		drivetrain.setHeading(0);*/
    	
    	if (driverController.getRawButton(11))
    		drivetrain.zeroGyro();
    	else if (driverController.getHeadingPadPressed())
    		drivetrain.setHeading(driverController.getHeadingPadDirection());
    }
}
