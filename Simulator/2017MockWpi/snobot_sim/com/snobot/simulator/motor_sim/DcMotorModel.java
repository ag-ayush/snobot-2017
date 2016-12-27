package com.snobot.simulator.motor_sim;

//https://github.com/Team254/Sim-FRC-2015/blob/master/src/com/team254/frc2015/sim/DCMotor.java
public class DcMotorModel
{

    // Motor Parameters
    public final double NOMINAL_VOLTAGE;
    public final double FREE_SPEED_RPM;
    public final double FREE_CURRENT;
    public final double STALL_TORQUE;
    public final double STALL_CURRENT;

    // Motor constants
    public double mKT;
    public double mKV;
    public double mResistance;
    public double mMotorInertia;

    // Current motor state
    protected double mPosition;
    protected double mVelocity;
    protected double mCurrent;

    public DcMotorModel(double aNominalVoltage, double aFreeSpeedRpm, double aFreeCurrent, double aStallTorque, double aStallCurrent,
            double aMotorInertia)
    {
        NOMINAL_VOLTAGE = aNominalVoltage;
        FREE_SPEED_RPM = aFreeSpeedRpm;
        FREE_CURRENT = aFreeCurrent;
        STALL_TORQUE = aStallTorque;
        STALL_CURRENT = aStallCurrent;

        mKT = aStallTorque / aStallCurrent;
        mKV = (aFreeSpeedRpm / aNominalVoltage) * (Math.PI * 2.0) / 60.0;
        mResistance = aNominalVoltage / aStallCurrent;
        mMotorInertia = aMotorInertia;

        // System.out.println("NEW: " + mKT + ", " + mKV + ", " + mResistance +
        // ", " + mMotorInertia);
    }

    /**
     * Reset the motor to a specified state.
     * 
     * @param position
     * @param velocity
     * @param current
     */
    public void reset(double position, double velocity, double current)
    {
        mPosition = position;
        mVelocity = velocity;
        mCurrent = current;
    }

    /**
     * Simulate applying a given voltage and load for a specified period of
     * time.
     * 
     * @param load
     *            Load applied to the motor (kg*m^2)
     * @param acceleration
     *            The external torque applied (ex. due to gravity) (N*m)
     * @param timestep
     *            How long the input is applied (s)
     */
    public void step(double applied_voltage, double load, double external_torque, double timestep)
    {
        /*
         * Using the 971-style first order system model. V = I * R + Kv * w
         * torque = Kt * I
         * 
         * V = torque / Kt * R + Kv * w torque = J * dw/dt + external_torque
         * 
         * dw/dt = (V - Kv * w) * Kt / (R * J) - external_torque / J
         */
        load += mMotorInertia;
        double acceleration = (applied_voltage - mVelocity / mKV) * mKT / (mResistance * load) + external_torque / load;
        mVelocity += acceleration * timestep;
        mPosition += mVelocity * timestep + .5 * acceleration * timestep * timestep;
        mCurrent = load * acceleration * Math.signum(applied_voltage) / mKT;
    }

    public double getPosition()
    {
        return mPosition;
    }

    public double getVelocity()
    {
        return mVelocity;
    }

    public double getCurrent()
    {
        return mCurrent;
    }

}