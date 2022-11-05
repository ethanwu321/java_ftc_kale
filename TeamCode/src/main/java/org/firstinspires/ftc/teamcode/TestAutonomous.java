package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class TestAutonomous extends LinearOpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void runOpMode() {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

        frontLeft.setPower(0.4);
        frontRight.setPower(0.4);
        backLeft.setPower(0.4);
        backRight.setPower(0.4);
        sleep(300);
        frontLeft.setPower(-0.4);
        frontRight.setPower(-0.4);
        backLeft.setPower(-0.4);
        backRight.setPower(-0.4);
        sleep(300);
        frontLeft.setPower(-0.4);
        frontRight.setPower(0.4);
        backLeft.setPower(0.4);
        backRight.setPower(-0.4);
        sleep(700);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
