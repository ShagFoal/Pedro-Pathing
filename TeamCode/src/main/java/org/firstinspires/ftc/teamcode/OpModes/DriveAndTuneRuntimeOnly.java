package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Drive & Tune (Runtime Only)")
public class DriveAndTuneRuntimeOnly extends OpMode {

    private DcMotor rf, rb, lb, lf;

    private final double[] powerMultipliers = {1.0, 1.0, 1.0, 1.0};
    private int selectedMotorIndex = 0; // 0=RF, 1=RB, 2=LB, 3=LF
    private final String[] motorNames = {"RF", "RB", "LB", "LF"};
    private boolean prevDpadUp = false, prevDpadDown = false;
    private boolean prevY = false, prevA = false, prevX = false, prevB = false;
    private boolean prevBack = false;

    // --- Константы для калибровки и движения ---
    private static final double STEP = 0.01;
    private static final double MIN = 0.5;
    private static final double MAX = 1.4;
    private static final double TEST_POWER = 0.5; // Базовая мощность для тестового движения
    private static final double RAMP_RATE = 0.05; // Скорость нарастания мощности

    private double currentTestPower = 0.0; // Текущая, плавно изменяющаяся мощность

    @Override
    public void init() {
        rf = hardwareMap.get(DcMotor.class, "RF");
        rb = hardwareMap.get(DcMotor.class, "RB");
        lb = hardwareMap.get(DcMotor.class, "LB");
        lf = hardwareMap.get(DcMotor.class, "LF");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addLine("=== Drive & Tune ===");
        telemetry.addLine("Y,B,A,X — выбрать мотор (RF,RB,LB,LF)");
        telemetry.addLine("Dpad Up/Down — изменить множитель");
        telemetry.addLine("Dpad Left/Right — тест движения (с плавным стартом)");
        telemetry.addLine("BACK — сбросить все множители");
        telemetry.update();
    }

    @Override
    public void loop() {
        handleMotorSelection();
        handleTuningInputs();
        handleMovementTest(); // Теперь с плавным стартом
        updateTelemetry();
    }

    private void handleMotorSelection() {
        if (gamepad1.y && !prevY) selectedMotorIndex = 0;
        if (gamepad1.a && !prevA) selectedMotorIndex = 1;
        if (gamepad1.x && !prevX) selectedMotorIndex = 2;
        if (gamepad1.b && !prevB) selectedMotorIndex = 3;
        prevY = gamepad1.y;
        prevA = gamepad1.a;
        prevX = gamepad1.x;
        prevB = gamepad1.b;
    }

    private void handleTuningInputs() {
        if (gamepad1.dpad_up && !prevDpadUp)
            powerMultipliers[selectedMotorIndex] = Math.min(MAX, powerMultipliers[selectedMotorIndex] + STEP);

        if (gamepad1.dpad_down && !prevDpadDown)
            powerMultipliers[selectedMotorIndex] = Math.max(MIN, powerMultipliers[selectedMotorIndex] - STEP);

        if (gamepad1.back && !prevBack)
            resetAll();

        prevDpadUp = gamepad1.dpad_up;
        prevDpadDown = gamepad1.dpad_down;
        prevBack = gamepad1.back;
    }

    private void handleMovementTest() {
        double targetPower = 0.0;

        if (gamepad1.dpad_left) {
            targetPower = TEST_POWER;
        } else if (gamepad1.dpad_right) {
            targetPower = -TEST_POWER;
        }

        // --- Логика плавного старта и остановки ---
        if (currentTestPower < targetPower) {
            currentTestPower = Math.min(currentTestPower + RAMP_RATE, targetPower);
        } else if (currentTestPower > targetPower) {
            currentTestPower = Math.max(currentTestPower - RAMP_RATE, targetPower);
        }

        setMotorPowers(currentTestPower);
    }

    private void setMotorPowers(double basePower) {
        rf.setPower(basePower * powerMultipliers[0]);
        rb.setPower(basePower * powerMultipliers[1]);
        lb.setPower(basePower * powerMultipliers[2]);
        lf.setPower(basePower * powerMultipliers[3]);
    }

    private void updateTelemetry() {
        telemetry.clear();
        telemetry.addLine("=== Drive & Tune ===");
        for (int i = 0; i < 4; i++) {
            telemetry.addData(motorNames[i] + (i == selectedMotorIndex ? " <<" : ""), "%.3f", powerMultipliers[i]);
        }
        telemetry.addData("\nCurrent Test Power", "%.2f", currentTestPower);
        telemetry.addLine("\nBACK = сбросить все");
        telemetry.update();
    }

    private void resetAll() {
        for (int i = 0; i < powerMultipliers.length; i++) {
            powerMultipliers[i] = 1.0;
        }
    }
}
