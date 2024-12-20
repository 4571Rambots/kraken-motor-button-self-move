package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    private RobotContainer robotContainer;
    private Command teleopCommand;

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();
    }

    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll(); 

        teleopCommand = robotContainer.getTeleopCommand();
        if (teleopCommand != null) teleopCommand.schedule();
    }

    @Override
    public void teleopPeriodic() {
        CommandScheduler.getInstance().run(); 
    }

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().cancelAll();
    }
}
