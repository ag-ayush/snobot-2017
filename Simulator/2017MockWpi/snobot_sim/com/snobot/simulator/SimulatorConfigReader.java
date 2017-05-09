package com.snobot.simulator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.snobot.simulator.module_wrapper.EncoderWrapperJni;
import com.snobot.simulator.module_wrapper.RelayWrapperJni;
import com.snobot.simulator.module_wrapper.SolenoidWrapperJni;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapperJni;

@SuppressWarnings("unchecked")
public class SimulatorConfigReader
{

    public void loadConfig(String aConfigFile)
    {
        if (aConfigFile == null)
        {
            System.out.println("Config file not set, won't hook anything up");
            return;
        }

        try
        {
            System.out.println("Loading " + aConfigFile);
            Yaml yaml = new Yaml();
            parseConfig(yaml.load(new FileReader(new File(aConfigFile))));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void parseConfig(Object aConfig)
    {
        Map<String, Object> config = (Map<String, Object>) aConfig;

        if (config.containsKey("speed_controllers"))
        {
            parseSpeedControllers((List<Map<String, Object>>) config.get("speed_controllers"));
        }

        if (config.containsKey("quad_encoders"))
        {
            parseEncoders((List<Map<String, Object>>) config.get("quad_encoders"));
        }

        if (config.containsKey("relays"))
        {
            parseRelays((List<Map<String, Object>>) config.get("relays"));
        }

        if (config.containsKey("solenoids"))
        {
            parseSolenoids((List<Map<String, Object>>) config.get("solenoids"));
        }

        if (config.containsKey("tank_drives"))
        {
            parseTankDriveConfig((List<Map<String, Object>>) config.get("tank_drives"));
        }

    }

    protected void parseSolenoids(List<Map<String, Object>> aSolenoids)
    {
        for (Map<String, Object> solenoidConfig : aSolenoids)
        {
            int handle = getIntHandle(solenoidConfig.get("handle"));
            if (solenoidConfig.containsKey("name"))
            {
                SolenoidWrapperJni.setName(handle, solenoidConfig.get("name").toString());
            }
        }
    }

    protected void parseRelays(List<Map<String, Object>> aRelays)
    {
        for (Map<String, Object> relayConfig : aRelays)
        {
            int handle = getIntHandle(relayConfig.get("handle"));
            if (relayConfig.containsKey("name"))
            {
                RelayWrapperJni.setName(handle, relayConfig.get("name").toString());
            }
        }
    }

    protected void parseSpeedControllers(List<Map<String, Object>> aSpeedControllers)
    {
        for (Map<String, Object> scConfig : aSpeedControllers)
        {
            int handle = getIntHandle(scConfig.get("handle"));
            if (scConfig.containsKey("name"))
            {
                SpeedControllerWrapperJni.setName(handle, scConfig.get("name").toString());
            }

            if (scConfig.containsKey("motor_sim"))
            {
                parseMotorSimConfig(handle, (Map<String, Object>) scConfig.get("motor_sim"));
            }
        }
    }

    protected void parseEncoders(List<Map<String, Object>> aEncoders)
    {
        for (Map<String, Object> encConfig : aEncoders)
        {
            int handle = -1;

            if (encConfig.containsKey("handle_a") && encConfig.containsKey("handle_b"))
            {
                int handleA = getIntHandle(encConfig.get("handle_a"));
                int handleB = getIntHandle(encConfig.get("handle_b"));

                handle = (handleA << 8) + handleB;
            }
            else
            {
                throw new RuntimeException("Could not load encoder config, will cause the program to crash");
            }

            if (encConfig.containsKey("name"))
            {
                EncoderWrapperJni.setName(handle, encConfig.get("name").toString());
            }

            if (encConfig.containsKey("speed_controller_handle"))
            {
                int speedControllerHandle = getIntHandle(encConfig.get("speed_controller_handle"));
                SimulationConnectorJni.connectEncoderAndSpeedController(handle, speedControllerHandle);
            }
        }
    }

    protected void parseMotorSimConfig(int aScHandle, Map<String, Object> motorSimConfig)
    {
        String type = motorSimConfig.get("type").toString();
        switch (type)
        {
        case "Simple":
            double maxSpeed = ((Number) motorSimConfig.get("max_speed")).doubleValue();
            SimulationConnectorJni.setSpeedControllerSimpleModel(aScHandle, maxSpeed);
            break;
        default:
            System.err.println("Unknown sim type!");
        }
    }

    protected void parseTankDriveConfig(List<Map<String, Object>> aTankDriveConfig)
    {
        for (Map<String, Object> tankDriveConfig : aTankDriveConfig)
        {
            int leftEncHandleA = getIntHandle(tankDriveConfig.get("left_enc_handle_a"));
            int leftEncHandleB = getIntHandle(tankDriveConfig.get("left_enc_handle_b"));
            int rightEncHandleA = getIntHandle(tankDriveConfig.get("right_enc_handle_a"));
            int rightEncHandleB = getIntHandle(tankDriveConfig.get("right_enc_handle_b"));
            int scHandle = getIntHandle(tankDriveConfig.get("gyro_handle"));
            double turnKp = ((Number) tankDriveConfig.get("turn_kp")).doubleValue();

            SimulationConnectorJni.connectTankDriveSimulator(leftEncHandleA, leftEncHandleB, rightEncHandleA, rightEncHandleB, scHandle, turnKp);
        }
    }

    protected int getIntHandle(Object aHandleObject)
    {
        int output = 0;

        try
        {
            output = (Integer) aHandleObject;
        }
        catch (ClassCastException ex)
        {
            String fullName = aHandleObject.toString();

            int lastDot = fullName.lastIndexOf(".");
            String className = fullName.substring(0, lastDot);
            String fieldName = fullName.substring(lastDot + 1);

            try
            {
                Class myClass = Class.forName(className);
                Field myField = myClass.getDeclaredField(fieldName);
                output = myField.getInt(null);
            }
            catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }

        }

        return output;
    }
}
