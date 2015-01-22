
package org.usfirst.frc.team4213.robot;



import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4213.robot.SlideDrive;
import org.usfirst.frc.team4213.robot.AIRFLOController;

public class Robot extends IterativeRobot {
    SlideDrive drivetrain = new SlideDrive(new CANJaguar(5), new CANJaguar(2), new CANJaguar(3));
	
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
    	drivetrain.regulatedDrive(driverController.getLY(), 0, driverController.getRX(), (driverController.getRawButton(7) || driverController.getRawButton(8)) ? 1:0.5);
    	
    	if (driverController.getRawButton(1))
    		drivetrain.setHeading(180);
    	if (driverController.getRawButton(2))
    		drivetrain.setHeading(90);
    	if (driverController.getRawButton(3))
    		drivetrain.setHeading(-90);
    	if (driverController.getRawButton(4))
    		drivetrain.setHeading(0);
    	
    	if (driverController.getRawButton(11))
    		drivetrain.zeroGyro();
    }
}
