package com.snobot.simulator.gui.module_widgetHAL;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.Util;
import com.snobot.simulator.module_wrapper.AnalogSourceWrapperJni;
import com.snobot.simulator.module_wrapper.AnalogWrapper;
import com.snobot.simulator.module_wrapper.RelayWrapperJni;

public class AnalogOutputDisplay extends BaseWidgetDisplay<Integer>
{

    private class AnalogDisplay extends JPanel
    {
        private static final int sDOT_SIZE = 30;

        private double mMotorSpeed;

        public AnalogDisplay()
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
            g.setColor(Util.colorGetShadedColor(mMotorSpeed, 5, 0));
            g.fillOval(0, 0, sDOT_SIZE, sDOT_SIZE);
        }
    }

    public AnalogOutputDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Analog"));
    }

    @Override
    public void update()
    {
        for (Integer key : mWidgetMap.keySet())
        {
            double value = AnalogSourceWrapperJni.getVoltage(key);
            ((AnalogDisplay) mWidgetMap.get(key)).updateDisplay(value);
        }
    }

    @Override
    protected AnalogDisplay createWidget(Integer key)
    {
        return new AnalogDisplay();
    }

    @Override
    protected String getName(Integer aKey)
    {
        return AnalogSourceWrapperJni.getName(aKey);
    }
}
