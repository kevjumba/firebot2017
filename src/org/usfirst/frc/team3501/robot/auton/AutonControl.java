package org.usfirst.frc.team3501.robot.auton;

import java.util.ArrayList;

import org.usfirst.frc.team3501.robot.auton.mode.AutonBuilder;
import org.usfirst.frc.team3501.robot.auton.mode.AutonMode;
import org.usfirst.frc.team3501.robot.auton.mode.DefaultMode;
import org.usfirst.frc.team3501.robot.auton.mode.step1Modes.Drive;
import org.usfirst.frc.team3501.robot.oi.RobotOutput;
import org.usfirst.frc.team3501.robot.util.Debugger;

public class AutonControl {
  private static AutonControl instance;

  public static final int NUM_ARRAY_MODE_STEPS = 1; // number of different
  // constructions for auton
  private int autonDelay;
  private long autonStartTime;

  private boolean running;

  private int[] autonSubmodeSelections = new int[NUM_ARRAY_MODE_STEPS];
  private ArrayList<ArrayList<AutonMode>> autonSteps = new ArrayList<>();

  private int currIndex;
  private AutonCommand[] commands;

  // singleton pattern
  public static AutonControl getInstance() {
    if (instance == null) {
      instance = new AutonControl();

    }
    return instance;
  }

  public AutonControl() {
    this.autonDelay = 0;
    this.currIndex = 0;

    for (int i = 0; i < NUM_ARRAY_MODE_STEPS; i++) {
      this.autonSteps.add(new ArrayList<AutonMode>());
      this.autonSubmodeSelections[i] = 0; // default autonomous selections in
      // order words do nothings
    }

    ArrayList<AutonMode> step1 = this.autonSteps.get(0);
    step1.add(new DefaultMode());
    step1.add(new Drive());

    // add auton mode steps here
  }

  public void initialize() {
    this.currIndex = 0;
    this.running = true;

    // initialize auton in runCycle
    AutonBuilder ab = new AutonBuilder();

    // add auton commands from all the different steps
    for (int i = 0; i < this.autonSteps.size(); i++) {
      this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).addToMode(ab);
    }

    // get the full auton mode
    this.commands = ab.getAutonList();

    this.autonStartTime = System.currentTimeMillis();

    // clear out each components "run seat"
    AutonCommand.reset();
  }

  public void runCycle() {
    // haven't initialized list yet
    long timeElapsed = System.currentTimeMillis() - this.autonStartTime;
    if (timeElapsed > this.getAutonDelayLength() && this.running) {
      Debugger.println("Current index " + this.currIndex, "QTIP");

      // start waiting commands
      while (this.currIndex < this.commands.length &&
          this.commands[this.currIndex].checkAndRun()) {
        this.currIndex++;

      }
      // calculate call for all running commands
      AutonCommand.execute();
    } else {
      RobotOutput.getInstance().stopAll();
    }

  }

  public void stop() {
    this.running = false;
  }

  public long getAutonDelayLength() {
    return this.autonDelay * 500;
  }

  public void updateModes() {
    // TODO write update modes that updates the modes array
    autonSubmodeSelections[0] = 1;
  }
}
