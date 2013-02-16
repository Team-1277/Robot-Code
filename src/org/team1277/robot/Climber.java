/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import org.team1277.robot.step.StepSystem;

/**
 *
 * @author roboclub
 */
public class Climber {
    //Rack Constants
    public static final double RACK_RETRACTED_INCHES = 0;
    public static final double RACK_RETRACTED_VOLTS = 0;
    public static final double RACK_EXTENDED_INCHES = 0;
    public static final double RACK_EXTENDED_VOLTS = 0;
    
    //Rack Pivot Constants
    public static final double PIVOT_BACK_DEGREES = 0;
    public static final double PIVOT_BACK_VOLTS = 0;
    public static final double PIVOT_FORWARD_DEGREES = 0;
    public static final double PIVOT_FORWARD_VOLTS = 0;
    
    //Plumbob Pivot Constants
    public static final double PLUMB_BACK_DEGREES = 0;
    public static final double PLUMB_BACK_VOLTS = 0;
    public static final double PLUMB_FORWARD_DEGREES = 0;
    public static final double PLUMB_FORWARD_VOLTS = 0;
    
    //PWMs and Channels
    public static final int RACK_EXTENDER_PWM = 5;
    public static final int RACK_PIVOTER_PWM = 6;
    public static final int RACK_EXTEND_POT_CHNL = 1;
    public static final int RACK_PIVOT_POT_CHNL = 2;
    
    
    public static final boolean LEARNING_MODE = true;
    
    public static boolean climbing = false;
    public static boolean correctToGoal = false;
    public static int stepNum = 0;
    
    public static double rackExtention = 0;
    public static double rackPivot = 0;
    public static double plumbPivot = 0;
    
    //POTs
    public static AnalogChannel rackExtendPOT;
    public static AnalogChannel rackPivotPOT;
    public static AnalogChannel plumbPivotPOT;
    
    
            
    //Motors
    public static Jaguar rackExtender;
    public static Jaguar rackPivoter;
    
    public static boolean atPosition = false;
    public static boolean atLength = false;
    public static boolean atAngle = false;
    
    private static boolean buttonDown;
    
    
    public Climber()
    {
        rackExtendPOT = new AnalogChannel(RACK_EXTEND_POT_CHNL);
        rackPivotPOT = new AnalogChannel(RACK_PIVOT_POT_CHNL);
        plumbPivotPOT = new AnalogChannel(RACK_PIVOT_POT_CHNL);
        
        rackExtender = new Jaguar(RACK_EXTENDER_PWM);
        rackPivoter = new Jaguar(RACK_PIVOTER_PWM);
        
        
        StepSystem.loadSteps();
    }
    
    public static void stop()
    {
        rackExtender.set(0);
        rackPivoter.set(0);
        climbing = false;
        if (LEARNING_MODE)
        {
            StepSystem.stopTraining();
            StepSystem.loadSteps();
        }
    }
    
    public static void reset()
    {
        stepNum = 0;
        climbing = false;
        if (LEARNING_MODE)
        {
            StepSystem.startTraining();
        }
    }
    
    public static void setClimbing(boolean state)
    {
        climbing = state;
    }
    
    public static void setCorrectToGoal(boolean state)
    {
        correctToGoal = state;
    }
    
    public static void update()
    {
        if (!LEARNING_MODE)
        {
            if (MainRobot.rightStick.getRawButton(10))
            {
                if (!buttonDown)
                {
                    buttonDown = true;
                    climbing = !climbing;
                }
            }
            else
            {
                buttonDown = false;
            }

            findPosition();

            if (climbing)
            {

                if (correctToGoal)
                {
                    atLength = false;
                    atAngle = false;
                    if (rackExtention < getLength(stepNum) + 3)
                    {
                        rackExtender.set(.5);
                    }
                    else if (rackExtention < getLength(stepNum) + 2)
                    {
                        rackExtender.set(.2);
                    }
                    else if (rackExtention < getLength(stepNum) + .2)
                    {
                        rackExtender.set(.1);
                    }
                    else if (rackExtention > getLength(stepNum) - 3)
                    {
                        rackExtender.set(.5);
                    }
                    else if (rackExtention > getLength(stepNum) - 2)
                    {
                        rackExtender.set(.2);
                    }
                    else if (rackExtention > getLength(stepNum) - .2)
                    {
                        rackExtender.set(.1);
                    }
                    else
                    {
                        rackExtender.set(0);
                        atLength = true;
                    }

                    /*
                     * Add angle PID;
                     */

                    atPosition = atAngle && atLength;

                }
                else
                {
                    rackExtender.set(0);
                    rackPivoter.set(0);
                }
            }
        }
        else
        {
            findPosition();
            if (MainRobot.rightStick.getRawButton(2))
            {
                if (!buttonDown)
                {
                    buttonDown = true;
                    StepSystem.update(rackExtention, rackPivot);
                }
            }
            else
            {
                buttonDown = false;
            }
            
            double rightJoyY = MainRobot.rightStick.getY();
            double leftJoyY = MainRobot.leftStick.getY();
            rackExtender.set(rightJoyY*.5);//m_rightDrive.set(rightJoyY*MainRobot.driveSpeed);
            rackPivoter.set(-leftJoyY*.5);
        
        }
    }
    
    private static double getAngle(int step)
    {
        return StepSystem.getStep(step).rackAngle;
    }
    
    private static double getLength(int step)
    {
        return StepSystem.getStep(step).rackPosition;
    }
    
    public static void findPosition()
    {
        double rackExtendVolts = rackExtendPOT.getVoltage();
        double rackPivotVolts = rackPivotPOT.getVoltage();
        double plumbPivotVolts = plumbPivotPOT.getVoltage();
        
        
        rackExtention = RACK_RETRACTED_INCHES + (RACK_EXTENDED_INCHES - RACK_RETRACTED_INCHES) * ((rackExtendVolts - RACK_RETRACTED_VOLTS) / (RACK_EXTENDED_VOLTS - RACK_RETRACTED_VOLTS));
        rackPivot = PIVOT_BACK_DEGREES + (PIVOT_FORWARD_DEGREES - PIVOT_BACK_DEGREES) * ((rackPivotVolts - PIVOT_BACK_VOLTS) / (PIVOT_FORWARD_VOLTS - PIVOT_BACK_VOLTS));
        plumbPivot = PLUMB_BACK_DEGREES + (PLUMB_FORWARD_DEGREES - PLUMB_BACK_DEGREES) * ((plumbPivotVolts - PLUMB_BACK_VOLTS) / (PLUMB_FORWARD_VOLTS - PLUMB_BACK_VOLTS));
    }
}
