package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Motor Test", group = "Test")
public class MotorTestTeleOp extends LinearOpMode {

    DcMotor motor0, motor1, motor2, motor3;

    @Override
    public void runOpMode() {

        // Подключаем моторы по имени из конфигурации
        motor0 = hardwareMap.get(DcMotor.class, "LF");
        motor1 = hardwareMap.get(DcMotor.class, "LB");
        motor2 = hardwareMap.get(DcMotor.class, "RF");
        motor3 = hardwareMap.get(DcMotor.class, "RB");

        telemetry.addLine("Готов к тесту. Нажми PLAY.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Каждая кнопка включает один мотор
            if (gamepad1.a) {
                motor0.setPower(0.5);
                telemetry.addLine("motor0 работает");
            } else motor0.setPower(0);

            if (gamepad1.b) {
                motor1.setPower(0.5);
                telemetry.addLine("motor1 работает");
            } else motor1.setPower(0);

            if (gamepad1.x) {
                motor2.setPower(0.5);
                telemetry.addLine("motor2 работает");
            } else motor2.setPower(0);

            if (gamepad1.y) {
                motor3.setPower(0.5);
                telemetry.addLine("motor3 работает");
            } else motor3.setPower(0);

            telemetry.update();
        }
    }
}
