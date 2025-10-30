package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotHardware {

    // --- Константы для механизмов ---
    public static final PIDFCoefficients OUTTAKE_PIDF = new PIDFCoefficients(25, 0, 5, 14);
    public static final double OUTTAKE_VELOCITY = -1500;

    // --- Объявление оборудования ---
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public DcMotorEx outtake1, outtake2;
    public DcMotor intake1, intake2;

    private DcMotor[] allMotors;

    public void init(HardwareMap hwMap, Telemetry telemetry) {
        telemetry.addLine("⌛ Инициализация оборудования...");

        initDriveMotors(hwMap, telemetry);
        initMechanisms(hwMap, telemetry);

        allMotors = new DcMotor[]{frontLeft, frontRight, backLeft, backRight, outtake1, outtake2, intake1, intake2};
        for (DcMotor motor : allMotors) {
            if (motor != null) {
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }

        telemetry.addLine("✅ Оборудование инициализировано.");
    }

    private void initDriveMotors(HardwareMap hwMap, Telemetry telemetry) {
        frontLeft = getDevice(hwMap, "frontLeft", DcMotor.class, telemetry);
        frontRight = getDevice(hwMap, "frontRight", DcMotor.class, telemetry);
        backLeft = getDevice(hwMap, "backLeft", DcMotor.class, telemetry);
        backRight = getDevice(hwMap, "backRight", DcMotor.class, telemetry);

        if (frontLeft != null && backLeft != null) {
            frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    private void initMechanisms(HardwareMap hwMap, Telemetry telemetry) {
        outtake1 = getDevice(hwMap, "outtake1", DcMotorEx.class, telemetry);
        outtake2 = getDevice(hwMap, "outtake2", DcMotorEx.class, telemetry);
        intake1 = getDevice(hwMap, "intake1", DcMotor.class, telemetry);
        intake2 = getDevice(hwMap, "intake2", DcMotor.class, telemetry);

        configureOuttakeMotor(outtake1);
        configureOuttakeMotor(outtake2);
    }
    
    private void configureOuttakeMotor(DcMotorEx motor) {
        if (motor != null) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setVelocityPIDFCoefficients(OUTTAKE_PIDF.p, OUTTAKE_PIDF.i, OUTTAKE_PIDF.d, OUTTAKE_PIDF.f);
        }
    }

    public void stopAll() {
        if (allMotors == null) return;
        for (DcMotor motor : allMotors) {
            if (motor != null) {
                motor.setPower(0);
            }
        }
    }

    public boolean allDriveMotorsInitialized() {
        return frontLeft != null && frontRight != null && backLeft != null && backRight != null;
    }

    private <T> T getDevice(HardwareMap hwMap, String name, Class<T> type, Telemetry telemetry) {
        try {
            return hwMap.get(type, name);
        } catch (Exception e) {
            telemetry.addLine("❌ Не удалось найти '" + name + "'");
            return null;
        }
    }
}
