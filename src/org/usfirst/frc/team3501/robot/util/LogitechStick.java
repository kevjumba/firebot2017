/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team3501.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Mike
 */
public class LogitechStick {
  private Joystick joystick;

  public LogitechStick(int portNum) {
    this.joystick = new Joystick(portNum);
  }

  public double getX() {
    return this.joystick.getRawAxis(0);
  }

  public double getY() {
    return -this.joystick.getRawAxis(1);
  }

  public double getTrigger() {
    return this.joystick.getRawAxis(2);
    // return this.joystick.getRawAxis(2); and 3
  }

  public boolean getButton(int btnNum) {
    return this.joystick.getRawButton(btnNum);
  }

  public double getTwist() {
    return this.joystick.getTwist();
  }
}
