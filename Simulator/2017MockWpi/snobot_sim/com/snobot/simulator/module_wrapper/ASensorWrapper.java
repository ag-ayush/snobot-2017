package com.snobot.simulator.module_wrapper;

public class ASensorWrapper
{
    protected String mName;
    protected boolean mVisible;

    public ASensorWrapper(String aName)
    {
        mName = aName;
        mVisible = true;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String aName)
    {
        mName = aName;
    }

    public void setVisible(boolean aVisible)
    {
        mVisible = aVisible;
    }

    public boolean isVisible()
    {
        return mVisible;
    }
}
