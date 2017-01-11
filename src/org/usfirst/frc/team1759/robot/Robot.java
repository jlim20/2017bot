
package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.robot.commands.ExampleCommand;
import org.usfirst.frc.team1759.robot.subsystems.ExampleSubsystem;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;
	String autoSelected;
	
	RobotDrive myRobot;
	Joystick leftStick;
	Joystick rightStick;
	Joystick shootStick;
	CANTalon canTalon0;
	CANTalon canTalon1;
	CANTalon canTalon2;
	CANTalon canTalon3;
	CANTalon canTalon4;
	CANTalon canTalon7;
	CANTalon canTalon5;
	CANTalon canTalon6;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        
        chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		canTalon0 = new CANTalon(0);
		canTalon1 = new CANTalon(1);
		canTalon2 = new CANTalon(2);
		canTalon3 = new CANTalon(3);
		canTalon4 = new CANTalon(4);
		canTalon6 = new CANTalon(6);
		canTalon5 = new CANTalon(5);
		canTalon7 = new CANTalon(7);
		
		CANTalon talons[] = new CANTalon[9];
		for (int i = 0; i < talons.length; ++i) {
			talons[i] = new CANTalon(i);			
		}
		
		// Old assignments
		// final CANTalon front_left_wheel  = talons[4]; // ok
		// final CANTalon front_right_wheel = talons[1]; // ?
		// final CANTalon back_left_wheel   = talons[6]; // corrected
		// final CANTalon back_right_wheel  = talons[0]; // ok
		
		// After the Great Reassignment of 2017-01-10.
		//
		// If you draw an imaginary "И" (Cyrillic ee) on the top of the robot 
		// starting from the front left wheel, the "И" will end with the back 
		// right wheel and will hit the talons in numerical order.
		final CANTalon front_left_wheel  = talons[0];
		final CANTalon back_left_wheel   = talons[1];		
		final CANTalon front_right_wheel = talons[2];
		final CANTalon back_right_wheel  = talons[3];
		
		//Making everything backwards because the wires are making the robot go backwards
		talons[0].setInverted(true);
		talons[1].setInverted(true);
		talons[2].setInverted(true);
		talons[3].setInverted(true);
		
		// front left, back left, front right, back right
		myRobot = new RobotDrive(front_left_wheel,
				back_left_wheel, 
				front_right_wheel,
				back_right_wheel);		

		// load talon port (cantalon), lower shoot talon port(cantalon), upper
		// shoot talon port(cantalon)
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		shootStick = new Joystick(2);
		
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        
		autoSelected = (String) chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
		
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
		
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//myRobot.tankDrive(leftStick, rightStick);
    	//myRobot.arcadeDrive(rightStick);
		
    	myRobot.mecanumDrive_Cartesian(rightStick.getX(), rightStick.getY(), rightStick.getTwist(), 0);
		Timer.delay(0.02);
    	//operatorControl();
    	
    	Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}