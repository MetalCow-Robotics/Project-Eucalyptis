package org.usfirst.frc.team4213.robot;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;

public class SlideDrive {
	MCRSpeedController leftWheels = new MCRSpeedController(1,true,false);
	MCRSpeedController rightWheels = new MCRSpeedController(2,true,true);
	MCRSpeedController strafingWheels = new MCRSpeedController(3,true,false);
	
	  SerialPort serial_port = new SerialPort(57600,SerialPort.Port.kOnboard);
	  IMUAdvanced imu;
	  double desiredHeading = 0;
	  boolean wasRegulatingHeading=true;
	  double velocity=0, position=0;
	  long lastTime=0;
	
	public SlideDrive() {
		
		// Put all of the jags into voltage (scaled from -1 to +1) mode.
		/*leftWheels.setPercentMode();
		leftWheels.enableControl();
		rightWheels.setPercentMode();
		rightWheels.enableControl();
		strafingWheels.setPercentMode();
		strafingWheels.enableControl();*/
		
		try {
			imu = new IMUAdvanced(serial_port, (byte)50);
    	} catch( Exception ex ) {
    		ex.printStackTrace();
    	}
        
        putRequiredDashboardValues();
	}
	
	public void putRequiredDashboardValues() {
		// Make sure we have our tuning constants on the dashboard if they aren't already there.
        try{SmartDashboard.getNumber("Gyro KP");}
        catch(Exception e){SmartDashboard.putNumber("Gyro KP", 130);}
        try{SmartDashboard.getNumber("Gyro KE");}
        catch(Exception e){SmartDashboard.putNumber("Gyro KE", 0.5);}
	}

	
	public void gyroInit() {
		boolean is_calibrating = imu.isCalibrating();
        if (!is_calibrating ) {
            Timer.delay( 0.3 );
            imu.zeroYaw();
        }
	}
	
	public void zeroGyro() {
		imu.zeroYaw();
		desiredHeading = 0;
	}
	
	public void smartZero() {
		
		if (imu.getYaw() > -45 && imu.getYaw() < 45){
			imu.zeroYaw();
			imu.user_yaw_offset += 0;
			desiredHeading = 0;
		} else if (imu.getYaw() > 45 && imu.getYaw() < 135){
			imu.zeroYaw();
			imu.user_yaw_offset += 90;
			desiredHeading = 90;
		} else if (imu.getYaw() > -135 && imu.getYaw() < -45){
			imu.zeroYaw();
			imu.user_yaw_offset -= 90;
			desiredHeading = -90;
		} else {
			imu.zeroYaw();
			imu.user_yaw_offset += 180;
			desiredHeading = 180;
		}
		
		
		
	}
	    
	public void rawDrive(double y, double x, double w, double t, boolean throttleW) {
		/*if (lastTime==0)
			lastTime=System.currentTimeMillis();
		long elapsedTime = System.currentTimeMillis()-lastTime;
		lastTime=System.currentTimeMillis();
		velocity+=imu.getWorldLinearAccelX()*elapsedTime;
		position+=velocity*elapsedTime;
		
		SmartDashboard.putNumber("Velocity", velocity);
		SmartDashboard.putNumber("Position", position);*/
		
		
		double left=(y*t+w), right=-(y*t-w);
		if (throttleW) {
			left = (y+w)*t;
			right = (y-w)*t;
		}
		
		if (Math.abs(left) > Math.abs(right)) {
			if (Math.abs(left) > t) {
				
				right = right/Math.abs(left) * t;
				left = Math.copySign(t, left);
			}
		} else {
			if (Math.abs(right) > t) {
				left = left/Math.abs(right) * t;
				right = Math.copySign(t, right);
			}
		}
		
		leftWheels.set(left);
		rightWheels.set(right);
		strafingWheels.set(x);
	}
	
	public void rawDrive(double y, double x, double w) {
		rawDrive(y,x,w,1,true);
	}
	
	public void regulatedDrive(double y, double x, double w, double t, boolean fieldOriented) {		
		double currentHeading = imu.getYaw();
		
		SmartDashboard.putBoolean("Field Oriented Drive Enabled", fieldOriented);
		if (fieldOriented) {
			double m = Math.sqrt(y*y+x*x);
			double h = Math.atan2(x, y)-Math.toRadians(currentHeading);
			
			
			
			//if (h>Math.PI/2) h-=Math.PI;
			//if (h<-Math.PI/2) h+=Math.PI;
			
			SmartDashboard.putNumber("FOH_h", Math.toDegrees(h));
			
			x=Math.sin(h)*m;
			y=Math.cos(h)*m;
			SmartDashboard.putNumber("FOH_x", x);
			SmartDashboard.putNumber("FOH_y", y);
		}
		
		if (w < 0.07 && w > -0.07) {
			SmartDashboard.putNumber(   "Robot Heading",       currentHeading       );
			
			if (!wasRegulatingHeading)
				desiredHeading=currentHeading;
			
			double error = desiredHeading-currentHeading;
			
			if (error > 180)
				error -= 360;
			else if (error < -180)
				error += 360;
			
			SmartDashboard.putNumber("Robot Heading Error", error);
			
			rawDrive(y, 0, Math.copySign(Math.pow(Math.abs(error/SmartDashboard.getNumber("Gyro KP")), SmartDashboard.getNumber("Gyro KE")), error), t, false);
			
			wasRegulatingHeading = true;
		} else {
			rawDrive(y, 0, w, t, true);
			
			wasRegulatingHeading = false;
		}
	}
	public void regulatedDrive(double y, double x, double w, double t){
		regulatedDrive(y,x,w,t, false);
	}
	public void regulatedDrive(double y, double x, double w) {
		regulatedDrive(y,x,w,1, false);
	}
	
	public void setHeading(double heading) {
		desiredHeading = heading;
	}
	
	public void stop(){
		rawDrive(0,0,0,0,true);
	}
}
