package com.snobot.simulator.motor_sim;

public class DcMotorModelConfig
{

    // Motor Parameters
    public final double NOMINAL_VOLTAGE;
    public final double FREE_SPEED_RPM;
    public final double FREE_CURRENT;
    public final double STALL_TORQUE;
    public final double STALL_CURRENT;

    // Motor constants
    public double mKT;
    public double mKV;
    public double mResistance;
    public double mMotorInertia;
    public boolean mInverted;

    // Indicates the motor has a brake, i.e. when givin 0 volts it will stay put
    public boolean mHasBrake;


    public DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia)
    {
        this(aNominalVoltage, aFreeSpeedRpm, aFreeCurrent, aStallTorque, aStallCurrent, aMotorInertia, false, false);
    }

    public DcMotorModelConfig(
            double aNominalVoltage, 
            double aFreeSpeedRpm, 
            double aFreeCurrent, 
            double aStallTorque, 
            double aStallCurrent,
            double aMotorInertia,
            boolean aHasBrake, boolean aInverted)
    {
        NOMINAL_VOLTAGE = aNominalVoltage;
        FREE_SPEED_RPM = aFreeSpeedRpm;
        FREE_CURRENT = aFreeCurrent;
        STALL_TORQUE = aStallTorque;
        STALL_CURRENT = aStallCurrent;
        
        mHasBrake = aHasBrake;
        mInverted = aInverted;

        mKT = aStallTorque / aStallCurrent;
        mKV = (aFreeSpeedRpm / aNominalVoltage) * (Math.PI * 2.0) / 60.0;
        mResistance = aNominalVoltage / aStallCurrent;
        mMotorInertia = aMotorInertia;
    }

    public void setInverted(boolean aInverted)
    {
        mInverted = aInverted;
    }

    public void setHasBrake(boolean aHasBrake)
    {
        mHasBrake = aHasBrake;
    }
}
