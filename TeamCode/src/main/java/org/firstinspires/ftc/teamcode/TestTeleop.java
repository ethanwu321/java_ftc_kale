package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TestTeleOp extends OpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x * 0.4;
        double y = -gamepad1.left_stick_y * 0.4;
        double r = gamepad1.right_stick_x * 0.4;

        frontLeft.setPower(y + r + x);
        frontRight.setPower(y - r - x);
        backLeft.setPower(y + r - x);
        backRight.setPower(y - r + x);
    }
}
