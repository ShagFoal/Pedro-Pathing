package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ShooterSystem {

    // --- Константы ---
    private static final double SHOOTER_VELOCITY = 2000;
    private static final double LIFT_SHOOT_POSITION = 0.5;
    private static final double LIFT_IDLE_POSITION = 0.0;
    private static final double LIFT_RETURN_DELAY_MS = 500; // 0.5 секунды задержка перед возвратом серво

    private final RobotHardware robot;
    private final ElapsedTime shootTimer = new ElapsedTime();

    private boolean isShooting = false;

    public ShooterSystem(RobotHardware robot) {
        this.robot = robot;
    }

    /**
     * Обновляет состояние системы стрельбы.
     * @param shootButtonPressed Нажата ли кнопка выстрела (R1).
     */
    public void update(boolean shootButtonPressed) {
        if (robot.shooterMotor == null || robot.liftServo == null) return;

        if (shootButtonPressed) {
            // Если кнопка нажата, включаем шутер и сбрасываем таймер
            robot.shooterMotor.setVelocity(SHOOTER_VELOCITY);
            robot.liftServo.setPosition(LIFT_SHOOT_POSITION);
            shootTimer.reset();
            isShooting = true;
        } else {
            // Если кнопка отпущена, выключаем шутер
            robot.shooterMotor.setVelocity(0);
            isShooting = false;

            // И если прошло достаточно времени после последнего выстрела, возвращаем серво
            if (shootTimer.milliseconds() > LIFT_RETURN_DELAY_MS) {
                robot.liftServo.setPosition(LIFT_IDLE_POSITION);
            }
        }
    }

    public boolean isShooting() {
        return isShooting;
    }
}
