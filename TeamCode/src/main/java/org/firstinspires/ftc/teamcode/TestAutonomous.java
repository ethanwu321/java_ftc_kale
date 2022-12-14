package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class TestAutonomous extends LinearOpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    CRServo Claw;
    DcMotor SlideL;
    DcMotor SlideR;
    double diameter = 3.5; // inches
    double fullRotationTicks = 537.7; // ticks
    // fullRotationTicks : circumference
    // 537.7 / (pi * diameter)
    // basically, for 1 ticks, the robot travels 537.7 ticks / (pi * diameter)
    int ticksPerInch = (int) (fullRotationTicks / Math.PI * diameter); // ticks / inch

    public void Motors_Forward(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }
    public void Motors_Backward(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(-power);
    }
    public void Motors_Right(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    public void Motors_Left(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_TurnRight(double power){
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_TurnLeft(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    public void Motors_ForwardLeft(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power*0);
        backLeft.setPower(power*0);
        backRight.setPower(power);
    }
    public void Motors_ForwardRight(double power){
        frontLeft.setPower(power*0);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(power*0);
    }
    public void Motors_BackwardLeft(double power){
        frontLeft.setPower(power*0);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power*0);
    }
    public void Motors_BackwardRight(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power*0);
        backLeft.setPower(power*0);
        backRight.setPower(-power);
    }



    @Override
    public void runOpMode() {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        SlideL = hardwareMap.get(DcMotor.class, "SlideL");
        // SlideR = hardwareMap.get(DcMotor.class, "SlideR");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

       Motors_Forward(0.4);
       sleep(400);
       Motors_Backward(0.4);
       sleep(400);
       Motors_Right(0.4);
       sleep(400);
       Motors_Left(0.4);
       sleep(400);
       Motors_TurnRight(0.4);
       sleep(400);
       Motors_TurnLeft(0.4);
       sleep(400);
       Motors_ForwardRight(0.4);
       sleep(400);
       Motors_BackwardLeft(0.4);
       sleep(400);
       Motors_ForwardLeft(0.4);
       sleep(400);
       Motors_BackwardRight(0.4);
       sleep(400);

    }

    public void test() {
        SlideL.setTargetPosition(10 * ticksPerInch); // set a target tick position
        SlideL.setPower(0.1);
        // SlideR.setPower(0.1);
        while (SlideL.isBusy()) {} // when its moving, loop
        // it "stalls" the code while the left motor is moving
        // SlideR.setPower(0);
        SlideL.setPower(0);

        // now computer can continue
        // here, the linear slide has reached the target position
        // add the claw
    }
}