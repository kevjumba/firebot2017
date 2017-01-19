package org.usfirst.frc.team3501.robot.teleop;

import org.usfirst.frc.team3501.robot.oi.DriverInput;
import org.usfirst.frc.team3501.robot.oi.RobotOutput;
import org.usfirst.frc.team3501.robot.oi.SensorInput;

public class TeleopDrive implements TeleopComponent {

  private static TeleopDrive instance;

  private static final boolean TUNING = true;

  private RobotOutput robotOut;
  private DriverInput driverIn;
  private SensorInput sensorIn;

  private boolean firstCycle = true;

  public static TeleopDrive getInstance() {
    if (instance == null) {
      instance = new TeleopDrive();
    }
    return instance;
  }

  private TeleopDrive() {

    this.driverIn = DriverInput.getInstance();
    this.robotOut = RobotOutput.getInstance();
    this.sensorIn = SensorInput.getInstance();

  }

  @Override
  public void calculate() {

  }

  @Override
  public void disable() {
    this.robotOut.setDriveLeft(0.0);
    this.robotOut.setDriveRight(0.0);
  }

}
