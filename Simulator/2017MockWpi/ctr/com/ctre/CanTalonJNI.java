/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ctre;

import com.snobot.simulator.SensorActuatorRegistry;
import com.snobot.simulator.module_wrapper.EncoderWrapper;
import com.snobot.simulator.module_wrapper.SpeedControllerWrapper;

import edu.wpi.first.wpilibj.hal.JNIWrapper;

public class CanTalonJNI extends JNIWrapper
{
    // Motion Profile status bits
    public static final int kMotionProfileFlag_ActTraj_IsValid = 0x1;
    public static final int kMotionProfileFlag_HasUnderrun = 0x2;
    public static final int kMotionProfileFlag_IsUnderrun = 0x4;
    public static final int kMotionProfileFlag_ActTraj_IsLast = 0x8;
    public static final int kMotionProfileFlag_ActTraj_VelOnly = 0x10;

    /**
     * Signal enumeration for generic signal access. Although every signal is
     * enumerated, only use this for traffic that must be solicited. Use the
     * auto generated getters/setters at bottom of this header as much as
     * possible.
     */
    public enum param_t
    {
        eProfileParamSlot0_P(1), eProfileParamSlot0_I(2), eProfileParamSlot0_D(3), eProfileParamSlot0_F(4), eProfileParamSlot0_IZone(5), eProfileParamSlot0_CloseLoopRampRate(
                6), eProfileParamSlot1_P(11), eProfileParamSlot1_I(12), eProfileParamSlot1_D(13), eProfileParamSlot1_F(14), eProfileParamSlot1_IZone(
                15), eProfileParamSlot1_CloseLoopRampRate(16), eProfileParamSoftLimitForThreshold(21), eProfileParamSoftLimitRevThreshold(22), eProfileParamSoftLimitForEnable(
                23), eProfileParamSoftLimitRevEnable(24), eOnBoot_BrakeMode(31), eOnBoot_LimitSwitch_Forward_NormallyClosed(32), eOnBoot_LimitSwitch_Reverse_NormallyClosed(
                33), eOnBoot_LimitSwitch_Forward_Disable(34), eOnBoot_LimitSwitch_Reverse_Disable(35), eFault_OverTemp(41), eFault_UnderVoltage(42), eFault_ForLim(
                43), eFault_RevLim(44), eFault_HardwareFailure(45), eFault_ForSoftLim(46), eFault_RevSoftLim(47), eStckyFault_OverTemp(48), eStckyFault_UnderVoltage(
                49), eStckyFault_ForLim(50), eStckyFault_RevLim(51), eStckyFault_ForSoftLim(52), eStckyFault_RevSoftLim(53), eAppliedThrottle(61), eCloseLoopErr(
                62), eFeedbackDeviceSelect(63), eRevMotDuringCloseLoopEn(64), eModeSelect(65), eProfileSlotSelect(66), eRampThrottle(67), eRevFeedbackSensor(
                68), eLimitSwitchEn(69), eLimitSwitchClosedFor(70), eLimitSwitchClosedRev(71), eSensorPosition(73), eSensorVelocity(74), eCurrent(75), eBrakeIsEnabled(
                76), eEncPosition(77), eEncVel(78), eEncIndexRiseEvents(79), eQuadApin(80), eQuadBpin(81), eQuadIdxpin(82), eAnalogInWithOv(83), eAnalogInVel(
                84), eTemp(85), eBatteryV(86), eResetCount(87), eResetFlags(88), eFirmVers(89), eSettingsChanged(90), eQuadFilterEn(91), ePidIaccum(
                93), eStatus1FrameRate(94), // TALON_Status_1_General_10ms_t
        eStatus2FrameRate(95), // TALON_Status_2_Feedback_20ms_t
        eStatus3FrameRate(96), // TALON_Status_3_Enc_100ms_t
        eStatus4FrameRate(97), // TALON_Status_4_AinTempVbat_100ms_t
        eStatus6FrameRate(98), // TALON_Status_6_Eol_t
        eStatus7FrameRate(99), // TALON_Status_7_Debug_200ms_t
        eClearPositionOnIdx(100),
        // reserved
        // reserved
        // reserved
        ePeakPosOutput(104), eNominalPosOutput(105), ePeakNegOutput(106), eNominalNegOutput(107), eQuadIdxPolarity(108), eStatus8FrameRate(109), // TALON_Status_8_PulseWid_100ms_t
        eAllowPosOverflow(110), eProfileParamSlot0_AllowableClosedLoopErr(111), eNumberPotTurns(112), eNumberEncoderCPR(113), ePwdPosition(114), eAinPosition(
                115), eProfileParamVcompRate(116), eProfileParamSlot1_AllowableClosedLoopErr(117), eStatus9FrameRate(118), // TALON_Status_9_MotProfBuffer_100ms_t
        eMotionProfileHasUnderrunErr(119), eReserved120(120), eLegacyControlMode(121);

        public final int value;

        private param_t(int value)
        {
            this.value = value;
        }
    }

    public static long new_CanTalonSRX(int deviceNumber, int controlPeriodMs, int enablePeriodMs)
    {
        return new_CanTalonSRX(deviceNumber);
    }

