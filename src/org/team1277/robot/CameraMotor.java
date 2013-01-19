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
    
    public static double angle = 0;
    public static void setAngle(double inAngle) {
        angle = inAngle;
        MainRobot.cameraServo.setAngle(angle);
    }
    
    public static void updateAngle() {
        if(angle > 0) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_UP))
            {
                angle--;
                MainRobot.cameraServo.setAngle(angle);
            }
        }
        if(angle < 90) {
            if(MainRobot.leftStick.getRawButton(MainRobot.BUTTON_CAMERA_SERVO_DOWN))
            {
                angle++;
                MainRobot.cameraServo.setAngle(angle);
            }
        }
        
        }
}