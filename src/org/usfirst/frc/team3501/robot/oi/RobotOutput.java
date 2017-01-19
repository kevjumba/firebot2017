package org.usfirst.frc.team3501.robot.oi;

import com.ctre.CANTalon;

public class RobotOutput {
  private static RobotOutput instance;

  // Drive
  private static CANTalon leftDriveFront;
  private static CANTalon leftDriveRear;

  private static CANTalon rightDriveFront;
  private static CANTalon rightDriveRear;

  // mechanisms
  private static CANTalon shooter;
  private static CANTalon indexer;
  private static CANTalon intake;

  private RobotOutput() {
  }

  public static RobotOutput getInstance() {
    if (RobotOutput.instance == null) {
      RobotOutput.instance = new RobotOutput();
    }
    return RobotOutput.instance;
  }

  public void setDriveLeft(double val) {
    this.leftDriveFront.set(val);
    this.leftDriveRear.set(val);

  }

  public void setDriveRight(double val) {
    this.rightDriveFront.set(-val);
    this.rightDriveRear.set(-val);
  }

  public void setShooter(double val) {
    this.shooter.set(val);
  }

  public void setIntake(double val) {
    this.intake.set(val);
  }

  public void setIndexer(double val) {
    this.indexer.set(val);
  }

  public void stopAll() {
    setDriveLeft(0);
    setDriveRight(0);
    setIntake(0);
    setShooter(0);
  }

  public boolean isHighGear() {
    // TODO Fill this in
    return false;
  }
}
