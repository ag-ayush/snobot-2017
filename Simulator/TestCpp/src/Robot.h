/*
 * Robot.h
 *
 *  Created on: May 10, 2017
 *      Author: PJ
 */

#ifndef SRC_ROBOT_H_
#define SRC_ROBOT_H_

#include <IterativeRobot.h>

class Robot: public frc::IterativeRobot
{
public:
    void RobotInit();

    void AutonomousInit() override;

    void AutonomousPeriodic() override;

    void TeleopInit() override;

    void TeleopPeriodic() override;

    void TestPeriodic() override;

};

#endif /* SRC_ROBOT_H_ */