    public static long new_CanTalonSRX(int deviceNumber, int controlPeriodMs)
    {
        return new_CanTalonSRX(deviceNumber);
    }

    public static long new_CanTalonSRX(int deviceNumber)
    {
        boolean allocated = SensorActuatorRegistry.get().getCanSpeedControllers().containsKey(deviceNumber);
        if (allocated)
        {
            throw new UnsupportedOperationException("CAN device already allocated at " + deviceNumber);
        }

        SpeedControllerWrapper wrapper = new SpeedControllerWrapper(deviceNumber);
        SensorActuatorRegistry.get().register(wrapper, deviceNumber, true);

        return deviceNumber;
    }

    public static long new_CanTalonSRX()
    {
        return 0;
    }

    public static void delete_CanTalonSRX(long handle)
    {

    }

    public static void GetMotionProfileStatus(long handle, Object canTalon, Object motionProfileStatus)
    {

    }

    public static void Set(long handle, double value)
    {
        getWrapperFromBuffer(handle).set(value);
    }

    public static void SetParam(long handle, int paramEnum, double value)
    {
        if (paramEnum == CanTalonJNI.param_t.eEncPosition.value)
        {
            if (value == 0)
            {
                EncoderWrapper wrapper = getEncoderWrapperFromBuffer(handle);
                if (wrapper != null)
                {
                    wrapper.reset();
                }
            }
            else
            {
                System.err.println("Unsupported simulator option...");
            }
        }
    }

    public static void RequestParam(long handle, int paramEnum)
    {

    }

    public static double GetParamResponse(long handle, int paramEnum)
    {
        return 0;
    }

    public static int GetParamResponseInt32(long handle, int paramEnum)
    {
        return 0;
    }

    public static void SetPgain(long handle, int slotIdx, double gain)
    {

    }

    public static void SetIgain(long handle, int slotIdx, double gain)
    {

    }

    public static void SetDgain(long handle, int slotIdx, double gain)
    {

    }

    public static void SetFgain(long handle, int slotIdx, double gain)
    {

    }

    public static void SetIzone(long handle, int slotIdx, int zone)
    {

    }

    public static void SetCloseLoopRampRate(long handle, int slotIdx, int closeLoopRampRate)
    {

    }

    public static void SetVoltageCompensationRate(long handle, double voltagePerMs)
    {

    }

    public static void SetSensorPosition(long handle, int pos)
    {

    }

    public static void SetForwardSoftLimit(long handle, int forwardLimit)
    {

    }

    public static void SetReverseSoftLimit(long handle, int reverseLimit)
    {

    }

    public static void SetForwardSoftEnable(long handle, int enable)
    {

    }

    public static void SetReverseSoftEnable(long handle, int enable)
    {

    }

    public static double GetPgain(long handle, int slotIdx)
    {
        return 0;
    }

    public static double GetIgain(long handle, int slotIdx)
    {
        return 0;
    }

    public static double GetDgain(long handle, int slotIdx)
    {
        return 0;
    }

    public static double GetFgain(long handle, int slotIdx)
    {
        return 0;
    }

    public static int GetIzone(long handle, int slotIdx)
    {
        return 0;
    }

    public static int GetCloseLoopRampRate(long handle, int slotIdx)
    {
        return 0;
    }

    public static double GetVoltageCompensationRate(long handle)
    {
        return 0;
    }

    public static int GetForwardSoftLimit(long handle)
    {
        return 0;
    }

    public static int GetReverseSoftLimit(long handle)
    {
        return 0;
    }

    public static int GetForwardSoftEnable(long handle)
    {
        return 0;
    }

    public static int GetReverseSoftEnable(long handle)
    {
        return 0;
    }

    public static int GetPulseWidthRiseToFallUs(long handle)
    {
        return 0;
    }

    public static int IsPulseWidthSensorPresent(long handle)
    {
        return 0;
    }

    public static void SetModeSelect2(long handle, int modeSelect, int demand)
    {

    }

    public static void SetStatusFrameRate(long handle, int frameEnum, int periodMs)
    {

    }

    public static void ClearStickyFaults(long handle)
    {

    }

    public static void ChangeMotionControlFramePeriod(long handle, int periodMs)
    {

    }

    public static void ClearMotionProfileTrajectories(long handle)
    {

    }

    public static int GetMotionProfileTopLevelBufferCount(long handle)
    {
        return 0;
    }

    public static boolean IsMotionProfileTopLevelBufferFull(long handle)
    {
        return false;
    }

    public static void PushMotionProfileTrajectory(long handle, int targPos, int targVel, int profileSlotSelect, int timeDurMs, int velOnly,
            int isLastPoint, int zeroPos)
    {

    }

    public static void ProcessMotionProfileBuffer(long handle)
    {

    }

    public static int GetFault_OverTemp(long handle)
    {
        return 0;
    }

    public static int GetFault_UnderVoltage(long handle)
    {
        return 0;
    }

    public static int GetFault_ForLim(long handle)
    {
        return 0;
    }

    public static int GetFault_RevLim(long handle)
    {
        return 0;
    }

