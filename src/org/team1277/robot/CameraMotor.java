/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot;

/**
 *
 * @author roboclub
 */
public class CameraMotor {
    
    public static double angleY = 0;
    public static double angleX = 0;
    public static double speed = 1d;
    
    public static void setAngle(double inAngleY, double inAngleX) {
        angleY = inAngleY;
        angleX = inAngleX;
        MainRobot.cameraServoY.setAngle(angleY);
        MainRobot.cameraServoX.setAngle(angleX);
    }
    
    public static void updateAngle() { 
        speed = MainRobot.leftStick.getThrottle() + 1d;
        System.out.println(speed);
        if(angleY > 0) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_DOWN))
            {
                angleY-=speed;
                MainRobot.cameraServoY.setAngle(angleY);
            }
        }
        if(angleY < 90) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_UP))
            {
                angleY+=speed;
                MainRobot.cameraServoY.setAngle(angleY);
            }
        }
        if(angleX > 0) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_LEFT))
            {
                angleX-=speed;
                MainRobot.cameraServoX.setAngle(angleX);
            }
        }
        if(angleX < 180) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_RIGHT))
            {
                angleX+=speed;
                MainRobot.cameraServoX.setAngle(angleX);
            }
        }
    }
        
}