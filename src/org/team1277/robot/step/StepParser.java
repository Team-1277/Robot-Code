/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot.step;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.util.ArgsUtilities;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 *
 * @author ben
 */
public class StepParser
{
    public static class Step
    {
        public double rackPosition;
        public double rackAngle;
    }
    
    public static Vector load(String file) throws IOException
    {
        BufferedReader reader = null;
        try
        {
            Vector results = new Vector();
            
            reader = new BufferedReader(new InputStreamReader(Connector.openInputStream("file:" + file)));
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] parts = ArgsUtilities.cut(line);
                if(parts.length != 2)
                {
                    throw new IllegalStateException("Bad line: \"" + line + "\"");
                }
                Step step = new Step();
                step.rackPosition = Double.parseDouble(parts[0]);
                step.rackAngle = Double.parseDouble(parts[1]);
                results.addElement(step);
            }
            return results;
        }
        finally
        {
            if(reader != null)
            {
                reader.close();
            }
        }
    }
}
