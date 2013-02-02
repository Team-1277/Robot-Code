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
    
    
    
    public static void Process() {
        NetworkTable server = NetworkTable.getTable("SmartDashboard");
        
        try
        {
            
        }
        catch (TableKeyNotDefinedException ex)
        {
            
            ex.printStackTrace();
        }
    }

}
