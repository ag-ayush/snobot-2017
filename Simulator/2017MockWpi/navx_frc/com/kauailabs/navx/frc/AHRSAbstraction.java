package com.kauailabs.navx.frc;

import com.snobot.simulator.module_wrapper.GyroWrapper;

public class AHRSAbstraction
{
    protected GyroWrapper mYaw;
    protected GyroWrapper mPitch;
    protected GyroWrapper mRoll;

    public AHRSAbstraction()
    {
        mYaw = new GyroWrapper();
        mPitch = new GyroWrapper();
        mRoll = new GyroWrapper();
    }

    public double getYaw()
    {
        return mYaw.getAngle();
    }

    public void reset()
    {
        mYaw.setAngle(0);
        mPitch.setAngle(0);
        mRoll.setAngle(0);
    }

    public GyroWrapper getYawWrapper()
    {
        return mYaw;
    }

}
