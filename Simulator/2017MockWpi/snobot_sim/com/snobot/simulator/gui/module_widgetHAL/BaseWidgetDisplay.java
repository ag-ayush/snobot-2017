package com.snobot.simulator.gui.module_widgetHAL;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class BaseWidgetDisplay<KeyType> extends JPanel
{

    protected Map<KeyType, Container> mWidgetMap = new HashMap<>();

    public BaseWidgetDisplay(Collection<KeyType> aKeys)
    {
        setLayout(new GridBagLayout());

        int i = 0;
        for (KeyType key : aKeys)
        {
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridy = i;

            Container panelPair = createWidget(key);
            if (panelPair != null)
            {
                mWidgetMap.put(key, panelPair);

                gc.gridx = 0;
                add(new JLabel("" + getName(key)), gc);

                gc.gridx = 1;
                add(panelPair, gc);

                ++i;
            }
        }
    }

    protected abstract Container createWidget(KeyType aKey);

    protected abstract String getName(KeyType aKey);

    public abstract void update();

    public boolean isEmpty()
    {
        return mWidgetMap.isEmpty();
    }
}
