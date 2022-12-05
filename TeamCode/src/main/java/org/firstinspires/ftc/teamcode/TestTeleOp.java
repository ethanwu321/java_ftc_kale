package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestTeleOp extends OpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    // Servo rightclaw;
    // Servo leftclaw;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        // leftclaw = hardwareMap.get(Servo.class, "LeftClaw");
        // rightclaw = hardwareMap.get(Servo.class, "RightClaw");

        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double Lx = gamepad1.left_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double Ly = gamepad1.left_stick_y * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double Rx = -gamepad1.right_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);

        frontLeft.setPower(Ly + Rx - Lx);
        frontRight.setPower(Ly - Rx + Lx);
        backLeft.setPower(-Ly - Rx - Lx);
        backRight.setPower(-Ly + Rx + Lx);

        telemetry.addData("front left power", frontLeft.getPower());
        telemetry.addData("front right power", frontRight.getPower());
        telemetry.addData("back left power", backLeft.getPower());
        telemetry.addData("back right power", backRight.getPower());
        telemetry.update();


        // if(gamepad2.y)
            // leftclaw.setPosition(0);
            // rightclaw.setPosition(0);
         // else if (gamepad2.x)
            // leftclaw.setPosition(1);
            // rightclaw.setPosition(1);

    }
}
