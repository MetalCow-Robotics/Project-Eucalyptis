package org.usfirst.frc.team4213.robot;
import edu.wpi.first.wpilibj.*;

public class Grabber {
	DoubleSolenoid arm = new DoubleSolenoid(0, 2);
	
	void off(){
		arm.set(DoubleSolenoid.Value.kOff);
	}
	
	void grip(){
		arm.set(DoubleSolenoid.Value.kForward);
	}
	
	void release(){
		arm.set(DoubleSolenoid.Value.kReverse);
	}
}
