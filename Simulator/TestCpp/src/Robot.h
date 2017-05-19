/*
 * Robot.h
 *
 *  Created on: May 10, 2017
 *      Author: PJ
 */

#ifndef SRC_ROBOT_H_
#define SRC_ROBOT_H_

#include <IterativeRobot.h>
#include "SpeedController.h"
#include "Joystick.h"

class Robot: public frc::IterativeRobot
{
public:
    void RobotInit();

    void AutonomousInit() override;

    void AutonomousPeriodic() override;

    void TeleopInit() override;

    void TeleopPeriodic() override;

    void TestPeriodic() override;

protected:

    std::shared_ptr<SpeedController> mLeftMotor;
    std::shared_ptr<SpeedController> mRightMotor;

    std::shared_ptr<Joystick> mDriverJoystick;

};

#endif /* SRC_ROBOT_H_ */
