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
    float right_trigger;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double y = -gamepad1.left_stick_y * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double r = gamepad1.right_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);

        frontLeft.setPower(y + r + x);
        frontRight.setPower(y + r - x);
        backLeft.setPower(y - r - x);
        backRight.setPower(y - r + x);

        if (gamepad1.right_trigger > 0.2) {
            x = gamepad1.left_stick_x * 0.1;
            y = -gamepad1.left_stick_y * 0.1;
            r = gamepad1.right_stick_x * 0.1;
            frontLeft.setPower(y + r + x);
            frontRight.setPower(y - r - x);
            backLeft.setPower(y + r - x);
            backRight.setPower(y - r + x);

        }
    }
}
