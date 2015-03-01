package org.usfirst.frc.team4213.robot;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4213.robot.SlideDrive;



public class Elevator {
    Encoder encoder = new Encoder(0,1,false, EncodingType.k4X);
    SpeedController motor = new Talon(4);//, false, false);
    DigitalInput limitSwitch = new DigitalInput(2);
    double setpoint;
    
    void putRequiredDashboardValues() {
    	try{
    		SmartDashboard.getNumber("Elevator Deadband");
    	}catch(Exception e){
    		SmartDashboard.putNumber("Elevator Deadband", 0.05);
    	}
    	try{
    		SmartDashboard.getNumber("Elevator KP");
    	}catch(Exception e){
    		SmartDashboard.putNumber("Elevator KP", 1);
    	}
    	try{
    		SmartDashboard.getNumber("Elevator KE");
    	}catch(Exception e){
    		SmartDashboard.putNumber("Elevator KE", 1);
    	}
    	try{
    		SmartDashboard.getNumber("Elevator Counts Per Inch");
    	}catch(Exception e){
    		SmartDashboard.putNumber("Elevator Counts Per Inch", 1000);
    	}
    	try{
    		SmartDashboard.getNumber("Elevator Max Travel");
    	}catch(Exception e){
    		SmartDashboard.putNumber("Elevator Max Travel", 36);
    	}
    }
    
    void rawDrive(double power){
    	if (limitSwitch.get()){
    		encoder.reset();
    		if (power>SmartDashboard.getNumber("Elevator Deadband"))
    			motor.set(power);
    		else
    			motor.set(0);
    	}else{
    		if (Math.abs(power) > SmartDashboard.getNumber("Elevator Deadband"))
    			motor.set(power);
    		else
    			motor.set(0);
    	}
    }
    
    void drive(double power){
    	setpoint = encoder.get()/SmartDashboard.getNumber("Elevator Counts Per Inch");
    	
    	rawDrive(power);
    }
    
    void setPosition(Unit amount){
    	try{
    		setpoint = amount.convertTo("inches").amount;
    	}catch(Exception e){}
    	
    	if (setpoint > SmartDashboard.getNumber("Elevator Max Travel")) setpoint = SmartDashboard.getNumber("Elevator Max Travel");
    	if (setpoint < 0.25) setpoint = 0;
    }
    
    void incrementPosition(Unit amount){
    	try{
    		setpoint = amount.convertTo("inches").add(new Unit(this.setpoint, "inches")).amount;	
    	}catch(Exception e){}
    	
    	if (setpoint > SmartDashboard.getNumber("Elevator Max Travel")) setpoint = SmartDashboard.getNumber("Elevator Max Travel");
    	if (setpoint < 0.25) setpoint = 0;
    }
    
    void regulatePosition(){
    	double error = setpoint*SmartDashboard.getNumber("Elevator Counts Per Inch") - encoder.get();
    	
    	rawDrive(Math.copySign(Math.pow(Math.abs(error*SmartDashboard.getNumber("Elevator KP")), SmartDashboard.getNumber("Elevator KE")), error));
    }
}
