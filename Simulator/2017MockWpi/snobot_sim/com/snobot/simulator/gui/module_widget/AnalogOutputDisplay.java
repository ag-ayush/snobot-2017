package com.snobot.simulator.gui.module_widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.Util;
import com.snobot.simulator.module_wrapper.AnalogSourceWrapperJni;


public class AnalogOutputDisplay extends BaseWidgetDisplay<Integer, AnalogDisplay>
{
    public AnalogOutputDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Analog"));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, AnalogDisplay> pair : mWidgetMap.entrySet())
        {
            double value = AnalogSourceWrapperJni.getVoltage(pair.getKey());
            pair.getValue().updateDisplay(value);
        }
    }

    @Override
    protected AnalogDisplay createWidget(Integer aKey)
    {
        if (AnalogSourceWrapperJni.getWantsHidden(aKey))
        {
            return null;
        }
        return new AnalogDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        JDialog dialog = new JDialog();

        dialog.setTitle("Analog " + aKey + " Settings");
        dialog.getContentPane().add(new JTextField(getName(aKey)));
        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return AnalogSourceWrapperJni.getName(aKey);
    }
}

class AnalogDisplay extends JPanel
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