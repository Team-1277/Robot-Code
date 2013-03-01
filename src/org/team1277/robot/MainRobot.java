/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team1277.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    static final int PWM_cameraYServo = 3;
    static final int PWM_cameraXServo = 4;
    
    //Joystick Buttons
    static final int BUTTON_CAMERA_SERVO_UP = 3;
    static final int BUTTON_CAMERA_SERVO_DOWN = 2;
    static final int BUTTON_CAMERA_SERVO_LEFT = 4;
    static final int BUTTON_CAMERA_SERVO_RIGHT = 5;
    
    //Rack potentiometer constants
    static final double RACK_FULLY_RETRACTED_VOLTS = 0;
    static final double RACK_FULLY_RETRACTED_INCHES = 0;
    static final double RACK_FULLY_EXTENDED_VOLTS = 4.5;
    static final double RACK_FULLY_EXTENDED_INCHES = 40;
    
    //Pivot potentionmeter constants
    static final double PIVOT_FULLY_FORWARD_VOLTS = 4.5;
    static final double PIVOT_FULLY_FORWARD_DEGREES = 180;
    static final double PIVOT_FULLY_BACKWARD_VOLTS = .5;
    static final double PIVOT_FULLY_BACKWARD_DEGRESS = -180;
    
    //Plumb bob potentiometer constants
    static final double PLUMB_FULLY_FORWARD_VOLTS
    static final double PLUMB_FULLY_FORWARD_DEGREES
    static final double PLUMB_FULLY_BACKWARD_VOLTS
    static final double PLUMB_FULLY_BACKWARD_DEGREES
    
    //Potentiometer info
    public static double rackVolts;
    public static double rackPivotVolts;
    public static double plumbPivotVolts;
    
    //Postion Info
    public static double rackExtention;
    public static double rackPivot;
    public static double plumbPivot;
    public static double totalPivot;
    public static int currentLevel;
    
    //Rack Postions Goals
    public static double[] rackExtentionGoals = {0,0/*,ect*/);
    public static double[] rackPivotGoals = {-45,-10/*,ect*/);
    public static double[] RobotPivotGoals = {0,0/*,ect*/);
    public static int stepNumber;
    public static boolean correctToGoal;
    
    //Auto climb running
    public static boolean climbing = false;
    boolean climbButtonDown = false;
    
    //Encoders
    EncoderCode encoder1;
    Encoder testEncoder;
    
    //Relays
    static final int RELAY_LIGHT = 1;
    
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
    public static Jaguar rightDrive;
    public static Jaguar leftDrive;
    public static Jaguar rackExtender;
    public static Jaguar rackPivoter;
    
    //Network Tables
    NetworkTable server;
    
    //Variables
    public static int driveMode; //1=Tankdrive 2=ArcadeDrive
    public static double driveSpeed; //speed modifier for the drive 0.0-1.0
    
    //Camera Junk
    public static Servo cameraServoX;
    public static Servo cameraServoY;
    
    //Flashing Light
    public static Relay light;
    
    
    /********************************** Constructor **************************************************/
    
    public MainRobot() {
        // Initialize counters to record the number of loops completed in autonomous and teleop modes
        autoPeriodicLoops = 0;
        disabledPeriodicLoops = 0;
        telePeriodicLoops = 0;
        
        encoder1 = new EncoderCode(1,2);
        
        
        server = NetworkTable.getTable("SmartDashboard");
        
        //Initalize jaguars
        //testJag = new Jaguar(PWM_rightDrivePort);
        rightDrive = new Jaguar(PWM_rightDrivePort);
        leftDrive = new Jaguar(PWM_leftDrivePort);
        
        cameraServoX = new Servo(PWM_cameraXServo);
        cameraServoY = new Servo(PWM_cameraYServo);
        
        //Init joysticks (You can change the ports in the driver station gui)
        rightStick = new Joystick(1); //port 1
        leftStick = new Joystick(2); //port 2
        
        //Init light
        light = new Relay(RELAY_LIGHT);
        
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
        driveSpeed = .75;
        
        System.out.println("Initialization done....");
    }

    /**
     * This function is called when the robot is disabled
     */
    public void disabledInit() {
        System.out.println("Tele-op deactivated");
        disabledPeriodicLoops = 0;
        encoder1.stop();
        //stop motors
        rightDrive.set(0);
        leftDrive.set(0);
        rackExtender.set(0);
        rackPivoter.set(0);
        climbing = false;
        
        
    }
    
    /**
     * This function is called when autonomous is started
     */
    public void autonomousInit() {
        System.out.println("autonomous activated...");
        autoPeriodicLoops = 0;
        CameraMotor.setAngle(45,95);
        rackExtender.set(0);
        rackPivoter.set(0);
        climbing = false;
    }
    
    /**
     * This function is called when tele-op is started
     */
    public void teleopInit() {
        System.out.println("Tele-op activated...");
        telePeriodicLoops = 0;
        
        climbing = false;
        
        encoder1.reset();
        encoder1.start();
        
        //reset motors
        
        rightDrive.set(0);
        leftDrive.set(0);
        rackExtender.set(0);
        rackPivoter.set(0);
        CameraMotor.setAngle(45,95);

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
        updatePostion();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        // feed the user watchdog at every period when autonomous is enabled
        Watchdog.getInstance().feed();
        // add to the loop count
        autoPeriodicLoops++;
        updatePostion();
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // feed the user watchdog at every period when tele-op is enabled
        Watchdog.getInstance().feed();
        
        updatePostion();
        
        for (int i=0; i<18; i++) {
            if (rightStick.getRawButton(i))
            {
                System.out.println("Button # "+ i);
            }
        }
        
        if (rightStack.getRawButton(10))
        {
            if (!climbButtonDown)
            {
                climbing = !climbing;
                climbButton = true;
            }
        }
        else
        {
            climbButtonDown = false;
        }
        
        
        
        //System.out.println(rightStick.getX(GenericHID.Hand.kRight));
        //System.out.println(rightStick.s))
        
        // add to the loop count
        telePeriodicLoops++;
        //Main drive train
        //testJag.set(-.1);
        //m_leftDrive.set(.1);
        //System.out.println(testJag.get());
        DriveTrain.updateDrive(driveMode);
        CameraMotor.updateAngle();
        //BlinkyLight.update(telePeriodicLoops);
        
        if (leftStick.getRawButton(1))
        {
            CameraMotor.setAngle(45,95);
        }
        
        //ImageProcessor.Process(server);
        encoder1.putCount(server,"Count1");
        encoder1.putDistance(server, "Distance1");
        System.out.println("Count "+encoder1.get());
        System.out.println("Distance "+encoder1.getDistance());
        //System.out.println("Count "+testEncoder.get());
        //System.out.println("Distance "+testEncoder.getDistance());
        //System.out.println("Direction "+testEncoder.getDirection());
        
    }
    
    public void updatePosition()
    {
        rackExtention = RACK_FULLY_RETRACTED_INCHES + (RACK_FULLY_EXTENDED_INCHES - RACK_FULLY_RETRACTED_INCHES) * ((rackVolts - RACK_FULLY_RETRACTED_VOLTS) / (RACK_FULLY_EXTENDED_VOLTS - RACK_FULLY_RETRACTED_VOLTS));
        rackPivot = PIVOT_FULLY_BACKWARD_DEGREES + (PIVOT_FULLY_FORWARD_DEGREES - PIVOT_FULLY_BACKWARD_DEGREES) * ((rackPivotVolts - PIVOT_FULLY_BACKWARD_VOLTS) / (PIVOT_FULLY_FORWARD_VOLTS - PIVOT_FULLY_BACKWARD_VOLTS));
        plumbPivot = PLUMB_FULLY_BACKWARD_DEGREES + (PLUMB_FULLY_FORWARD_DEGREES - PLUMB_FULLY_BACKWARD_DEGREES) * ((plumbPivotVolts - PLUMB_FULLY_BACKWARD_VOLTS) / (PLUMB_FULLY_FORWARD_VOLTS - PLUMB_FULLY_BACKWARD_VOLTS));
        
        totalPivot = rackPivot+plumbPivot;
    }
    
    public void correctRack()
    {
        if (correctToGoal)
        {
            if (rackExtention<rackExtentionGoals[stepNumber]-2)
            {
                rackExtender.set(.5);
            }
            else if(rackExtention<rackExtentionGoals[stepNumber]-1)
            {
                rackExtender.set(.2);
            }
            else if(rackExtention<rackExtentionGoals[stepNumber]-.1)
            {
                rackExtender.set(.1);
            }
            else if(rackExtention>rackExtentionGoals[stepNumber]+2)
            {
                rackExtender.set(.5);
            }
            else if(rackExtention>rackExtentionGoals[stepNumber]+1)
            {
                rackExtender.set(.2);
            }
            else if(rackExtention>rackExtentionGoals[stepNumber]+.1)
            {
                rackExtender.set(.1);
            }
            else
            {
                rackExtender.set(0);
            }
            
        }
    }
}
