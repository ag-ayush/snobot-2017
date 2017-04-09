package com.snobot.simulator.robot_sim.roboSquad2017;

import java.util.Map;

import com.kauailabs.navx.frc.AHRSAbstraction;
import com.kauailabs.navx.frc.NavxRegistry;
import com.snobot.simulator.ASimulator;
import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.GyroWrapper;
import com.snobot.simulator.module_wrapper.SolenoidWrapper;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;
import com.snobot.simulator.module_wrapper.TankDriveGyroSimulator;
import com.snobot.simulator.motor_sim.DcMotorModel;
import com.snobot.simulator.motor_sim.StaticLoadDcMotorSim;
import com.snobot.simulator.motor_sim.motors.MakeTransmission;
import com.snobot.simulator.motor_sim.motors.VexMotorFactory;

public class RoboSquadSimulator extends ASimulator
{
    ///////////////////////////////////
    // Solenoids
    ///////////////////////////////////
    // Climber Brake
    public static int breakSolenoidChannel1 = 0;
    public static int breakSolenoidChannel2 = 1;

    // Gear Intake
    public static int gearIntakeSolenoidChannel1 = 2;
    public static int gearIntakeSolenoidChannel2 = 3;

    // Fuel Intake
    public static int fuelIntakeSolenoidChannel1 = 4;
    public static int fuelIntakeSolenoidChannel2 = 5;

    ///////////////////////////////////
    // Talons
    ///////////////////////////////////
    // Drive TalonSRXs
    public static int leftDriveMaster = 1;
    public static int leftDriveSlave1 = 16;
    public static int leftDriveSlave2 = 2;

    public static int rightDriveMaster = 13;
    public static int rightDriveSlave1 = 14;
    public static int rightDriveSlave2 = 12;

    // Shooter TalonSRXs
    public static int shooterMaster = 5;
    public static int shooterSlave1 = 6;

    // Feeder, Gear Intake, and Fuel Intake TalonSRXs
    public static int gearIntakeChannel1 = 10;
    public static int gearIntakeChannel2 = 11;
    public static int feederChannel = 4;
    public static int fuelIntakeMaster = 15;
    public static int fuelIntakeSlave = 3;

    @Override
    protected void createSimulatorComponents()
    {
        Map<Integer, SolenoidWrapper> solenoids = SensorActuatorRegistry.get().getSolenoids();

        solenoids.get(breakSolenoidChannel1).setName("Break (A)");
        solenoids.get(breakSolenoidChannel2).setName("Break (B)");

        solenoids.get(gearIntakeSolenoidChannel1).setName("Gear (A)");
        solenoids.get(gearIntakeSolenoidChannel2).setName("Gear (B)");

        solenoids.get(fuelIntakeSolenoidChannel1).setName("Fuel (A)");
        solenoids.get(fuelIntakeSolenoidChannel2).setName("Fuel (B)");
    
        Map<Integer, SpeedControllerWrapper> speedControllers = SensorActuatorRegistry.get().getCanSpeedControllers();

        speedControllers.get(shooterMaster).setName("Shooter (A)");
        speedControllers.get(shooterSlave1).setName("Shooter (B)");

        speedControllers.get(gearIntakeChannel1).setName("Gear Intake (A)");
        speedControllers.get(gearIntakeChannel2).setName("Gear Intake (B)");

        speedControllers.get(fuelIntakeMaster).setName("Fuel Intake (A)");
        speedControllers.get(fuelIntakeSlave).setName("Fuel Intake (B)");

        speedControllers.get(feederChannel).setName("Feeder");

        SensorActuatorRegistry.get().getRelays().get(0).setName("Compressor?");
        
        createDrivetrain();
    }

    private void createDrivetrain()
    {
        double drivetrainLoad = .6;
        // double gearReduction = 1;
        double drivetrainReduction = 11.43;
        double drivetrainSpeedLossFactor = .81;
        double drivetrainWheelDiameter = 6;
        
        Map<Integer, SpeedControllerWrapper> speedControllers = SensorActuatorRegistry.get().getCanSpeedControllers();
        speedControllers.get(leftDriveMaster).setName("Left Drive (A)");
        speedControllers.get(leftDriveSlave1).setName("Left Drive (B)");
        speedControllers.get(leftDriveSlave2).setName("Left Drive (C)");

        speedControllers.get(rightDriveMaster).setName("Right Drive (A)");
        speedControllers.get(rightDriveSlave1).setName("Right Drive (B)");
        speedControllers.get(rightDriveSlave2).setName("Right Drive (C)");

        EncoderWrapper leftEncoder = new EncoderWrapper(leftDriveMaster, -1);
        EncoderWrapper rightEncoder = new EncoderWrapper(rightDriveMaster, -1);

        SpeedControllerWrapper leftSC = SensorActuatorRegistry.get().getCanSpeedControllers().get(leftDriveMaster);
        DcMotorModel leftMotor = VexMotorFactory.makeCIMMotor();
        leftMotor = MakeTransmission.makeTransmission(leftMotor, 3, drivetrainReduction, 1.0);
        leftMotor.setInverted(false);
        leftSC.setMotorSimulator(new StaticLoadDcMotorSim(leftMotor, drivetrainLoad, drivetrainWheelDiameter / 2 * drivetrainSpeedLossFactor));
        leftEncoder.setSpeedController(leftSC);

        rightEncoder = new EncoderWrapper(rightDriveMaster, -1);
        SpeedControllerWrapper rightSC = SensorActuatorRegistry.get().getCanSpeedControllers().get(rightDriveMaster);
        DcMotorModel rightMotor = VexMotorFactory.makeCIMMotor();
        rightMotor = MakeTransmission.makeTransmission(rightMotor, 3, drivetrainReduction, 1.0);
        rightMotor.setInverted(true);
        rightSC.setMotorSimulator(new StaticLoadDcMotorSim(rightMotor, drivetrainLoad, drivetrainWheelDiameter / 2 * drivetrainSpeedLossFactor));
        rightEncoder.setSpeedController(rightSC);

        SensorActuatorRegistry.get().register(leftEncoder, leftDriveMaster);
        SensorActuatorRegistry.get().register(rightEncoder, rightDriveMaster);

        AHRSAbstraction navx = NavxRegistry.get().getNavx();
        GyroWrapper gyroWrapper = navx.getYawWrapper();
        TankDriveGyroSimulator drivetrainSim = new TankDriveGyroSimulator(leftEncoder, rightEncoder, gyroWrapper);
        drivetrainSim.setTurnKp(400 / 12.0);
        mSimulatorComponenets.add(drivetrainSim);

    }
}
