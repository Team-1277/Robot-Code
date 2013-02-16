/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot.step;

import java.io.IOException;
import java.util.Vector;
import org.team1277.robot.step.StepParser.Step;

/**
 *
 * @author ben
 */
public class StepSystem
{
    public static final String STEP_FILE = "steps.txt";
    
    private static StepGenerator generator;
    private static Vector steps;
    
    public static void startTraining()
    {
        generator = new StepGenerator();
        if(!generator.open(STEP_FILE))
        {
            throw new RuntimeException("Can't open " + STEP_FILE);
        }
    }
    
    public static void stopTraining()
    {
        if(generator == null)
        {
            throw new IllegalStateException("Training was never started");
        }
        if(!generator.close())
        {
            throw new RuntimeException("Can't save " + STEP_FILE);
        }
    }
    
    public static Vector loadSteps()
    {
        try
        {
            steps = StepParser.load(STEP_FILE);
            return steps;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Can't load steps: " + e);
        }
    }
    
    public static Step getStep(int index)
    {
        return (Step) steps.elementAt(index);
    }
    
    public static StepIterator steps()
    {
        return new StepIterator(steps);
    }
    
    public static void update(double rackPosition, double rackAngle)
    {
        try {
            generator.writeStep(rackPosition, rackAngle);
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }
}
