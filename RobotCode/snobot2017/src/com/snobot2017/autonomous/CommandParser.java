package com.snobot2017.autonomous;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.snobot.lib.autonomous.ACommandParser;
import com.snobot.lib.motion_profile.ISetpointIterator;
import com.snobot.lib.motion_profile.PathConfig;
import com.snobot.lib.motion_profile.PathGenerator;
import com.snobot.lib.motion_profile.PathSetpoint;
import com.snobot.lib.motion_profile.StaticSetpointIterator;
import com.snobot2017.Properties2017;
import com.snobot2017.SmartDashBoardNames;
import com.snobot2017.Snobot2017;
import com.snobot2017.autonomous.AutonomousFactory.StartingPositions;
import com.snobot2017.autonomous.path.DriveStraightPath;
import com.snobot2017.autonomous.path.DriveTurnPath;
import com.snobot2017.autonomous.trajectory.TrajectoryPathCommand;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.io.TextFileDeserializer;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * Creates commands from a file path and adds them to a CommandGroup.
 * 
 * @author Alec/Andrew
 *
 */
public class CommandParser extends ACommandParser
{
    private static final double sEXPECTED_DT = .02;

    protected Snobot2017 mSnobot;
    protected SendableChooser<StartingPositions> mPositionChooser;

    /**
     * Creates a CommandParser object.
     * 
     * @param aSnobot
     *            The robot using the CommandParser.
     * @param aPositionChooser
     * @param aStartPosition
     */
    public CommandParser(Snobot2017 aSnobot, SendableChooser<StartingPositions> aPositionChooser)
    {
        super(NetworkTable.getTable(SmartDashBoardNames.sAUTON_TABLE_NAME), SmartDashBoardNames.sROBOT_COMMAND_TEXT,
                SmartDashBoardNames.sSUCCESFULLY_PARSED_AUTON, " ", "#");

        mSnobot = aSnobot;
        mPositionChooser = aPositionChooser;

    }

