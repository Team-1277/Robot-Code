/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team1277.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;


/**
 *
 * @author roboclub
 */
public class ImageProcessor {
    
    //public static NetworkTable server;
    
    
    
    public static void Process(NetworkTable server) {
        
        
        try
        {
            //System.out.println(server.getNumber("test"));
            if(server.containsKey("IMAGE_COUNT"))
            {
                System.out.println(server.getNumber("IMAGE_COUNT",0.0));
            }
        }
        catch (TableKeyNotDefinedException ex)
        {
            
            ex.printStackTrace();
        }
    }

}
