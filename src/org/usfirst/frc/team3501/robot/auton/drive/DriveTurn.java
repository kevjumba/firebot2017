package org.usfirst.frc.team3501.robot.auton.drive;

import org.usfirst.frc.team3501.robot.auton.AutonCommand;
import org.usfirst.frc.team3501.robot.auton.RobotComponent;
import org.usfirst.frc.team3501.robot.oi.RobotOutput;
import org.usfirst.frc.team3501.robot.oi.SensorInput;
import org.usfirst.frc.team3501.robot.util.Lib;
import org.usfirst.frc.team3501.robot.util.PID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTurn extends AutonCommand {

  private RobotOutput robotOut;
  private SensorInput sensorIn;

  private PID gyroControl;

  private boolean firstCycle = true;
  private double targetAngle;

  private double pConst = 0.0;
  private double iConst = 0.0;
  private double dConst = 0.0;
  private double eps = 0.0;

  public DriveTurn(double targetAngle) {
    this(targetAngle, 1.0, -1, -1);
  }

  public DriveTurn(double targetAngle, long timeOut) {
    this(targetAngle, 1.0, -1, timeOut);
  }

  public DriveTurn(double targetAngle, double eps, long timeOut) {
    this(targetAngle, 1.0, eps, timeOut);
  }

  public DriveTurn(double targetAngle, double maxSpeed) {
    this(targetAngle, maxSpeed, -1, -1);
  }

  public DriveTurn(double targetAngle, double maxSpeed, double eps, long timeOut) {
    super(RobotComponent.DRIVE, timeOut);

    this.robotOut = RobotOutput.getInstance();
    this.sensorIn = SensorInput.getInstance();

    this.targetAngle = targetAngle;
    //
    // pConst = SmartDashboard.getNumber("Drive Gyro P: ");
    // iConst = SmartDashboard.getNumber("Drive Gyro I: ");
    // dConst = SmartDashboard.getNumber("Drive Gyro D: ");
    pConst = 0.1;
    iConst = 0;
    dConst = 0;
    if (eps != -1) {
      this.eps = eps;
    } else {
      // this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
    }

    this.gyroControl = new PID(pConst, iConst, dConst, eps);
    this.gyroControl.setMaxOutput(maxSpeed);
  }

  @Override
  public boolean calculate() {
    if (this.firstCycle) {
      this.firstCycle = false;
      this.gyroControl.setDesiredValue(this.sensorIn.getAngle()
          + this.targetAngle);
      SmartDashboard.putNumber("Desired Angle: ",
          (this.sensorIn.getAngle() + this.targetAngle));

    }
    this.gyroControl.setConstants(pConst, iConst, dConst);

    double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
    SmartDashboard.putNumber("Output ", (xVal));
    double leftDrive = Lib.calcLeftTankDrive(xVal, 0.0);
    double rightDrive = Lib.calcRightTankDrive(xVal, 0.0);

    if (this.gyroControl.isDone()) {
      this.robotOut.setDriveLeft(0.0);
      this.robotOut.setDriveRight(0.0);
      return true;
    } else {
      this.robotOut.setDriveLeft(leftDrive);
      this.robotOut.setDriveRight(rightDrive);
    }

    return false;
  }

  @Override
  public void override() {
    this.robotOut.setDriveLeft(0.0);
    this.robotOut.setDriveRight(0.0);
  }

}
