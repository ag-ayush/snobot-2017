package com.snobot.simulator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.snobot.simulator.RobotStateSingleton;
import com.snobot.simulator.gui.joysticks.JoystickManagerDialog;

import edu.wpi.first.wpilibj.Timer;

public class SimulatorFrame extends JFrame
{

    private GraphicalSensorDisplayPanel mBasicPanel;
    private EnablePanel mEnablePanel;

    public SimulatorFrame()
    {
        initComponenents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RobotStateSingleton.get().addLoopListener(new RobotStateSingleton.LoopListener()
        {

            @Override
            public void looped()
            {
                mBasicPanel.update();
                mEnablePanel.setTime(Timer.getMatchTime());
            }
        });
    }

    private void initComponenents()
    {
        mBasicPanel = new GraphicalSensorDisplayPanel();
        mEnablePanel = new EnablePanel();

        mEnablePanel.addStateChangedListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                RobotStateSingleton.get().setDisabled(!mEnablePanel.isEnabled());
                RobotStateSingleton.get().setAutonomous(mEnablePanel.isAuton());
            }
        });

        JButton configureJoystickBtn = new JButton("Configure Joysticks");
        configureJoystickBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                showJoystickDialog();
            }
        });

        JButton changeSettingsBtn = new JButton("Change Settings");
        JButton hideSettingsBtn = new JButton("Save Settings");

        changeSettingsBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettingsBtn.setVisible(false);
                hideSettingsBtn.setVisible(true);

                showSettingsOptions(true);
            }
        });

        hideSettingsBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettingsBtn.setVisible(true);
                hideSettingsBtn.setVisible(false);

                showSettingsOptions(false);
            }
        });
        hideSettingsBtn.setVisible(false);

        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.add(changeSettingsBtn, BorderLayout.NORTH);
        settingsPanel.add(hideSettingsBtn, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(configureJoystickBtn, BorderLayout.NORTH);
        buttonPanel.add(settingsPanel, BorderLayout.SOUTH);

        add(mBasicPanel, BorderLayout.CENTER);
        add(mEnablePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        RobotStateSingleton.get().setDisabled(false);
        RobotStateSingleton.get().setAutonomous(false);

        mEnablePanel.setRobotEnabled(true);
        RobotStateSingleton.get().setDisabled(false);
    }

    private void showJoystickDialog()
    {
        JoystickManagerDialog dialog = new JoystickManagerDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void showSettingsOptions(boolean aShow)
    {
        mBasicPanel.showSettingsButtons(aShow);
        pack();
    }
}
