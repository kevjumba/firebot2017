/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team3501.robot.auton.util;

import org.usfirst.frc.team3501.robot.auton.AutonCommand;
import org.usfirst.frc.team3501.robot.auton.AutonControl;
import org.usfirst.frc.team3501.robot.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonStop extends AutonCommand {

  public AutonStop() {
    super(RobotComponent.UTIL);
  }

  @Override
  public boolean calculate() {
    AutonControl.getInstance().stop();
    return true;
  }

  @Override
  public void override() {
    // nothing to do

  }

}
