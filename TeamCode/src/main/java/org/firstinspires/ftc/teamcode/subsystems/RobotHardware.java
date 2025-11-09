package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotHardware {

    // --- Константы ---
    // Вы можете настраивать эти PIDF в будущем для более точной скорости
    private static final PIDFCoefficients SHOOTER_PIDF = new PIDFCoefficients(10, 0, 0, 12);

    // --- Оборудование ---
    public DcMotor intakeMotor;
    public DcMotorEx shooterMotor;
    public Servo liftServo;

    /**
     * Инициализирует все оборудование робота.
     * @param hardwareMap HardwareMap, предоставляемый OpMode.
     * @param telemetry Telemetry для вывода сообщений об ошибках.
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        // --- Инициализация в безопасном режиме ---
        intakeMotor = getDevice(hardwareMap, telemetry, DcMotor.class, "intakeMotor");
        //shooterMotor = getDevice(hardwareMap, telemetry, DcMotorEx.class, "shooterMotor");
        //liftServo = getDevice(hardwareMap, telemetry, Servo.class, "liftServo");

        // --- Настройка оборудования ---
        if (intakeMotor != null) {
            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (shooterMotor != null) {
            shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor.setVelocityPIDFCoefficients(SHOOTER_PIDF.p, SHOOTER_PIDF.i, SHOOTER_PIDF.d, SHOOTER_PIDF.f);
        }
    }

    /**
     * Вспомогательный метод для безопасной инициализации устройств.
     * @return Возвращает устройство или null, если оно не найдено.
     */
    private <T> T getDevice(HardwareMap hardwareMap, Telemetry telemetry, Class<T> deviceClass, String name) {
        try {
            return hardwareMap.get(deviceClass, name);
        } catch (Exception e) {
            telemetry.addLine("❌ Не удалось найти устройство: " + name);
            return null;
        }
    }
}
