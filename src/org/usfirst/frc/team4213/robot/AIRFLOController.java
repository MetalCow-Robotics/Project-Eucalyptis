package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

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
	
	public boolean getHeadingPadPressed(){
		return getRawButton(1) || getRawButton(2) || getRawButton(3) || getRawButton(4);
	}
	
	public double getHeadingPadDirection(){
		float x=0, y=0;
		if (getRawButton(1)) y-=1;
		if (getRawButton(2)) x+=1;
		if (getRawButton(3)) x-=1;
		if (getRawButton(4)) y+=1;
		return Math.toDegrees(Math.atan2(x, y));
	}
	
	public boolean getButton(int n) {
		return getRawButton(n);
	}
	
	public boolean getButtonToggled(int n) {
		return false;
	}
}
