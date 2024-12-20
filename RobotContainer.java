package frc.robot;

import frc.robot.subsystems.Motor;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
    private final Motor motorSubsystem = new Motor(Constants.KRACKEN_MOTOR_CAN_ID);
    private final XboxController xboxController = new XboxController(Constants.XBOX_CONTROLLER_PORT);

    private boolean isMotorRunningForward = false;
    private boolean isMotorRunningBackward = false;

    public RobotContainer() {
        configureButtonBindings();

        // Default teleop command to control motor with the joystick
        motorSubsystem.setDefaultCommand(
            new RunCommand(() -> {
                double speed = -xboxController.getLeftY(); // Joystick forward/backward

                // Deadband to ignore small joystick movements
                if (Math.abs(speed) < Constants.DEADBAND_THRESHOLD) {
                    speed = 0;
                }

                if (!isMotorRunningForward && !isMotorRunningBackward) {
                    motorSubsystem.setMotorSpeed(speed * 0.5); // Scale the speed
                }
            }, motorSubsystem)
        );
    }

    private void configureButtonBindings() {
        // A Button: Start motor forward
        new JoystickButton(xboxController, XboxController.Button.kA.value)
            .onTrue(new InstantCommand(() -> {
                motorSubsystem.setMotorSpeed(Constants.AUTO_SPEED);
                isMotorRunningForward = true;
                isMotorRunningBackward = false;
            }, motorSubsystem));

        // Y Button: Start motor backward
        new JoystickButton(xboxController, XboxController.Button.kY.value)
            .onTrue(new InstantCommand(() -> {
                motorSubsystem.setMotorSpeed(-Constants.AUTO_SPEED);
                isMotorRunningForward = false;
                isMotorRunningBackward = true;
            }, motorSubsystem));

        // B Button: Stop the motor
        new JoystickButton(xboxController, XboxController.Button.kB.value)
            .onTrue(new InstantCommand(() -> {
                motorSubsystem.stopMotor();
                isMotorRunningForward = false;
                isMotorRunningBackward = false;
            }, motorSubsystem));
    }

    public Command getTeleopCommand() {
        return new RunCommand(() -> {
            double speed = -xboxController.getLeftY();

            if (Math.abs(speed) < Constants.DEADBAND_THRESHOLD) {
                speed = 0;
            }

            if (!isMotorRunningForward && !isMotorRunningBackward) {
                motorSubsystem.setMotorSpeed(speed * 0.5); 
            }
        }, motorSubsystem);
    }

    public Command getAutonomousCommand() {
        return new InstantCommand(() -> motorSubsystem.setMotorSpeed(Constants.AUTO_SPEED), motorSubsystem)
            .andThen(new InstantCommand(motorSubsystem::stopMotor, motorSubsystem))
            .withTimeout(Constants.AUTO_DURATION);
    }
}
