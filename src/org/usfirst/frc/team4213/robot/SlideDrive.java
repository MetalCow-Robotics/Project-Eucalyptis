package org.usfirst.frc.team4213.robot;

import com.kauailabs.nav6.frc.IMU;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.Math;

public class SlideDrive {
	CANJaguar leftWheels, rightWheels, strafingWheels;
	
	  SerialPort serial_port;
	  IMU imu;
	  double desiredHeading = 0;
	  boolean wasRegulatingHeading=true;
	
	public SlideDrive(CANJaguar left, CANJaguar right, CANJaguar strafing) {
		leftWheels=left;
		rightWheels=right;
		strafingWheels=strafing;
		
		// Put all of the jags into voltage (scaled from -1 to +1) mode.
		leftWheels.setPercentMode();
		leftWheels.enableControl();
		rightWheels.setPercentMode();
		rightWheels.enableControl();
		strafingWheels.setPercentMode();
		strafingWheels.enableControl();
		
		try {
	    	serial_port = new SerialPort(57600,SerialPort.Port.kOnboard);
			imu = new IMU(serial_port, (byte)50);
    	} catch( Exception ex ) {
    		ex.printStackTrace();
    	}
        if ( imu != null ) {
            LiveWindow.addSensor("IMU", "Gyro", imu);
        }
        
        putRequiredDashboardValues();
	}
	
	public void putRequiredDashboardValues() {
		// Make sure we have our tuning constants on the dashboard if they aren't already there.
        try{SmartDashboard.getNumber("Gyro KP");}
        catch(Exception e){SmartDashboard.putNumber("Gyro KP", 130);}
        try{SmartDashboard.getNumber("Gyro KPE");}
        catch(Exception e){SmartDashboard.putNumber("Gyro KPE", 0.5);}
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
	    
	public void rawDrive(double y, double x, double w, double t) {
		double left = (y+w)*t;
		double right = -(y-w)*t;
		
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
		rawDrive(y,x,w,1);
	}
	
	public void regulatedDrive(double y, double x, double w, double t, boolean fieldOriented) {		
		double currentHeading = imu.getYaw();
		
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
			
			rawDrive(y, 0, Math.copySign(Math.pow(Math.abs(error/SmartDashboard.getNumber("Gyro KP")), SmartDashboard.getNumber("Gyro KE")), error), t);
			
			wasRegulatingHeading = true;
		} else {
			rawDrive(y, 0, w, t);
			
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
}
