package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MCRSpeedController extends Victor {
	boolean ramp=false, invert=false;
	//static double maxRampAmount=1.0/25.0;
	double lastSpeed;
	public MCRSpeedController(int port, boolean ramp, boolean invert) {
		super(port);
		this.ramp = ramp;
		this.invert = invert;
	}
	
	static void putRequiredDashboardValues(){
		try{
			SmartDashboard.getNumber("rampTime");
		} catch(Exception e){
			SmartDashboard.putNumber("rampTime", 0.5);
		}
	}

	public void set(double amount){
		double change = amount-lastSpeed;
		double maxRampAmount = SmartDashboard.getNumber("rampTime")/20;
		if (Math.abs(change) < maxRampAmount || !ramp) {
			super.set((invert?-1:1) * amount);
			lastSpeed = amount;
		} else {
			if (change > 0)
				change = maxRampAmount;
			else
				change = -maxRampAmount;
			super.set((invert?-1:1) * (lastSpeed + change));
			lastSpeed += change;
		}
	}
	public void reset(){
		super.set(0);
		lastSpeed = 0;
	}
}
