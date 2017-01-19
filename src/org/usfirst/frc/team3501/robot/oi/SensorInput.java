package org.usfirst.frc.team3501.robot.oi;

import org.usfirst.frc.team3501.robot.util.BNO055;
import org.usfirst.frc.team3501.robot.util.Enc;
import org.usfirst.frc.team3501.robot.util.EncoderAngle;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorInput {

  public static final boolean USING_CAMERA = true;

  private static SensorInput instance;
  private RobotOutput robotOut;
  private Enc encDriveLeft;
  private Enc encDriveRight;
  private Enc encShooter;

  private BNO055 imu; // gyro

  private EncoderAngle encoderAngle;

  // private Compressor compressor;
  private PowerDistributionPanel pdp;

  private static double TICKSPERINCH = (128 * 3) * (3) / (Math.PI * 7.874); // drive
  private static double TICKSPERREV = 1024; // shooter
  private static double SHOOTERRATIO = 1;

  private double lastTime = 0.0;
  private double deltaTime = 20.0;

  private double leftDriveSpeedFPS;
  private double rightDriveSpeedFPS;
  private double shooterSpeedRPM;
  private double gyroZero = 0;

  private boolean firstCycle = true;
  private boolean hangStarted = false;

  private Thread cameraThread;

  private double zAxisAutoInit = 0.0;

  private SensorInput() {
    this.robotOut = RobotOutput.getInstance();
    this.encDriveLeft = new Enc(0, 1); // was 0,1 MUST FLIP FOR COMP BOT
    this.encDriveRight = new Enc(2, 3); // was 2,3MUST FLIP FOR COMP BOT
    this.encShooter = new Enc(4, 5);

    // if (USING_CAMERA) {
    // this.camera = new SimCamera();
    // this.cameraThread = new Thread(this.camera);
    // this.cameraThread.setPriority(Thread.NORM_PRIORITY - 1);
    // this.cameraThread.start();
    // }
    // this.compressor = new Compressor();

    this.encoderAngle = new EncoderAngle(this);

    this.imu = BNO055.getInstance(BNO055.opmode_t.OPERATION_MODE_IMUPLUS,
        BNO055.vector_type_t.VECTOR_EULER, Port.kOnboard, (byte) 0x28);
    gyroZero = imu.getHeading();
    this.pdp = new PowerDistributionPanel();
    this.reset();
  }

  public static SensorInput getInstance() {
    if (instance == null) {
      instance = new SensorInput();
    }

    return instance;
  }

  public void update() {

    if (this.lastTime == 0.0) {
      this.deltaTime = 20;
      this.lastTime = System.currentTimeMillis();
    } else {
      this.deltaTime = System.currentTimeMillis() - this.lastTime;
      this.lastTime = System.currentTimeMillis();
    }

    if (this.firstCycle) {

      this.firstCycle = false;
    }
    // this.camera.update();

    // System.out.println("Gyro Speed" + this.gyro.getSpeed());

    this.encDriveLeft.updateSpeed();
    this.encDriveRight.updateSpeed();
    this.encShooter.updateSpeed();
    this.encoderAngle.update();

    // ParticleReport.setScoreConstants(SmartDashboard.getNumber("areaTarget: "),
    // SmartDashboard.getNumber("areaWeight: ")
    // , SmartDashboard.getNumber("ratioTarget: "),
    // SmartDashboard.getNumber("ratioWeight: "),
    // SmartDashboard.getNumber("widthWeight: "),
    // SmartDashboard.getNumber("widthTarget: "),
    // SmartDashboard.getNumber("heightWeight: "),
    // SmartDashboard.getNumber("heightTarget: "));
    // SmartDashboard.putNumber("Continuous Angle", this.getAngle());
    // SmartDashboard.putNumber("Encoder Angle", this.getEncoderAngle());
    // SmartDashboard.putNumber("Angle Difference", this.getAngle() -
    // this.getEncoderAngle());

    SmartDashboard.putNumber("GYRO: ", this.getAngle());

    double left = this.getEncoderLeftSpeed() / TICKSPERINCH;
    double right = this.getEncoderRightSpeed() / TICKSPERINCH;

    this.leftDriveSpeedFPS = (left / 12) / (this.deltaTime / 1000);
    this.rightDriveSpeedFPS = (right / 12) / (this.deltaTime / 1000);
    this.shooterSpeedRPM = (this.getEncoderShooterSpeed() / TICKSPERREV)
        * SHOOTERRATIO / (this.deltaTime / 60000.0); // I tried just
    // this.getEncoderShooterSpeed
    // but it didnt work either

    double score;
    String output;

    /*
     * if(this.camera.getBestParticle() != null){
     * score = this.camera.getBestParticle().getScore();
     * }else{
     */
    score = 0;
    output = "";
    // }

    SmartDashboard.putBoolean("Is air low?", this.getIsAirLow());
    SmartDashboard.putNumber("Drive Speed FPS: ", this.getDriveSpeedFPS());
    SmartDashboard.putNumber("Drive Position Inches: ", this.getDriveInches());

    // SmartDashboard.putNumber("Navx Mag Z: ", this.navx.getRawMagZ());
    // SmartDashboard.putNumber("Navx Mag X: ", this.navx.getRawMagX());
    // SmartDashboard.putNumber("Navx Mag Y: ", this.navx.getRawMagY());
    SmartDashboard.putNumber("Navx Zaxis Auto Init: ", this.zAxisAutoInit);

    SmartDashboard.putNumber("Shooter RPM: ", this.shooterSpeedRPM);
    SmartDashboard.putNumber("Shooter Enc: ", this.encShooter.get());

    SmartDashboard.putNumber("Left Enc", this.getEncoderLeft());
    SmartDashboard.putNumber("Right Enc", this.getEncoderRight());

  }

  public void reset() {
    this.encoderAngle.reset();
    this.encDriveLeft.reset();
    this.encDriveRight.reset();
    gyroZero = imu.getHeading();
    this.firstCycle = true;
    // this.camera.stopCamera();
    // this.robotOut.resetIntakeArmEnc();
    // this.encIndexer.reset();

  }

  public double getLastTickLength() {
    return this.deltaTime;
  }

  // -----------------------------------------------------
  // ---- Component Specific Methods ---------------------
  // -----------------------------------------------------

  // ----------------- DRIVE ------------------------------
  public int getEncoderLeft() {
    return this.encDriveLeft.get();
  }

  public int getEncoderLeftSpeed() {
    return this.encDriveLeft.speed();
  }

  public double getEncoderLeftRawSpeed() {
    return this.encDriveLeft.rawSpeed();
  }

  public double getLeftDriveInches() {
    return this.getEncoderLeft() / TICKSPERINCH;
  }

  public double getLeftDriveSpeedInches() {
    return this.getEncoderLeftSpeed() / TICKSPERINCH;
  }

  public int getEncoderRight() {
    return this.encDriveRight.get();
  }

  public int getEncoderRightSpeed() {
    return this.encDriveRight.speed();
  }

  public double getRightDriveInches() {
    return this.getEncoderRight() / TICKSPERINCH;
  }

  public double getRightDriveSpeedInches() {
    return this.getEncoderRightSpeed() / TICKSPERINCH;
  }

  public double getEncoderRightRawSpeed() {
    return this.encDriveRight.rawSpeed();
  }

  public double getDriveEncoderAverage() {
    return (this.getEncoderRight() + this.getEncoderLeft()) / 2;
  }

  public double getDriveInches() {
    return this.getDriveEncoderAverage() / TICKSPERINCH;
  }

  public Enc getEncoderLeftObj() {
    return this.encDriveLeft;
  }

  public Enc getEncoderRightObj() {
    return this.encDriveRight;
  }

  public double getDriveEncoderSpeedAverage() {
    return (this.getEncoderLeftSpeed() + this.getEncoderRightSpeed()) / 2.0;
  }

  public double getDriveSpeedFPS() {
    return (this.leftDriveSpeedFPS + this.rightDriveSpeedFPS) / 2;
  }

  // ----------------------- Shooter -------------------------------
  public int getEncoderShooter() {
    return this.encShooter.get();
  }

  public double getEncoderShooterSpeed() {
    return this.encShooter.speed(); // .getRate();
  }

  public double getShooterRPM() {
    return this.shooterSpeedRPM;
  }

  public boolean getHangStarted() {
    return this.hangStarted;
  }

  public void setHangStarted() {
    this.hangStarted = true;
  }

  // ----------------------- GYRO ----------------------------------

  public double getAngle() {
    return this.imu.getHeading() - this.gyroZero;
  }

  public double getZAxisAutoInit() {
    return this.zAxisAutoInit;
  }

  // ----------------------- POSITIONAL SYSTEM
  // ----------------------------------

  public double getEncoderAngle() {
    return this.encoderAngle.getAngle();
  }

  // ----------------------- PDP ----------------------------------
  public double getVoltage() {
    return this.pdp.getVoltage();
  }

  public double getCurrent(int channel) {
    return this.pdp.getCurrent(channel);
  }

  public double getTotalCurrent() {
    return this.pdp.getTotalCurrent();
  }

  // ----------------------- Compressor ----------------------------------

  public boolean getIsAirLow() {
    return false;// this.compressor.getPressureSwitchValue();
  }

}
