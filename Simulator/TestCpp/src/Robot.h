/*
 * Robot.h
 *
 *  Created on: May 10, 2017
 *      Author: PJ
 */

#ifndef SRC_ROBOT_H_
#define SRC_ROBOT_H_


#include "Drivetrain/IDrivetrain.h"
#include "Gearboss/IGearBoss.h"
#include "Climber/IClimber.h"
#include "Positioner/IPositioner.h"

#include "SnobotLib/ASnobot.h"

class Robot: public ASnobot
{
public:
    void RobotInit();

protected:

    std::shared_ptr<IDrivetrain> mDrivetrain;
    std::shared_ptr<IGearBoss> mGearBoss;
    std::shared_ptr<IClimber> mClimber;
    std::shared_ptr<IPositioner> mPositioner;

};

#endif /* SRC_ROBOT_H_ */
