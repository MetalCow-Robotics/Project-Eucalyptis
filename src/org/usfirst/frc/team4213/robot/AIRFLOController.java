package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.Joystick;

public class AIRFLOController extends Joystick {
	public AIRFLOController (int port) {
		super(port);
	}
	
	public double getLY(){
		return -getRawAxis(1);
	}
	
	public double getLX(){
		return getRawAxis(0);
	}
	
	public double getRY() {
		return -getRawAxis(2);
	}
	public double getRX(){
		return getRawAxis(3);
	}
}
