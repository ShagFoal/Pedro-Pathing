package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystems.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSystem;

@TeleOp(name = "Main Competition TeleOp")
public class MainTeleOp extends OpMode {

    private Follower follower;
    private RobotHardware robot;
    private IntakeSystem intakeSystem;
    // private ShooterSystem shooterSystem; // Шутер пока отключен

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap, telemetry);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));
        follower.startTeleopDrive();

        intakeSystem = new IntakeSystem(robot);
        // shooterSystem = new ShooterSystem(robot); // Шутер пока отключен

        telemetry.addLine("Робот готов к запуску!");
        telemetry.update();
    }

    @Override
    public void loop() {
        follower.update();
        handleDriving();

        // --- Обновление подсистем ---
        // L1 (left_bumper) теперь отвечает за выталкивание
        intakeSystem.update(gamepad1.right_trigger > 0.5, gamepad1.left_bumper);
        // shooterSystem.update(gamepad1.right_bumper); // Шутер пока отключен

        updateTelemetry();
    }

    private void handleDriving() {
        double driveY = -gamepad1.left_stick_y;
        double driveX = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
        follower.setTeleOpDrive(driveY, driveX, turn, false);
    }

    private void updateTelemetry() {
        telemetry.clear();
        telemetry.addLine("--- Состояние робота ---");

        Pose currentPose = follower.getPose();
        telemetry.addData("X", "%.2f", currentPose.getX());
        telemetry.addData("Y", "%.2f", currentPose.getY());
        telemetry.addData("Heading (deg)", "%.2f", Math.toDegrees(currentPose.getHeading()));

        telemetry.addLine("\n--- Подсистемы ---");
        telemetry.addData("Intake/Outtake", intakeSystem.isRunning() ? "АКТИВЕН" : "ВЫКЛЮЧЕН");
        // telemetry.addData("Shooter/Lift", shooterSystem.isShooting() ? "СТРЕЛЯЕТ" : "ОЖИДАНИЕ"); // Шутер пока отключен
        telemetry.update();
    }
}
