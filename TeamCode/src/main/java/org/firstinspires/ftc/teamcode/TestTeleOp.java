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
    Servo claw;
    DcMotor SlideL; // encoder here?
    DcMotor SlideR;

    double diameter = 3.5; // inches
    double fullRotationTicks = 537.7; // ticks
    // fullRotationTicks : circumference
    // 537.7 / (pi * diameter)
    // basically, for 1 ticks, the robot travels 537.7 ticks / (pi * diameter)
    int ticksPerInch = (int) (fullRotationTicks / Math.PI * diameter); // ticks / inch

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        claw = hardwareMap.get(Servo.class, "RightClaw");
        claw.scaleRange(0.5, 1);
        SlideL = hardwareMap.get(DcMotor.class, "SlideL");
        SlideR = hardwareMap.get(DcMotor.class, "SlideR");

        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double Lx = gamepad1.left_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double Ly = gamepad1.left_stick_y * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        double Rx = -gamepad1.right_stick_x * (gamepad1.right_trigger > 0.2 ? 0.1 : 0.7);
        if (gamepad2.dpad_up) {
            SlideL.setPower(0.5);
            SlideR.setPower(-0.5);
        } else if (gamepad2.dpad_down) {
            SlideL.setPower(-0.5);
            SlideR.setPower(0.5);}
            else if (SlideL.getCurrentPosition() >= 1535) {
                SlideL.setPower(0.0);
                SlideR.setPower(0.0);}
            else if (SlideR.getCurrentPosition() >= 1535) {
                SlideL.setPower(0.0);
                SlideR.setPower(0.0);}
            else if (SlideL.getCurrentPosition() <= 65) {
                SlideL.setPower(0.0);
                SlideR.setPower(0.0);}
            else if (SlideR.getCurrentPosition() <= 65) {
                SlideL.setPower(0.0);
                SlideR.setPower(0.0);}
         else {
            SlideL.setPower(0.0);
            SlideR.setPower(0.0);
        }


        // SlideR.setPower(0.1)

        frontLeft.setPower(-Ly +Rx -Lx);
        frontRight.setPower(-Ly -Rx +Lx);
        backLeft.setPower(Ly -Rx -Lx);
        backRight.setPower(Ly +Rx +Lx);



        telemetry.addData("front left power",frontLeft.getPower());
        telemetry.addData("front right power",frontRight.getPower());
        telemetry.addData("back left power",backLeft.getPower());
        telemetry.addData("back right power",backRight.getPower());
        telemetry.addData("claw position",claw.getPosition());
        telemetry.addData("linear slide position",SlideL.getCurrentPosition());
        telemetry.addData("SlideL power", SlideL.getPower());
        telemetry.addData("SlideR power", SlideR.getPower());
        telemetry.update();


        if(gamepad2.right_bumper)
            claw.setPosition(0.5);
        if(gamepad2.left_bumper)
            claw.setPosition(1);

        // bind linear slide to dpad_up
    }
}
