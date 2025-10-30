package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Класс, инкапсулирующий всю логику управления механизмами робота (захват, выброс и т.д.).
 */
public class MechanismController {

    private final RobotHardware robot;

    private boolean outtakeActive = false;
    private boolean intakeActive = false;
    private boolean prevRightBumper = false;
    private boolean prevLeftBumper = false;

    /**
     * Конструктор, принимающий ссылку на оборудование робота.
     * @param robot Инициализированный объект RobotHardware.
     */
    public MechanismController(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Главный метод, который должен вызываться в каждом цикле TeleOp.
     * Обрабатывает ввод с геймпада и управляет механизмами.
     * @param gamepad Геймпад, используемый для управления.
     */
    public void update(Gamepad gamepad) {
        // Toggle-контроль для Outtake (R1)
        if (togglePressed(gamepad.right_bumper, prevRightBumper)) outtakeActive = !outtakeActive;
        prevRightBumper = gamepad.right_bumper;

        double velocity = outtakeActive ? RobotHardware.OUTTAKE_VELOCITY : 0;
        safeSetVelocity(robot.outtake1, velocity);
        safeSetVelocity(robot.outtake2, velocity);

        // Toggle-контроль для Intake (L1)
        if (togglePressed(gamepad.left_bumper, prevLeftBumper)) intakeActive = !intakeActive;
        prevLeftBumper = gamepad.left_bumper;

        safeSetPower(robot.intake1, intakeActive ? -1.0 : 0);

        // Управление вторым мотором захвата (кнопка 'X', режим удержания)
        safeSetPower(robot.intake2, gamepad.x ? 1.0 : 0);
    }

    // --- Геттеры для телеметрии ---
    public boolean isOuttakeActive() {
        return outtakeActive;
    }

    public boolean isIntakeActive() {
        return intakeActive;
    }

    // --- Вспомогательные методы ---

    private boolean togglePressed(boolean current, boolean previous) {
        return current && !previous;
    }

    private void safeSetPower(DcMotor motor, double power) {
        if (motor != null) motor.setPower(power);
    }

    private void safeSetVelocity(DcMotorEx motor, double velocity) {
        if (motor != null) motor.setVelocity(velocity);
    }
}
