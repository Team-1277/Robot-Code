/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team1277.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainRobot extends IterativeRobot {
    
    //Constants
    public static final int ARCADE_JOYSTICK = 2; //1 = left stick 2 = right stick
    
    //PWM Constants
    static final int PWM_rightDrivePort = 1;
    static final int PWM_leftDrivePort = 2;
    static final int PWM_cameraServo = 3;
    
    //Joystick Buttons
    static final int BUTTON_CAMERA_SERVO_UP = 3;
    static final int BUTTON_CAMERA_SERVO_DOWN = 2;
    
    //Joystick Variables
    public static Joystick rightStick;			// joystick 1 (arcade stick or right tank stick)
    public static Joystick leftStick;                       // joystick 2 (arcade stick or left tank stick)
    static final int NUM_JOYSTICK_BUTTONS = 12;
    boolean[] m_rightStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS + 1)];
    boolean[] m_leftStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS + 1)];
    
    // Local variables to count the number of periodic loops performed
    public static int autoPeriodicLoops;
    public static int disabledPeriodicLoops;
    public static int telePeriodicLoops;
        
    //Motor Variables
    public static Victor rightDrive;
    public static Victor leftDrive;
    
    public static Jaguar testJag;
    
    //Variables
    public static int driveMode; //1=Tankdrive 2=ArcadeDrive
    public static double driveSpeed; //speed modifier for the drive 0.0-1.0
    
    //Camera Junk
    public static Servo cameraServo;
    
    //public static final String eatItNick = "CAMEL CASE FTW";
    
    /********************************** Constructor **************************************************/
    
    public MainRobot() {
        // Initialize counters to record the number of loops completed in autonomous and teleop modes
        autoPeriodicLoops = 0;
        disabledPeriodicLoops = 0;
        telePeriodicLoops = 0;
        
        //Initalize jaguars
        testJag = new Jaguar(PWM_rightDrivePort);
        //m_rightDrive = new Victor(PWM_rightDrivePort);
        leftDrive = new Victor(PWM_leftDrivePort);
        
        cameraServo = new Servo(PWM_cameraServo);
        
        //Init joysticks (You can change the ports in the driver station gui)
        rightStick = new Joystick(1); //port 1
        leftStick = new Joystick(2); //port 2
        
        // Iterate over all the buttons on each joystick, setting state to false for each
        int buttonNum;						// start counting buttons at button 1
        for (buttonNum = 1; buttonNum <= NUM_JOYSTICK_BUTTONS; buttonNum++) {
            m_rightStickButtonState[buttonNum] = false;
            m_leftStickButtonState[buttonNum] = false;
        }
        
    }
    
    
    /********************************** Initialization Routines *************************************/
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void robotInit() {
        System.out.println("Initializing robot....");
        
        driveMode = 1; //Tank Drive
        driveSpeed = .25;
        
        System.out.println("Initialization done....");
    }

    /**
     * This function is called when the robot is disabled
     */
    public void disabledInit() {
        System.out.println("Tele-op deactivated");
        disabledPeriodicLoops = 0;
        
        //stop motors
        //rightDrive.set(0);
        leftDrive.set(0);
        testJag.set(0);
    }
    
    /**
     * This function is called when autonomous is started
     */
    public void autonomousInit() {
        System.out.println("autonomous activated...");
        autoPeriodicLoops = 0;
        CameraMotor.setAngle(90);
    }
    
    /**
     * This function is called when tele-op is started
     */
    public void teleopInit() {
        System.out.println("Tele-op activated...");
        telePeriodicLoops = 0;
        
        //reset motors
        //rightDrive.set(0);
        leftDrive.set(0);
        CameraMotor.setAngle(90);
    }
    
    /********************************** Periodic Routines *************************************/
    
    /*
     * This function is called periodically when the robot is disabled
     */
    public void disabledPeriodic() {
        // feed the user watchdog at every period when disabled
        Watchdog.getInstance().feed();
        // add to the loop count
        disabledPeriodicLoops++;
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        // feed the user watchdog at every period when autonomous is enabled
        Watchdog.getInstance().feed();
        // add to the loop count
        autoPeriodicLoops++;
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // feed the user watchdog at every period when tele-op is enabled
        Watchdog.getInstance().feed();
        // add to the loop count
        telePeriodicLoops++;
        //Main drive train
        //testJag.set(-.1);
        //m_leftDrive.set(.1);
        //System.out.println(testJag.get());
        DriveTrain.updateDrive(driveMode);
        CameraMotor.updateAngle();
    }
}
