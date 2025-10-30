package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Y-Axis Movement Test", group = "Autonomous")
public class MovementAutonomous extends OpMode {
    private Follower follower;

    // Пути для каждого сегмента движения
    private Path pathMoveLeft10, pathMoveBackToStart, pathMoveLeft15;

    // Состояние нашего простого автомата
    private int currentState = 0;

    @Override
    public void init() {
        // --- Определяем ключевые точки --- 
        Pose startPose = new Pose(0, 0, 0); // Начало в (0,0)
        Pose left10Pose = new Pose(0, 10, 0); // Точка в 10 ед. по Y
        Pose left15Pose = new Pose(0, 15, 0); // Точка в 15 ед. по Y

        // --- Инициализируем Follower ---
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        // --- Строим пути ---
        // 1. Движение от (0,0) до (0,10)
        pathMoveLeft10 = new Path(new BezierLine(startPose, left10Pose));
        pathMoveLeft10.setLinearHeadingInterpolation(0, 0); // Держим курс 0 градусов

        // 2. Движение от (0,10) обратно в (0,0)
        pathMoveBackToStart = new Path(new BezierLine(left10Pose, startPose));
        pathMoveBackToStart.setLinearHeadingInterpolation(0, 0);

        // 3. Движение от (0,0) до (0,15)
        pathMoveLeft15 = new Path(new BezierLine(startPose, left15Pose));
        pathMoveLeft15.setLinearHeadingInterpolation(0, 0);
        
        telemetry.addLine("Paths created. Ready to start.");
    }

    @Override
    public void start() {
        currentState = 0;
        follower.followPath(pathMoveLeft10); // Запускаем первый путь
    }

    @Override
    public void loop() {
        follower.update();

        // Автомат состояний для последовательного выполнения путей
        switch (currentState) {
            case 0:
                // Если первый путь завершен
                if (!follower.isBusy()) {
                    follower.followPath(pathMoveBackToStart); // Запускаем второй путь
                    currentState = 1;
                }
                break;
            case 1:
                // Если второй путь завершен
                if (!follower.isBusy()) {
                    follower.followPath(pathMoveLeft15); // Запускаем третий путь
                    currentState = 2;
                }
                break;
            case 2:
                // Если третий путь завершен
                if (!follower.isBusy()) {
                    currentState = 3; // Все готово
                }
                break;
        }

        // Телеметрия
        telemetry.addData("Current State", currentState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    @Override
    public void stop() {
        // Можно добавить логику для сохранения последней позиции, если нужно
    }
}
