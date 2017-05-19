
#include "Robot.h"
#include "Talon.h"

void Robot::RobotInit()
{
    std::cout << "Robot init !!!" << std::endl;
    mLeftMotor = std::shared_ptr<SpeedController>(new Talon(0));
    mRightMotor = std::shared_ptr<SpeedController>(new Talon(1));

    mDriverJoystick = std::shared_ptr<Joystick>(new Joystick(1));
}

void Robot::AutonomousInit()
{

}

void Robot::AutonomousPeriodic()
{

}

void Robot::TeleopInit()
{

}

void Robot::TeleopPeriodic()
{
    mLeftMotor->Set(mDriverJoystick->GetRawAxis(1));
    mRightMotor->Set(mDriverJoystick->GetRawAxis(5));
}

void Robot::TestPeriodic()
{

}

START_ROBOT_CLASS(Robot)