    public static int GetFault_HardwareFailure(long handle)
    {
        return 0;
    }

    public static int GetFault_ForSoftLim(long handle)
    {
        return 0;
    }

    public static int GetFault_RevSoftLim(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_OverTemp(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_UnderVoltage(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_ForLim(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_RevLim(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_ForSoftLim(long handle)
    {
        return 0;
    }

    public static int GetStckyFault_RevSoftLim(long handle)
    {
        return 0;
    }

    public static int GetAppliedThrottle(long handle)
    {
        return (int) (getWrapperFromBuffer(handle).get() * 1023);
    }

    public static int GetCloseLoopErr(long handle)
    {
        return 0;
    }

    public static int GetFeedbackDeviceSelect(long handle)
    {
        return 0;
    }

    public static int GetModeSelect(long handle)
    {
        return 0;
    }

    public static int GetLimitSwitchEn(long handle)
    {
        return 0;
    }

    public static int GetLimitSwitchClosedFor(long handle)
    {
        return 0;
    }

    public static int GetLimitSwitchClosedRev(long handle)
    {
        return 0;
    }

    public static int GetSensorPosition(long handle)
    {
        return GetEncPosition(handle);
    }

    public static int GetSensorVelocity(long handle)
    {
        return 0;
    }

    public static double GetCurrent(long handle)
    {
        return 0;
    }

    public static int GetBrakeIsEnabled(long handle)
    {
        return 0;
    }

    public static int GetEncPosition(long handle)
    {
        EncoderWrapper wrapper = getEncoderWrapperFromBuffer(handle);
        int port = (int) handle;
        if (wrapper != null)
        {
            return wrapper.getRaw();
        }
        else
        {
            SensorActuatorRegistry.get().register(new EncoderWrapper(port, -1), port);
        }

        return 0;
    }

    public static int GetEncVel(long handle)
    {
        return 0;
    }

    public static int GetEncIndexRiseEvents(long handle)
    {
        return 0;
    }

    public static int GetQuadApin(long handle)
    {
        return 0;
    }

    public static int GetQuadBpin(long handle)
    {
        return 0;
    }

    public static int GetQuadIdxpin(long handle)
    {
        return 0;
    }

    public static int GetAnalogInWithOv(long handle)
    {
        return 0;
    }

    public static int GetAnalogInVel(long handle)
    {
        return 0;
    }

    public static double GetTemp(long handle)
    {
        return 0;
    }

    public static double GetBatteryV(long handle)
    {
        return 0;
    }

    public static int GetResetCount(long handle)
    {
        return 0;
    }

    public static int GetResetFlags(long handle)
    {
        return 0;
    }

    public static int GetFirmVers(long handle)
    {
        return 0;
    }

    public static int GetPulseWidthPosition(long handle)
    {
        return 0;
    }

    public static int GetPulseWidthVelocity(long handle)
    {
        return 0;
    }

    public static int GetPulseWidthRiseToRiseUs(long handle)
    {
        return 0;
    }

    public static int GetActTraj_IsValid(long handle)
    {
        return 0;
    }

    public static int GetActTraj_ProfileSlotSelect(long handle)
    {
        return 0;
    }

    public static int GetActTraj_VelOnly(long handle)
    {
        return 0;
    }

    public static int GetActTraj_IsLast(long handle)
    {
        return 0;
    }

    public static int GetOutputType(long handle)
    {
        return 0;
    }

    public static int GetHasUnderrun(long handle)
    {
        return 0;
    }

    public static int GetIsUnderrun(long handle)
    {
        return 0;
    }

    public static int GetNextID(long handle)
    {
        return 0;
    }

    public static int GetBufferIsFull(long handle)
    {
        return 0;
    }

    public static int GetCount(long handle)
    {
        return 0;
    }

    public static int GetActTraj_Velocity(long handle)
    {
        return 0;
    }

    public static int GetActTraj_Position(long handle)
    {
        return 0;
    }

    public static void SetDemand(long handle, int param)
    {

    }

    public static void SetOverrideLimitSwitchEn(long handle, int param)
    {

    }

    public static void SetFeedbackDeviceSelect(long handle, int param)
    {

    }

    public static void SetRevMotDuringCloseLoopEn(long handle, int param)
    {

    }

    public static void SetOverrideBrakeType(long handle, int param)
    {

    }

    public static void SetModeSelect(long handle, int param)
    {

    }

    public static void SetProfileSlotSelect(long handle, int param)
    {

    }

    public static void SetRampThrottle(long handle, int param)
    {

    }

    public static void SetRevFeedbackSensor(long handle, int param)
    {

    }

    // *************************************************
    // Our custom functions
    // *************************************************
    private static SpeedControllerWrapper getWrapperFromBuffer(long deviceId)
    {
        return SensorActuatorRegistry.get().getCanSpeedControllers().get((int) deviceId);
    }

    private static EncoderWrapper getEncoderWrapperFromBuffer(long handle)
    {
        int port = (int) handle;

        if (SensorActuatorRegistry.get().getCanEncoders().containsKey(port))
        {
            return SensorActuatorRegistry.get().getCanEncoders().get(port);
        }

        return null;
    }
}