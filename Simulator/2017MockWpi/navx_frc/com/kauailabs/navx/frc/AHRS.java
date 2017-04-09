package com.kauailabs.navx.frc;

import edu.wpi.first.wpilibj.SPI.Port;

public class AHRS
{
    private AHRSAbstraction mAbstraction;

    public AHRS(Port aPort)
    {
        mAbstraction = new AHRSAbstraction();
        NavxRegistry.get().registerNavx(aPort, mAbstraction);
    }

    public double getYaw()
    {
        return mAbstraction.getYaw();
    }

    public void reset()
    {
        mAbstraction.reset();
    }

}
