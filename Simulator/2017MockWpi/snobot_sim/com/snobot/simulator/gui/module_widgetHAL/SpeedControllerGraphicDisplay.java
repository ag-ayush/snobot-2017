package com.snobot.simulator.gui.module_widgetHAL;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.Util;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapperJni;

public class SpeedControllerGraphicDisplay extends BaseWidgetDisplay<Integer>
{
    private class MotorDisplay extends JPanel
    {
        private static final int sDOT_SIZE = 30;

        private double mMotorSpeed;

        public MotorDisplay()
        {
            setPreferredSize(new Dimension(sDOT_SIZE, sDOT_SIZE));
        }

        public void updateDisplay(double aValue)
        {
            mMotorSpeed = aValue;
        }

        @Override
        public void paint(Graphics g)
        {
            g.clearRect(0, 0, getWidth(), getHeight());
            g.setColor(Util.getMotorColor(mMotorSpeed));
            g.fillOval(0, 0, sDOT_SIZE, sDOT_SIZE);
        }
    }

    public SpeedControllerGraphicDisplay(Collection<Integer> aKeys, String aName)
    {
        super(aKeys);
        setBorder(new TitledBorder(aName));
    }

    @Override
    public void update()
    {
        for (Integer key : mWidgetMap.keySet())
        {
            double value = SpeedControllerWrapperJni.getVoltagePercentage(key);
            ((MotorDisplay) mWidgetMap.get(key)).updateDisplay(value);
        }
    }

    @Override
    protected MotorDisplay createWidget(Integer aKey)
    {
        return new MotorDisplay();
    }

    @Override
    protected String getName(Integer aKey)
    {
        return SpeedControllerWrapperJni.getName(aKey);
    }
}
