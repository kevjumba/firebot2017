package org.usfirst.frc.team3501.robot.oi;

import org.usfirst.frc.team3501.robot.util.LogitechStick;

public class DriverInput {

  private static DriverInput instance;
  // private LogitechDualAction driver;
  private LogitechStick driver;
  private LogitechStick operator;

  private DriverInput() {
    this.driver = new LogitechStick(0); //
    this.operator = new LogitechStick(1);
  }

  public static DriverInput getInstance() {
    if (instance == null) {
      instance = new DriverInput();
    }
    return instance;
  }

  // -------------------------------------
  // --- DRIVER --------------------------
  // -------------------------------------

  public double getDriverX() {
    return this.driver.getX();
  }

  public double getDriverY() {
    return this.driver.getY();
  }

  public double getDriverTwist() {
    return this.driver.getTwist();
  }

}
