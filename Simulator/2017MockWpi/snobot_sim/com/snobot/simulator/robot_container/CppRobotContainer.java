package com.snobot.simulator.robot_container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CppRobotContainer implements IRobotClassContainer
{
    private String mRobotClassName;
    private Class<?> mJniClass;

    public CppRobotContainer(String aRobotClassName)
    {
        mRobotClassName = aRobotClassName;
    }

    @Override
    public void constructRobot()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalArgumentException, InvocationTargetException
    {
        mJniClass = Class.forName(mRobotClassName);

        Method method = mJniClass.getMethod("createRobot");
        method.invoke(null);
    }

    @Override
    public void startCompetition()
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Method method = mJniClass.getMethod("startCompetition");
        method.invoke(null);
    }
}