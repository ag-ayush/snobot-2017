package com.snobot.simulator;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.SimulatorConfig.QuadEncoderConfig;
import com.snobot.simulator.SimulatorConfig.SpeedControllerConfig;
import com.snobot.simulator.SimulatorConfig.TankDriveSimulator;
import com.snobot.simulator.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapperJni;

import edu.wpi.first.wpilibj.RobotBase;

public abstract class ASimulator implements ISimulatorUpdater
{
    private static final Object sUPDATE_MUTEX = new Object();

    private static final double sMOTOR_UPDATE_FREQUENCY = .02;

    protected List<ISimulatorUpdater> mSimulatorComponenets;

    protected ASimulator()
    {
        mSimulatorComponenets = new ArrayList<>();
        updateMotorsThread.start();
    }

    protected void createSimulatorComponents(String aConfigFile)
    {
        try
        {
            System.out.println("Loading " + aConfigFile);
            Yaml yaml = new Yaml();
            SimulatorConfig config = (SimulatorConfig) yaml.load(new FileReader(new File(aConfigFile)));
            
            for (SpeedControllerConfig speedController : config.speed_contollers)
            {
                SpeedControllerWrapperJni.setName(speedController.handlePort, speedController.name);
            }

            for (QuadEncoderConfig encoderConfig : config.quad_encoders)
            {
                int handle = (encoderConfig.handleAPort << 8) + encoderConfig.handleBPort;

                SimulationConnectorJni.connectEncoderAndSpeedController(handle, encoderConfig.speedControllerPort);
                EncoderWrapperJni.setName(handle, encoderConfig.name);
            }

            for (TankDriveSimulator tankSim : config.tank_drive_simulator)
            {
                SimulationConnectorJni.connectTankDriveSimulator(
                        tankSim.left_encoder.handleAPort, 
                        tankSim.left_encoder.handleBPort, 
                        tankSim.right_encoder.handleAPort, 
                        tankSim.right_encoder.handleBPort, 
                        tankSim.gyro_wrapper.handlePort,
                        tankSim.turnKp);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update()
    {
        synchronized (sUPDATE_MUTEX)
        {
            for (ISimulatorUpdater simulator : mSimulatorComponenets)
            {
                simulator.update();
            }
        }
    }

    @Override
    public void setRobot(RobotBase aRobot)
    {
        for (ISimulatorUpdater simulator : mSimulatorComponenets)
        {
            simulator.setRobot(aRobot);
        }
    }

    protected Thread updateMotorsThread = new Thread(new Runnable()
    {

        @Override
        public void run()
        {
            while (true)
            {
                synchronized (sUPDATE_MUTEX)
                {
                    SpeedControllerWrapperJni.updateAllSpeedControllers(sMOTOR_UPDATE_FREQUENCY);
                }

                try
                {
                    Thread.sleep((long) (sMOTOR_UPDATE_FREQUENCY * 1000));
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }, "MotorUpdater");
}
