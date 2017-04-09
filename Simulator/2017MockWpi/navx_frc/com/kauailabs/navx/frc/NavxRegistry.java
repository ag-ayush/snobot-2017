package com.kauailabs.navx.frc;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class NavxRegistry
{
    private static NavxRegistry mInstance = new NavxRegistry();

    private NavxRegistry()
    {

    }

    public static NavxRegistry get()
    {
        return mInstance;
    }

    private Map<Port, AHRSAbstraction> mNavxMap = new HashMap<>();

    public void registerNavx(Port aPort, AHRSAbstraction mAbstraction)
    {
        mNavxMap.put(aPort, mAbstraction);
    }

    public AHRSAbstraction getNavx()
    {
        return getNavx(SPI.Port.kMXP);
    }

    public AHRSAbstraction getNavx(Port aPort)
    {
        return mNavxMap.get(aPort);
    }
}
