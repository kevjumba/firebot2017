package org.usfirst.frc.team3501.robot;

import java.util.logging.Logger;

import org.usfirst.frc.team3501.robot.auton.AutonControl;
import org.usfirst.frc.team3501.robot.oi.DriverInput;
import org.usfirst.frc.team3501.robot.oi.RobotOutput;
import org.usfirst.frc.team3501.robot.oi.SensorInput;
import org.usfirst.frc.team3501.robot.teleop.TeleopControl;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

  private RobotOutput robotOut;
  private DriverInput driverInput;
  private SensorInput sensorInput;
  private TeleopControl teleopControl;
  private Logger logger;

  @Override
  public void robotInit() {

    this.robotOut = RobotOutput.getInstance();
    this.driverInput = DriverInput.getInstance();
    this.sensorInput = SensorInput.getInstance();
    this.teleopControl = TeleopControl.getInstance();
    // this.logger = Logger.getInstance();
  }

  @Override
  public void disabledInit() {
    this.robotOut.stopAll();
    // this.logger.close();
    this.teleopControl.disable();
  }

  @Override
  public void disabledPeriodic() {
    this.sensorInput.update();
    AutonControl.getInstance().updateModes();
  }

  @Override
  public void autonomousInit() {
    AutonControl.getInstance().initialize();
    SensorInput.getInstance().reset();
  }

  @Override
  public void autonomousPeriodic() {
    this.sensorInput.update();
    AutonControl.getInstance().runCycle();
  }

  @Override
  public void teleopInit() {
    // this.logger.openFile();
    // SensorInput.getInstance().reset();
  }

  @Override
  public void teleopPeriodic() {

    // if (this.driverInput.getJumpButton()) {
    // this.sensorInput.reset();
    // }

    this.sensorInput.update();
    this.teleopControl.runCycle();

    // this.logger.logAll(); // write the log data
  }
}
