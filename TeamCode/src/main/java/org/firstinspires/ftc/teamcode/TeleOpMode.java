package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "TeleOp Final", group = "Drive")
public class TeleOpMode extends LinearOpMode {

    // --- Константы для управления ---
    private static final double SLOW_MODE_MULTIPLIER = 0.4;
    private static final double NORMAL_MODE_MULTIPLIER = 1.0;

    // --- Объекты-контроллеры ---
    private final RobotHardware robot = new RobotHardware();
    private MechanismController mechanismController;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        mechanismController = new MechanismController(robot);

        if (!robot.allDriveMotorsInitialized()) {
            telemetry.addLine("\n❌ КРИТИЧЕСКАЯ ОШИБКА: Не найден один или несколько моторов шасси.");
            updateHardwareStatusTelemetry();
            telemetry.update();
            while (opModeInInit()) { sleep(100); }
            return;
        }

        updateHardwareStatusTelemetry();
        telemetry.addLine("\n✅ TeleOp готов. Нажми INIT → PLAY");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            handleDriving(gamepad1);
            mechanismController.update(gamepad1);
            updateTelemetry(gamepad1);
        }
        
        robot.stopAll();
    }

    private void handleDriving(Gamepad gamepad) {
        double speedMultiplier = gamepad.left_trigger > 0.5 ? SLOW_MODE_MULTIPLIER : NORMAL_MODE_MULTIPLIER;

        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = gamepad.right_stick_x;

        double fl = (y + x + rx) * speedMultiplier;
        double bl = (y - x + rx) * speedMultiplier;
        double fr = (y - x - rx) * speedMultiplier;
        double br = (y + x - rx) * speedMultiplier;

        double max = Math.max(1.0, Math.max(Math.abs(fl), Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br)))));
        fl /= max; fr /= max; bl /= max; br /= max;

        safeSetPower(robot.frontLeft, fl);
        safeSetPower(robot.backLeft, bl);
        safeSetPower(robot.frontRight, fr);
        safeSetPower(robot.backRight, br);
    }

    private void updateTelemetry(Gamepad gamepad) {
        telemetry.clear();
        telemetry.addLine("--- Управление ---");
        telemetry.addData("Режим скорости", gamepad.left_trigger > 0.5 ? "SLOW MODE" : "NORMAL");
        telemetry.addData("Outtake", mechanismController.isOuttakeActive() ? "ACTIVE" : "INACTIVE");
        telemetry.addData("Intake", mechanismController.isIntakeActive() ? "ACTIVE" : "INACTIVE");
        telemetry.addLine("\n--- Моторы ---");
        if (robot.outtake1 != null) telemetry.addData("Outtake Velo", "%.1f", robot.outtake1.getVelocity());
        if (robot.intake1 != null) telemetry.addData("Intake Power", "%.2f", robot.intake1.getPower());
        telemetry.update();
    }

    private void updateHardwareStatusTelemetry() {
        telemetry.addLine("\n--- Статус оборудования ---");
        telemetry.addData("Шасси FL", robot.frontLeft != null ? "OK" : "❌");
        telemetry.addData("Шасси FR", robot.frontRight != null ? "OK" : "❌");
        telemetry.addData("Шасси BL", robot.backLeft != null ? "OK" : "❌");
        telemetry.addData("Шасси BR", robot.backRight != null ? "OK" : "❌");
        telemetry.addData("Outtake 1", robot.outtake1 != null ? "OK" : "❌");
        telemetry.addData("Outtake 2", robot.outtake2 != null ? "OK" : "❌");
        telemetry.addData("Intake 1", robot.intake1 != null ? "OK" : "❌");
        telemetry.addData("Intake 2", robot.intake2 != null ? "OK" : "❌");
    }

    private void safeSetPower(DcMotor motor, double power) {
        if (motor != null) motor.setPower(power);
    }
}