    /**
     * Takes a list of Strings and creates a Command.
     * 
     * @param args
     *            The command's name and parameters.
     */
    @Override
    protected Command parseCommand(List<String> args)
    {
        String commandName = args.get(0);
        Command newCommand = null;
        try
        {
            switch (commandName)
            {
            case AutonomousCommandNames.sPARALLEL_COMMAND:
            {
                newCommand = parseParallelCommand(args);
                break;
            }
            case AutonomousCommandNames.sWAIT_COMMAND:
            {
                newCommand = parseWaitCommand(args);
                break;
            }
            case AutonomousCommandNames.sSTUPID_DRIVE_STRAIGHT_COMMAND:
            {
                newCommand = parseStupidDriveStraightCommand(args);
                break;
            }
            case AutonomousCommandNames.sSCORE_GEAR_COMMAND:
            {
                newCommand = parseScoreGearCommand(args);
                break;
            }
            case AutonomousCommandNames.sDRIVE_STRAIGHT_A_DISTANCE:
            {
                newCommand = parseDriveStraightADistance(args);
                break;
            }
            case AutonomousCommandNames.sTURN_WITH_DEGREES:
            {
                newCommand = parseTurnWithDegrees(args);
                break;
            }
            case AutonomousCommandNames.sDRIVE_PATH_STRAIGHT:
            {
                newCommand = createDrivePathCommand(args);
                break;
            }
            case AutonomousCommandNames.sDRIVE_PATH_TURN:
            {
                newCommand = createTurnPathCommand(args);
                break;
            }
            case AutonomousCommandNames.sDRIVE_TRAJECTORY:
            {
                newCommand = createTrajectoryCommand(args.get(1));
                break;
            }
            case AutonomousCommandNames.sREPLAY:
            {
                newCommand = parseReplayCommand(args);
                break;
            }
            case AutonomousCommandNames.sGO_TO_POSITION_SMOOTH_IN_STEPS:
            {
                newCommand = parseGoToPositionInStepsCommand(args);
                break;
            }

            case AutonomousCommandNames.sSCORE_GEAR_TRAJECTORY:
            {
                newCommand = createScoreGearWithTrajectoryCommand();
                break;
            }
            case AutonomousCommandNames.sSTART_HOPPER_TRAJ:
            {
                newCommand = createGetHoppersWithTrajectoryCommand(args);
                break;
            }
            case AutonomousCommandNames.sGET_HOPPER_AND_GEAR:
            {
                newCommand = createGetHoppersAndGetGearWithTrajectoryCommand(args);
                break;
            }
            case AutonomousCommandNames.sGET_GEAR_AND_BOILER:
            {
                newCommand = createScoreFuelWithTrajectoryCommand();
                break;
            }
            default:
                addError("Received unexpected command name '" + commandName + "'");
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            addError("You have not specified enough aguments for the command: " + commandName + ". " + e.getMessage());
        }
        catch (Exception e)
        {
            addError("Failed to parse the command: " + commandName + ". " + e.getMessage());
            e.printStackTrace();
        }
        return newCommand;
    }

    private Command createTurnPathCommand(List<String> args)
    {
        PathConfig dudePathConfig = new PathConfig(Double.parseDouble(args.get(1)), // Endpoint
                Double.parseDouble(args.get(2)), // Max Velocity
                Double.parseDouble(args.get(3)), // Max Acceleration
                sEXPECTED_DT);

        ISetpointIterator dudeSetpointIterator;

        // TODO create dynamic iterator, way to switch
        if (true)
        {
            dudeSetpointIterator = new StaticSetpointIterator(dudePathConfig);
        }

        return new DriveTurnPath(mSnobot.getDriveTrain(), mSnobot.getPositioner(), dudeSetpointIterator);
    }

    private Command createDrivePathCommand(List<String> args)
    {
        PathConfig dudePathConfig = new PathConfig(Double.parseDouble(args.get(1)), // Endpoint
                Double.parseDouble(args.get(2)), // Max Velocity
                Double.parseDouble(args.get(3)), // Max Acceleration
                sEXPECTED_DT);

        ISetpointIterator dudeSetpointIterator;

        // TODO create dynamic iterator, way to switch
        if (true)
        {
            PathGenerator dudePathGenerator = new PathGenerator();
            List<PathSetpoint> dudeList = dudePathGenerator.generate(dudePathConfig);
            dudeSetpointIterator = new StaticSetpointIterator(dudeList);
        }

        return new DriveStraightPath(mSnobot.getDriveTrain(), mSnobot.getPositioner(), dudeSetpointIterator);
    }

    private Command createTrajectoryCommand(String aFile)
    {
        String pathFile = Properties2017.sAUTON_PATH_DIRECTORY.getValue() + "/" + aFile.trim();
        TextFileDeserializer deserializer = new TextFileDeserializer();
        Path p = deserializer.deserializeFromFile(pathFile);

        Command output = null;
        if (p == null)
        {
            addError("Could not read path file " + pathFile);
        }
        else
        {
            output = new TrajectoryPathCommand(mSnobot.getDriveTrain(), mSnobot.getPositioner(), p);
        }

        return output;
    }

    private Command createScoreFuelWithTrajectoryCommand()
    {
        StartingPositions startPosition = mPositionChooser.getSelected();
        if (startPosition == null)
        {
            return null;
        }

        String scoreFilename = null;
        String boilFilename = null;

        switch (startPosition)
        {
        case RedRight:
            scoreFilename = "RedRightScoreGear.csv";
            boilFilename = "RedRightGearToBoiler.csv";
            break;
        case RedMiddle:
            scoreFilename = "RedMiddleScoreGear.csv";
            break;
        case RedLeft:
            scoreFilename = "RedLeftScoreGear.csv";
            break;
        case BlueRight:
            scoreFilename = "BlueRightScoreGear.csv";
            break;
        case BlueMiddle:
            scoreFilename = "BlueMiddleScoreGear.csv";
            break;
        case BlueLeft:
            scoreFilename = "BLueLeftScoreGear.csv";
            boilFilename = "BlueLeftGearToBoiler.csv";
            break;
        default:
            break;
        }
        CommandGroup output = new CommandGroup();

        if (scoreFilename != null)
        {
            output.addSequential(createTrajectoryCommand(scoreFilename));
            output.addSequential(parseScoreGearCommand(3));

            if (boilFilename != null)
            {
                // TODO
                System.out.println("********************  ADD BOILER TRAJECTORY *********************");
                output.addSequential(createTrajectoryCommand(boilFilename));
            }
            else
            {
                output.addSequential(parseStupidDriveStraightCommand(1.1, -.3));
            }

        }
        else
        {
            mErrorText += "Invalid scoring filename";
        }

        return output;
    }

    private Command createScoreGearWithTrajectoryCommand()
    {
        StartingPositions startPosition = mPositionChooser.getSelected();
        if (startPosition == null)
        {
            return null;
        }

        String fileName = null;

        switch (startPosition)
        {
        case RedLeft:
            fileName = "RedLeftScoreGear.csv";
            break;
        case RedMiddle:
            fileName = "RedMiddleScoreGear.csv";
            break;
        case RedRight:
            fileName = "RedRightScoreGear.csv";
            break;
        case BlueRight:
            fileName = "BlueRightScoreGear.csv";
            break;
        case BlueMiddle:
            fileName = "BlueMiddleScoreGear.csv";
            break;
        case BlueLeft:
            fileName = "BlueLeftScoreGear.csv";
            break;
        default:
            break;
        }

        if (fileName != null)
        {
            return createTrajectoryCommand(fileName);
        }
        else
        {
            addError("Invalid start selection for the current robot start position command : " + startPosition);
            return null;
        }
    }

    private Command createGetHoppersWithTrajectoryCommand(List<String> args)
    {
        StartingPositions startPosition = mPositionChooser.getSelected();
        if (startPosition == null)
        {
            return null;
        }

        boolean doClose = true;
        if (args.size() > 1)
        {
            if (args.get(1).equals("Far"))
            {
                doClose = false;
            }
        }

        String fileName = null;

        switch (startPosition)
        {
        case RedLeft:
            if (doClose)
            {
                fileName = "RedLeftToHopperFive.csv";
            }
            else
            {
                fileName = "RedLeftToHopperFour.csv";
            }
            break;
        case RedMiddle:
            if (doClose)
            {
                fileName = "RedMiddleToHopperOne.csv";
            }
            else
            {
                fileName = "RedMiddleToHopperFive.csv";
            }
            break;
        case RedRight:
            if (doClose)
            {
                fileName = "RedRightToHopperOne.csv";
            }
            else
            {
                fileName = "RedRightToHopperTwo.csv";
            }
            break;
        case BlueRight:
            if (doClose)
            {
                fileName = "BlueRightToHopperFour.csv";
            }
            else
            {
                fileName = "BlueRightToHopperFive.csv";
            }
            break;
        case BlueMiddle:
            if (doClose)
            {
                fileName = "BlueMiddleToHopperThree.csv";
            }
            else
            {
                fileName = "BlueMiddleToHopperFour.csv";
            }
            break;
        case BlueLeft:
            if (doClose)
            {
                fileName = "BlueLeftToHopperThree.csv";
            }
            else
            {
                fileName = "BlueLeftToHopperTwo.csv";
            }
            break;
        default:
            break;
        }

        if (fileName != null)
        {
            CommandGroup output = new CommandGroup();
            output.addSequential(createTrajectoryCommand(fileName));
            output.addSequential(parseScoreGearCommand(3));
            return output;
        }
        else
        {
            addError("Invalid start selection for the current robot start position command : " + startPosition);
            return null;
        }
    }

    private Command createGetHoppersAndGetGearWithTrajectoryCommand(List<String> args)
    {
        StartingPositions startPosition = mPositionChooser.getSelected();
        if (startPosition == null)
        {
            return null;
        }

        String scoreFilename = null;
        String hopperFilename = null;

        switch (startPosition)
        {
        case RedLeft:
            scoreFilename = "RedLeftScoreGear.csv";
            hopperFilename = "RedLeftScoreGearGetHopper.csv";
            break;
        case RedRight:
            scoreFilename = "RedRightScoreGear.csv";
            hopperFilename = "RedRightScoreGearGetHopper.csv";
            break;
        case BlueRight:
            scoreFilename = "BlueRightScoreGear.csv";
            hopperFilename = "BlueRightScoreGearGetHopper.csv";
            break;
        case BlueLeft:
            scoreFilename = "BlueLeftScoreGear.csv";
            hopperFilename = "BlueLeftScoreGearGetHopper.csv";
            break;

        case RedMiddle:
            scoreFilename = "RedMiddleScoreGear.csv";
            break;
        case BlueMiddle:
            scoreFilename = "BlueMiddleScoreGear.csv";
            break;

        // Intentional fall through, nothing to do
        case Origin:
        default:
            break;
        }

        CommandGroup output = new CommandGroup();

        if (scoreFilename != null)
        {
            output.addSequential(createTrajectoryCommand(scoreFilename));
            output.addSequential(parseScoreGearCommand(3));

            if (hopperFilename != null)
            {
                output.addSequential(createTrajectoryCommand(hopperFilename));
            }
            else
            {
                output.addSequential(parseStupidDriveStraightCommand(1.1, -.3));
            }

        }
        else
        {
            mErrorText += "Invalid scoring filename";
        }

        return output;
    }

    /**
     * 
     * @param args
     * @return
     */
    private Command parseTurnWithDegrees(List<String> args)
    {
        double speed = Double.parseDouble(args.get(1));
        double angle = Double.parseDouble(args.get(2));
        return new TurnWithDegrees(speed, angle, mSnobot.getSnobotActor());
    }

    private Command parseDriveStraightADistance(List<String> args)
    {
        double distance = Double.parseDouble(args.get(1));
        double speed = Double.parseDouble(args.get(2));
        return new DriveStraightADistance(distance, speed, mSnobot.getSnobotActor());
    }

    private Command parseScoreGearCommand(List<String> args)
    {
        double time = Double.parseDouble(args.get(1));
        return parseScoreGearCommand(time);
    }

    private Command parseScoreGearCommand(double aTime)
    {
        return new ScoreGear(mSnobot.getGearBoss(), aTime);
    }

    private Command parseStupidDriveStraightCommand(List<String> args)
    {
        double time = Double.parseDouble(args.get(1));
        double speed = Double.parseDouble(args.get(2));
        return parseStupidDriveStraightCommand(time, speed);
    }

    private Command parseStupidDriveStraightCommand(double aTime, double aSpeed)
    {
        return new StupidDriveStraight(mSnobot.getDriveTrain(), aTime, aSpeed);
    }

    private Command parseReplayCommand(List<String> args) throws IOException
    {
        String autoPath = Properties2017.sREPLAY_PATH.getValue() + args.get(1);
        return new Replay(mSnobot.getDriveTrain(), autoPath);
    }

    private Command parseGoToPositionInStepsCommand(List<String> args) throws IOException
    {
        double x = Double.parseDouble(args.get(1));
        double y = Double.parseDouble(args.get(2));
        double speed = .5;
        if (args.size() > 3)
        {
            speed = Double.parseDouble(args.get(3));
        }

        return new GoToPositionInSteps(x, y, speed, mSnobot.getSnobotActor());
    }

    protected Command parseWaitCommand(List<String> args)
    {
        double time = Double.parseDouble(args.get(1));
        return new WaitCommand(time);
    }

    @Override
    public CommandGroup readFile(String aFilePath)
    {
        if (aFilePath == null)
        {
            aFilePath = "NOT FOUND!";
        }

        mAutonTable.putString(SmartDashBoardNames.sAUTON_FILENAME, aFilePath);
        return super.readFile(aFilePath);
    }

    public void saveAutonMode()
    {
        String new_text = mAutonTable.getString(SmartDashBoardNames.sROBOT_COMMAND_TEXT, "");
        String filename = mAutonTable.getString(SmartDashBoardNames.sAUTON_FILENAME, "auton_file.txt");

        System.out.println("*****************************************");
        System.out.println("Saving auton mode to " + filename);
        System.out.println("*****************************************");

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
            bw.write(new_text);
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mAutonTable.putBoolean(SmartDashBoardNames.sSAVE_AUTON, false);
    }
}
