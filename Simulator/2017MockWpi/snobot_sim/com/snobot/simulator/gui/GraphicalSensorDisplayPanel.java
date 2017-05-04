package com.snobot.simulator.gui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.gui.module_widget.EncoderGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SolenoidGraphicDisplay;
import com.snobot.simulator.gui.module_widget.SpeedControllerGraphicDisplay;
import com.snobot.simulator.gui.module_widgetHAL.AnalogOutputDisplay;
import com.snobot.simulator.gui.module_widgetHAL.DigitalSourceGraphicDisplay;
import com.snobot.simulator.gui.module_widgetHAL.RelayGraphicDisplay;
import com.snobot.simulator.module_wrapper.AnalogSourceWrapperJni;
import com.snobot.simulator.module_wrapper.DigitalSourceWrapperJni;
import com.snobot.simulator.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapperJni;

@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class GraphicalSensorDisplayPanel extends JPanel
{
    public GraphicalSensorDisplayPanel()
    {
        create();
    }

    private com.snobot.simulator.gui.module_widgetHAL.SpeedControllerGraphicDisplay mSpeedControllerPanel;
    private SpeedControllerGraphicDisplay mCanSpeedControllerPanel;
    private SolenoidGraphicDisplay mSolenoidPanel;
    private DigitalSourceGraphicDisplay mDigitalSourcePanel;
    private RelayGraphicDisplay mRelayPanel;
    private AnalogOutputDisplay mAnalogPanel;
    private EncoderGraphicDisplay mEncoderPanel;
    private EncoderGraphicDisplay mCanEncoderPanel;

    public void create()
    {
        SensorActuatorRegistry reg = SensorActuatorRegistry.get();

        List<Integer> speedControllers = IntStream.of(SpeedControllerWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> digitalSource = IntStream.of(DigitalSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> relaySource = IntStream.of(RelayWrapperJni.getPortList()).boxed().collect(Collectors.toList());
        List<Integer> analogSource = IntStream.of(AnalogSourceWrapperJni.getPortList()).boxed().collect(Collectors.toList());

        mSpeedControllerPanel = new com.snobot.simulator.gui.module_widgetHAL.SpeedControllerGraphicDisplay(speedControllers, "Speed Controllers");
        mCanSpeedControllerPanel = new SpeedControllerGraphicDisplay(reg.getCanSpeedControllers(), "CAN Speed Controllers");
        mSolenoidPanel = new SolenoidGraphicDisplay(reg.getSolenoids());
        mDigitalSourcePanel = new DigitalSourceGraphicDisplay(digitalSource);
        mRelayPanel = new RelayGraphicDisplay(relaySource);
        mAnalogPanel = new AnalogOutputDisplay(analogSource);
        mEncoderPanel = new EncoderGraphicDisplay(reg.getEncoders(), "Encoders (Digital Input)");
        mCanEncoderPanel = new EncoderGraphicDisplay(reg.getCanEncoders(), "Encoders (CAN)");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (!mSpeedControllerPanel.isEmpty())
        {
            add(mSpeedControllerPanel);
        }
        if (!mCanSpeedControllerPanel.isEmpty())
        {
            add(mCanSpeedControllerPanel);
        }
        if (!mSolenoidPanel.isEmpty())
        {
            add(mSolenoidPanel);
        }
        if (!mDigitalSourcePanel.isEmpty())
        {
            add(mDigitalSourcePanel);
        }
        if (!mRelayPanel.isEmpty())
        {
            add(mRelayPanel);
        }
        if (!mAnalogPanel.isEmpty())
        {
            add(mAnalogPanel);
        }
        if (!mEncoderPanel.isEmpty())
        {
            add(mEncoderPanel);
        }
        if (!mCanEncoderPanel.isEmpty())
        {
            add(mCanEncoderPanel);
        }
    }

    public void update()
    {
        SensorActuatorRegistry reg = SensorActuatorRegistry.get();

        mSpeedControllerPanel.update();
        mCanSpeedControllerPanel.update(reg.getCanSpeedControllers());
        mSolenoidPanel.update(reg.getSolenoids());
        mDigitalSourcePanel.update();
        mRelayPanel.update();
        mAnalogPanel.update();
        mEncoderPanel.update(reg.getEncoders());
        mCanEncoderPanel.update(reg.getCanEncoders());

        repaint();
    }
}
