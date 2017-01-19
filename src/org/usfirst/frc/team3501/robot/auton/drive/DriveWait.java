package org.usfirst.frc.team3501.robot.auton.drive;

import org.usfirst.frc.team3501.robot.auton.AutonCommand;
import org.usfirst.frc.team3501.robot.auton.RobotComponent;

public class DriveWait extends AutonCommand {

  public DriveWait() {
    super(RobotComponent.DRIVE);
  }

  @Override
  public boolean calculate() {
    return true;
  }

  @Override
  public void override() {
    // TODO Auto-generated method stub

  }

}
