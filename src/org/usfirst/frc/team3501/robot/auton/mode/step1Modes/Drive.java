package org.usfirst.frc.team3501.robot.auton.mode.step1Modes;

import org.usfirst.frc.team3501.robot.auton.drive.DriveStraight;
import org.usfirst.frc.team3501.robot.auton.mode.AutonBuilder;
import org.usfirst.frc.team3501.robot.auton.mode.AutonMode;

public class Drive implements AutonMode {

  @Override
  public void addToMode(AutonBuilder ab) {
    // TODO Auto-generated method stub
    ab.addCommand(new DriveStraight(10));
    // ab.addCommand(new DriveTurn(45));
  }

}
