/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot;

import edu.wpi.first.wpilibj.Relay.Value;

/**
 *
 * @author roboclub
 */
public class BlinkyLight {
    public static boolean state = true;
    public static void update(int loops) {
        if(loops%20==0) {
            if(state) {
                MainRobot.light.set(Value.kOff);
            } else {
                MainRobot.light.set(Value.kOn);
            }
            state = !state;
        }
    }
}
