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
    public static void setAngle(double inAngleY, double inAngleX) {
        angleY = inAngleY;
        angleX = inAngleX;
        MainRobot.cameraServoY.setAngle(angleY);
        MainRobot.cameraServoX.setAngle(angleX);
    }
    
    public static void updateAngle() {
        if(angleY > 0) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_DOWN))
            {
                angleY--;
                MainRobot.cameraServoY.setAngle(angleY);
            }
        }
        if(angleY < 90) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_UP))
            {
                angleY++;
                MainRobot.cameraServoY.setAngle(angleY);
            }
        }
        if(angleX > 0) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_LEFT))
            {
                angleX--;
                MainRobot.cameraServoX.setAngle(angleX);
            }
        }
        if(angleX < 180) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_RIGHT))
            {
                angleX++;
                MainRobot.cameraServoX.setAngle(angleX);
            }
        }
    }
    public static void nod() {
        MainRobot.cameraServoY.setAngle(90-angleY);
        MainRobot.cameraServoY.setAngle(angleY);
        MainRobot.cameraServoY.setAngle(90-angleY);
   }
        
}