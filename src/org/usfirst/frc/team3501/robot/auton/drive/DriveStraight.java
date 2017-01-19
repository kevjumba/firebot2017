package org.usfirst.frc.team3501.robot.auton.drive;

import org.usfirst.frc.team3501.robot.auton.AutonCommand;
import org.usfirst.frc.team3501.robot.auton.RobotComponent;
import org.usfirst.frc.team3501.robot.oi.RobotOutput;
import org.usfirst.frc.team3501.robot.oi.SensorInput;
import org.usfirst.frc.team3501.robot.util.Lib;
import org.usfirst.frc.team3501.robot.util.PID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraight extends AutonCommand {

  private RobotOutput robotOut;
  private SensorInput sensorIn;

  private PID encControl;
  private PID gyroControl;

  private double gyroP;
  private double gyroI;
  private double gyroD;
  private double gyroEps;

  private double encP;
  private double encI;
  private double encD;
  private double encEps;

  private boolean firstCycle;
  private double target;

  public DriveStraight(double target) {
    this(target, 1.0, SmartDashboard.getNumber("Drive Enc Eps: "), -1);
  }

  public DriveStraight(double target, long timeOut) {
    this(target, 1.0, SmartDashboard.getNumber("Drive Enc Eps: "), timeOut);
  }

  public DriveStraight(double target, double eps, long timeOut) {
    this(target, 1.0, eps, timeOut);
  }

  public DriveStraight(double target, double maxSpeed, double eps, long timeOut) {
    super(RobotComponent.DRIVE, timeOut);

    this.robotOut = RobotOutput.getInstance();
    this.sensorIn = SensorInput.getInstance();

    this.target = target;
    this.firstCycle = true;
    this.encP = 0.1;
    this.encI = 0.0;
    this.encD = 0.0;
    this.gyroP = 0.1;
    this.gyroI = 0.0;
    this.gyroD = 0.0;
    // if (this.robotOut.isHighGear()) {
    // this.encP = SmartDashboard.getNumber("Drive P High: ");
    // this.encI = SmartDashboard.getNumber("Drive I High: ");
    // this.encD = SmartDashboard.getNumber("Drive D High: ");
    // } else {
    // this.encP = SmartDashboard.getNumber("Drive Enc P: ");
    // this.encI = SmartDashboard.getNumber("Drive Enc I: ");
    // this.encD = SmartDashboard.getNumber("Drive Enc D: ");
    // }
    // this.encEps = eps;
    // this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
    // this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
    // this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
    // this.gyroEps = SmartDashboard.getNumber("Drive Gyro Eps: ");

    // TODO: figure out correct PID values
    this.encControl = new PID(this.encP, this.encI, this.encD);
    this.encControl.setDoneRange(0.5);
    this.encControl.setMaxOutput(maxSpeed);
    this.encControl.setMinDoneCycles(5);

    this.gyroControl = new PID(this.gyroP, this.gyroI, this.gyroD,
        this.gyroEps);
    this.gyroControl.setDoneRange(0.5);
    this.gyroControl.setMinDoneCycles(5);
  }

  @Override
  public boolean calculate() {
    if (this.firstCycle) {
      this.firstCycle = false;
      this.encControl.setDesiredValue(this.sensorIn.getDriveInches()
          + this.target);
      this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
    }

    double yVal = this.encControl.calcPID(this.sensorIn.getDriveInches());
    double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());

    double leftDrive = Lib.calcLeftTankDrive(xVal, yVal);
    double rightDrive = Lib.calcRightTankDrive(xVal, yVal);

    if (this.encControl.isDone()) {
      this.robotOut.setDriveLeft(0.0);
      this.robotOut.setDriveRight(0.0);
      return true;
    } else {
      this.robotOut.setDriveLeft(leftDrive);
      this.robotOut.setDriveRight(rightDrive);
      return false;
    }
  }

  @Override
  public void override() {
    this.robotOut.setDriveLeft(0.0);
    this.robotOut.setDriveRight(0.0);
  }

}
