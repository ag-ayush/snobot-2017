package com.snobot.simulator.gui.module_widgetHAL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.module_wrapper.RelayWrapperJni;

import edu.wpi.first.wpilibj.Relay.Value;

public class RelayGraphicDisplay extends BaseWidgetDisplay<Integer>
{
    private class RelayDisplay extends JPanel
    {
        private static final int sWIDTH = 80;
        private static final int sHEIGHT = 15;

        private Value mValue;

        public RelayDisplay()
        {
            setPreferredSize(new Dimension(sWIDTH, sHEIGHT));
            mValue = Value.kOff;
        }

        public void updateDisplay(Value value)
        {
            mValue = value;
        }

        @Override
        public void paint(Graphics g)
        {
            g.clearRect(0, 0, getWidth(), getHeight());
            g.drawRect(0, 0, getWidth(), getHeight());

            switch (mValue)
            {
            case kOff:
                break;
            case kOn:
                g.setColor(Color.green);
                g.fillRect(0, 0, sWIDTH / 2, sHEIGHT);

                g.setColor(Color.red);
                g.fillRect(sWIDTH / 2, 0, sWIDTH, sHEIGHT);
                break;
            case kForward:
                g.setColor(Color.green);
                g.fillRect(0, 0, sWIDTH, sHEIGHT);
                break;
            case kReverse:
                g.setColor(Color.red);
                g.fillRect(0, 0, sWIDTH, sHEIGHT);
                break;
            }
        }
    }

    public RelayGraphicDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Relay"));
    }

    @Override
    public void update()
    {
        for (Integer key : mWidgetMap.keySet())
        {
            Value value = Value.kForward;
            ((RelayDisplay) mWidgetMap.get(key)).updateDisplay(value);
        }
    }

    @Override
    protected RelayDisplay createWidget(Integer aKey)
    {
        return new RelayDisplay();
    }

    @Override
    protected String getName(Integer aKey)
    {
        return RelayWrapperJni.getName(aKey);
    }
}
