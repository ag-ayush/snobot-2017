package com.snobot.simulator;

import java.util.ArrayList;
import java.util.List;

public class SimulatorConfig
{
    public static class ASensorActuatorConfig
    {
        public String name;

        public ASensorActuatorConfig()
        {

        }

        public ASensorActuatorConfig(String aName)
        {
            name = aName;
        }
    }

    public static class SpeedControllerConfig extends ASensorActuatorConfig
    {
        public int handlePort;
        public String handleName;

        public SpeedControllerConfig()
        {

        }

        public SpeedControllerConfig(String aName, int aHandle)
        {
            super(aName);
            handlePort = aHandle;
        }
    }

    public static class AnalogConfig extends ASensorActuatorConfig
    {
        public int handlePort;
        public String handleName;

        public AnalogConfig()
        {

        }

        public AnalogConfig(String aName, int aHandle)
        {
            super(aName);
            handlePort = aHandle;
        }
    }

    public static class DigitalConfig extends ASensorActuatorConfig
    {
        public int handlePort;
        public String handleName;

        public DigitalConfig()
        {

        }

        public DigitalConfig(String aName, int aHandle)
        {
            super(aName);
            handlePort = aHandle;
        }
    }

    public static class QuadEncoderConfig extends ASensorActuatorConfig
    {
        public int handleAPort;
        public int handleBPort;
        public int speedControllerPort;

        public String handleAName;
        public String handleBName;

        public QuadEncoderConfig()
        {

        }

        // public QuadEncoderConfig(String aName, int aHandleA, int aHandleB)
        // {
        // super(aName);
        // handleAPort = aHandleA;
        // handleBPort = aHandleB;
        // }
    }

    public static class TankDriveSimulator
    {
        public QuadEncoderConfig left_encoder;
        public QuadEncoderConfig right_encoder;
        public AnalogConfig gyro_wrapper;
        public double turnKp;
    }

    public List<SpeedControllerConfig> speed_contollers = new ArrayList<>();
    public List<QuadEncoderConfig> quad_encoders = new ArrayList<>();
    public List<AnalogConfig> analog_config = new ArrayList<>();
    public List<DigitalConfig> digital_config = new ArrayList<>();

    public List<TankDriveSimulator> tank_drive_simulator = new ArrayList<>();
}
