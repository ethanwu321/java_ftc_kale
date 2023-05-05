package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

@TeleOp
public class TestTeleOp extends OpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    Servo claw;
    Servo rotator;
    //Servo clawextension;
    DcMotorEx SlideL; // encoder here?
    DcMotorEx SlideR;

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
        rotator = hardwareMap.get(Servo.class, "ClawRotator");
        rotator.scaleRange(0, 1);

        //SlideL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //clawextension = hardwareMap.get(Servo.class, "ClawExtension");
        //claw.scaleRange(0.1, 1);
        SlideL = hardwareMap.get(DcMotorEx.class, "SlideL");
        SlideR = hardwareMap.get(DcMotorEx.class, "SlideR");

        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double Lx = gamepad1.left_stick_x * (gamepad1.right_trigger > 0.2 ? 0.2 : 1.0);
        double Ly = gamepad1.left_stick_y * (gamepad1.right_trigger > 0.2 ? 0.2 : 1.0);
        double Rx = -gamepad1.right_stick_x * (gamepad1.right_trigger > 0.2 ? 0.2 : 0.7);
        double LSLy = gamepad2.left_stick_y;

        double slideScaleValue = 1;

        frontLeft.setPower(-Ly +Rx -Lx);
        frontRight.setPower(-Ly -Rx +Lx);
        backLeft.setPower(Ly +Rx +Lx);
        backRight.setPower(Ly -Rx -Lx);
        SlideL.setPower(-LSLy * slideScaleValue);
        SlideR.setPower(LSLy * slideScaleValue);

        telemetry.addLine("left stick y: " + LSLy);

        if (SlideL.getCurrentPosition() >= 1535) {
            SlideL.setPower(0);
            SlideR.setPower(0);
        }
        if (SlideL.getCurrentPosition() <= 65) {
            SlideL.setPower(0);
            SlideR.setPower(0);
        }


        //if (gamepad2.y) {
            //SlideL.setTargetPosition(1500);
            //SlideR.setTargetPosition(1500);
        //}
        //if (gamepad2.dpad_up) {
            //SlideL.setPower(0.5);
            //SlideR.setPower(-0.5);
        //} //else if (gamepad2.dpad_down) {
            //SlideL.setPower(-0.5);
            //SlideR.setPower(0.5);}
            //else if (SlideL.getCurrentPosition() >= 1535) {
                //SlideL.setPower(0.0);
                //SlideR.setPower(0.0);}
            //else if (SlideR.getCurrentPosition() >= 1535) {
                //SlideL.setPower(0.0);
                //SlideR.setPower(0.0);}
            //else if (SlideL.getCurrentPosition() <= 65 || SlideR.getCurrentPosition() <= 65) {
                //SlideL.setPower(0.0);
                //SlideR.setPower(0.0);}
         //else {
            //SlideL.setPower(0.0);
            //SlideR.setPower(0.0);
        //}


        if(gamepad2.right_bumper)
            claw.setPosition(0.5);
        if(gamepad2.left_bumper)
            claw.setPosition(0.8);
        if(gamepad2.x)
            rotator.setPosition(0.5);
        if(gamepad2.y)
            rotator.setPosition(0.25);
        if(gamepad2.b)
            rotator.setPosition(0.0);
        //if(gamepad2.dpad_up)
          //  clawextension.setPosition(clawextension.getPosition() + 0.1);
        //if(gamepad2.dpad_down)
          //  clawextension.setPosition(clawextension.getPosition() - 0.1);

            // bind linear slide to dpad_up

        // SlideR.setPower(0.1)

        telemetry.addData("front left power",frontLeft.getPower());
        telemetry.addData("front right power",frontRight.getPower());
        telemetry.addData("back left power",backLeft.getPower());
        telemetry.addData("back right power",backRight.getPower());
        telemetry.addData("claw position",claw.getPosition());
        telemetry.addData("linear slide position",SlideL.getCurrentPosition());
        telemetry.addData("SlideL power", SlideL.getPower());
        telemetry.addData("SlideR power", SlideR.getPower());
        telemetry.update();

    }
}

