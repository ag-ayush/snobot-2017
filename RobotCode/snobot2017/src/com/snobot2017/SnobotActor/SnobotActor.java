package com.snobot2017.SnobotActor;

import com.snobot.lib.InDeadbandHelper;
import com.snobot2017.SmartDashBoardNames;
import com.snobot2017.drivetrain.IDriveTrain;
import com.snobot2017.positioner.IPositioner;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SnobotActor implements ISnobotActor
{
    private IDriveTrain mDriveTrain;
    private IPositioner mPositioner;

    private InDeadbandHelper mInDeadbandHelper;

    private DistanceControlParams mDistanceControlParams;
    private TurnControlParams mTurningControlParams;
    private ControlMode mControlMode;

    private class DistanceControlParams
    {
        double mGoalDistance;
        double mDrivingSpeed;

        DistanceControlParams(double aGoalDistance, double aDrivingSpeed)
        {
            mGoalDistance = aGoalDistance;
            mDrivingSpeed = aDrivingSpeed;
        }
    }

    private class TurnControlParams
    {
        double mGoalAngle;
        double mTurningSpeed;

        TurnControlParams(double aGoalAngle, double aTurningSpeed)
        {
            mGoalAngle = aGoalAngle;
            mTurningSpeed = aTurningSpeed;
        }
    }

    private enum ControlMode
    {
        Off, Distance, Turning, PositionInSteps
    }

    private enum GoToPositionSubsteps
    {
        NoAction, Turning, Driving
    }

    private GoToPositionSubsteps mGoToPositionSubstep = GoToPositionSubsteps.NoAction;

    /**
     * Constructor
     * 
     * @param aDriveTrain
     * @param aPositioner
     */
    public SnobotActor(IDriveTrain aDriveTrain, IPositioner aPositioner)
    {
        mDriveTrain = aDriveTrain;
        mPositioner = aPositioner;

        mInDeadbandHelper = new InDeadbandHelper(10);
        mControlMode = ControlMode.Off;
        mDistanceControlParams = new DistanceControlParams(0, 0);
        mTurningControlParams = new TurnControlParams(0, 0);
    }

    @Override
    public void init()
    {

    }

    @Override
    public void update()
    {

    }

    /**
     * Setting the goal for the driveDistance command
     * 
     * @param aDesiredDistance
     *            in inches
     */
    public void setDistanceGoal(double aDistance, double aGoalSpeed)
    {
        mControlMode = ControlMode.Distance;
        mDistanceControlParams = new DistanceControlParams(mPositioner.getTotalDistance() + aDistance, aGoalSpeed);
    }

    @Override
    public void setTurnGoal(double aAngle, double aGoalSpeed)
    {
        mControlMode = ControlMode.Turning;
        mTurningControlParams = new TurnControlParams(aAngle, aGoalSpeed);
    }

    @Override
    public void setGoToPositionInStepsGoal(double aX, double aY, double aSpeed)
    {
        mControlMode = ControlMode.PositionInSteps;
        mGoToPositionSubstep = GoToPositionSubsteps.Turning;

        double dx = aX - mPositioner.getXPosition();
        double dy = aY - mPositioner.getXPosition();

        double distanceAway = Math.sqrt(dx * dx + dy * dy);
        double goalAngle = Math.toDegrees(Math.atan2(dx, dy)); //Switched on purpose

        mDistanceControlParams = new DistanceControlParams(mPositioner.getTotalDistance() + distanceAway, aSpeed);
        mTurningControlParams = new TurnControlParams(goalAngle, aSpeed);
    }


    @Override
    public boolean executeControlMode()
    {
        boolean finished = false;
        
        switch(mControlMode)
        {
        case Off:
            finished = true;
            break;
        case Distance:
            finished = driveDistance();
            break;
        case Turning:
            finished = turnToAngle();
            break;
        case PositionInSteps:
            finished = driveToPositionInSteps();
            break;
        default:
            break;
        
        }

        if (finished)
        {
            mControlMode = ControlMode.Off;
            mDriveTrain.stop();
        }

        return finished;
    }

    @Override
    public void control()
    {

    }

    @Override
    public void rereadPreferences()
    {

    }

    @Override
    public void updateSmartDashboard()
    {
        String actionName = "";
        switch (mControlMode)
        {
        case Distance:
            actionName = "Driving Distance";
            break;
        case Turning:
            actionName = "Turning";
            break;
        case PositionInSteps:
            actionName = "Go To Position";
            switch (mGoToPositionSubstep)
            {
            case Driving:
                actionName += "::Driving";
                break;
            case Turning:
                actionName += "::Turning";
                break;
            case NoAction:
                actionName += "::No Action";
                break;
            }
            break;
        case Off:
        default:
            break;

        }
        SmartDashboard.putString(SmartDashBoardNames.sSNOBOT_ACTION, mGoToPositionSubstep.toString());
        SmartDashboard.putBoolean(SmartDashBoardNames.sIN_ACTION, isInAction());
        SmartDashboard.putString(SmartDashBoardNames.sSNOBOT_ACTION_NAME, actionName);
    }

    @Override
    public void updateLog()
    {

    }

    @Override
    public void stop()
    {
        mControlMode = ControlMode.Off;
        mGoToPositionSubstep = GoToPositionSubsteps.NoAction;
    }

    @Override
    public boolean isInAction()
    {
        return mControlMode != ControlMode.Off;
    }

    private boolean turnToAngle()
    {
        double error = mTurningControlParams.mGoalAngle - mPositioner.getOrientationDegrees();
        double turnMeasure = error % 360;

        if ((turnMeasure) < 0)
        {
            turnMeasure = turnMeasure + 360;
        }

        if (turnMeasure <= 180)
        {
            mDriveTrain.setLeftRightSpeed(mTurningControlParams.mTurningSpeed, -mTurningControlParams.mTurningSpeed);
        }
        else
        {
            mDriveTrain.setLeftRightSpeed(-mTurningControlParams.mTurningSpeed, mTurningControlParams.mTurningSpeed);
        }

        if (mInDeadbandHelper.isFinished(Math.abs(error) < 5))
        {
            mDriveTrain.setLeftRightSpeed(0, 0);
            return true;
        }

        return false;
    }

    private boolean driveDistance()
    {
        double error = mDistanceControlParams.mGoalDistance - mPositioner.getTotalDistance();
        boolean isFinished = false;

        if (mInDeadbandHelper.isFinished(Math.abs(error) < 6))
        {
            mDriveTrain.setLeftRightSpeed(0, 0);
            isFinished = true;
        }
        else if (error > 0)
        {
            mDriveTrain.setLeftRightSpeed(mDistanceControlParams.mDrivingSpeed, mDistanceControlParams.mDrivingSpeed);
        }
        else
        {
            mDriveTrain.setLeftRightSpeed(-mDistanceControlParams.mDrivingSpeed, -mDistanceControlParams.mDrivingSpeed);
        }

        return isFinished;
    }

    private boolean driveToPositionInSteps()
    {
        boolean finished = false;

        switch (mGoToPositionSubstep)
        {
        case Turning:
        {
            boolean done = turnToAngle();
            if (done)
            {
                mGoToPositionSubstep = GoToPositionSubsteps.Driving;
            }
            break;
        }
        case Driving:
        {
            boolean done = driveDistance();
            if (done)
            {
                mGoToPositionSubstep = GoToPositionSubsteps.NoAction;
                finished = true;
            }
            break;
        }
        case NoAction:
        {
            finished = true;
            break;
        }
        }

        return finished;
    }
}
