package org.firstinspires.ftc.teamcode.subsystems;

public class IntakeSystem {

    // --- Константы ---
    private static final double INTAKE_POWER = 1.0;
    private static final double OUTTAKE_POWER = -0.8;

    private final RobotHardware robot;
    private boolean isRunning = false;

    public IntakeSystem(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Обновляет состояние системы забора/выпуска на основе ввода с геймпада.
     * Этот метод должен вызываться в каждом цикле TeleOp.
     * @param intakeButtonPressed Нажат ли триггер забора (R2).
     * @param outtakeButtonPressed Нажата ли кнопка выпуска (L1).
     */
    public void update(boolean intakeButtonPressed, boolean outtakeButtonPressed) {
        if (robot.intakeMotor == null) return; // Безопасная проверка

        if (intakeButtonPressed) {
            robot.intakeMotor.setPower(INTAKE_POWER);
            isRunning = true;
        } else if (outtakeButtonPressed) {
            robot.intakeMotor.setPower(OUTTAKE_POWER);
            isRunning = true;
        } else {
            robot.intakeMotor.setPower(0);
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
