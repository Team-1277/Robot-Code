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
    
    public static int angle = 0;
    
    public static void updateAngle() {
        if(angle > 0) {
            if(MainRobot.m_leftStick.getRawButton(3))
            {
                angle--;
                MainRobot.cameraServo.setAngle(angle);
            }
        }
        if(angle < 90) {
            if(MainRobot.m_leftStick.getRawButton(2))
            {
                angle++;
                MainRobot.cameraServo.setAngle(angle);
                System.out.println(angle);
            }
        }
        
        }
}