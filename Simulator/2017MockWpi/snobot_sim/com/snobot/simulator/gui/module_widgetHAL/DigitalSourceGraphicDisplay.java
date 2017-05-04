package com.snobot.simulator.gui.module_widgetHAL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.module_wrapper.DigitalSourceWrapperJni;

public class DigitalSourceGraphicDisplay extends BaseWidgetDisplay<Integer>
{
    private class DigitalSourceWrapperDisplay extends JPanel
    {
        private static final int sDOT_SIZE = 30;

        private boolean mState;

        public DigitalSourceWrapperDisplay()
        {
            setPreferredSize(new Dimension(sDOT_SIZE, sDOT_SIZE));
        }

        public void updateDisplay(boolean aValue)
        {
            mState = aValue;
        }

        @Override
        public void paint(Graphics g)
        {
            g.clearRect(0, 0, getWidth(), getHeight());
            g.setColor(mState ? Color.green : Color.red);
            g.fillOval(0, 0, sDOT_SIZE, sDOT_SIZE);
        }
    }

    public DigitalSourceGraphicDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Digital IO"));
    }

    @Override
    public void update()
    {
        for (Integer key : mWidgetMap.keySet())
        {
            boolean value = DigitalSourceWrapperJni.getState(key);
            ((DigitalSourceWrapperDisplay) mWidgetMap.get(key)).updateDisplay(value);
        }
    }

    @Override
    protected DigitalSourceWrapperDisplay createWidget(Integer aKey)
    {
        return new DigitalSourceWrapperDisplay();
    }

    @Override
    protected String getName(Integer aKey)
    {
        return DigitalSourceWrapperJni.getName(aKey);
    }
}
