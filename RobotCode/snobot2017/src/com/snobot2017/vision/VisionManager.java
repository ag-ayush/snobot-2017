package com.snobot2017.vision;

import com.snobot.lib.ISubsystem;
import com.snobot2017.PortMappings2017;
import com.snobot2017.Properties2017;

public class VisionManager implements ISubsystem
{
    private VisionAdbServer mVisionServer;

    public VisionManager()
    {
        if (Properties2017.sENABLE_VISION.getValue())
        {
            mVisionServer = new VisionAdbServer(PortMappings2017.sADB_BIND_PORT, PortMappings2017.sAPP_MJPEG_PORT,
                    PortMappings2017.sAPP_MJPEG_FORWARDED_PORT);
        }
    }

    @Override
    public void init()
    {
        // mVisionServer.restartApp();

        // TODO Add log headers
    }

    @Override
    public void update()
    {

    }

    @Override
    public void control()
    {

    }

    @Override
    public void rereadPreferences()
    {

    }

    @Override
    public void updateSmartDashboard()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateLog()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop()
    {

    }

}