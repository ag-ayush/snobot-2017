package com.snobot.simulator;

import java.util.ArrayList;

import com.snobot.simulator.joysticks.IMockJoystick;
import com.snobot.simulator.joysticks.JoystickFactory;

import edu.wpi.first.wpilibj.Timer;

public class RobotStateSingleton
{

    public interface LoopListener
    {
        public void looped();
    }

    /**
     * Singlteton instance
     */
    private static RobotStateSingleton sInstance = new RobotStateSingleton();

    /**
     * Mutex used to keep things civil between the GUI thread and the Robot
     * thread
     */
    private static final Object sPROGRAM_STARTED_LOCK = new Object();

    /**
     * The period that the main loop should be run at
     */
    private static final double sCYCLE_TIME = .02;

    /**
     * The time to sleep. You can run simulations faster/slower by changing
     * this. For example, making the wait time 1 second, means one 20ms cycle
     * will happen each second, 50x slower than normal. Or, you could make it
     * .002, which would make the code execute at 10x speed
     */
    private double sWaitTime = .02;

    private boolean mRobotStarted = false;

    private ArrayList<LoopListener> mListeners = new ArrayList<>();

    private RobotStateSingleton()
    {
        // enabled_time = 0;
    }

    public static RobotStateSingleton get()
    {
        return sInstance;
    }

    public void addLoopListener(LoopListener aListener)
    {
        mListeners.add(aListener);
    }

    private void updateLoopListeners()
    {
        IMockJoystick[] joysticks = JoystickFactory.get().getAll();
        for(int i = 0; i < joysticks.length; ++i)
        {
            IMockJoystick joystick = joysticks[i];
            JoystickJni.setJoystickInformation(i, joystick.getAxisValues(), joystick.getPovValues(), joystick.getButtonCount(),
                    joystick.getButtonMask());
        }

        SimulationConnectorJni.updateLoop();

        for (LoopListener listener : mListeners)
        {
            listener.looped();
        }
    }

    public void waitForProgramStart()
    {
        if (!mRobotStarted)
        {
            synchronized (sPROGRAM_STARTED_LOCK)
            {
                try
                {
                    System.out.println("Waiting for robot to initialize...");
                    sPROGRAM_STARTED_LOCK.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            System.out.println("Robot already initialized!");
        }

    }

    public void robotInitialized()
    {
        synchronized (sPROGRAM_STARTED_LOCK)
        {
            System.out.println("Robot Initialized");
            System.out.println("\n\n");
            sPROGRAM_STARTED_LOCK.notify();
            mRobotStarted = true;
        }
    }

    public void waitForDSData()
    {
        Timer.delay(sWaitTime);

        synchronized (sPROGRAM_STARTED_LOCK)
        {
            updateLoopListeners();
        }
    }

    public void setWaitTime(double aTime)
    {
        sWaitTime = aTime;
    }

    public double getCycleTime()
    {
        return sCYCLE_TIME;
    }
}
