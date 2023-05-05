package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class OldAutonomous extends LinearOpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    Servo claw;
    DcMotor SlideL;
    DcMotor SlideR;
    double diameter = 3.5; // inches
    double fullRotationTicks = 537.7; // ticks
    // fullRotationTicks : circumference
    // 537.7 / (pi * diameter)
    // basically, for 1 ticks, the robot travels 537.7 ticks / (pi * diameter)
    int ticksPerInch = (int) (fullRotationTicks / Math.PI * diameter); // ticks / inch
//finish everything here non-intake related

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
        frontLeft.setPower(power*0);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(power*0);
    }
    public void Motors_ForwardRight(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power*0);
        backLeft.setPower(power*0);
        backRight.setPower(power);
    }
    public void Motors_BackwardLeft(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power*0);
        backLeft.setPower(power*0);
        backRight.setPower(-power);
    }
    public void Motors_BackwardRight(double power){
        frontLeft.setPower(power*0);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power*0);
    }
    // public void Move_Right(int i, int ticksPerInch) {
    // frontLeft.setTargetPosition(i*ticksPerInch);
    // frontRight.setTargetPosition(-i*ticksPerInch);
    // backLeft.setTargetPosition(i*ticksPerInch);
    // backRight.setTargetPosition(-i*ticksPerInch);}

    // start by closing claw, lift linear slide one stage,
    // can try to read sleeve or just move the bot to a junction



    @Override
    public void runOpMode() {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        SlideL = hardwareMap.get(DcMotor.class, "SlideL");
        SlideR = hardwareMap.get(DcMotor.class, "SlideR");
        claw = hardwareMap.get(Servo.class, "RightClaw");
        claw.scaleRange(0.5, 1);

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();


        //Motors_Forward(0.4);
        //sleep(825);
        //Motors_Right(0.4);
        //sleep(1200);
        //Motors_Forward(0.4);
        //sleep(1650);
        //Motors_Backward(0.4);
        //sleep(1650);

        //Motors_Right(0.4);
        //sleep(1200);
        //Motors_Forward(0.4);
        //sleep(1650);
        //Motors_TurnLeft(0.4);
        //sleep(500);
        SlideUp(3);
        claw.setPosition(0.5);
        sleep(1000);
        claw.setPosition(1);
        sleep(1000);
        claw.setPosition(0.5);
        sleep(1000);

    }


    public void SlideUp(int inches) {
        SlideL.setTargetPosition(inches * ticksPerInch); // set a target tick position
        SlideL.setPower(0.3);
        SlideR.setPower(-0.3);
        while (SlideR.isBusy()) {}
        while (SlideL.isBusy()) {} // when its moving, loop
        //it "stalls" the code while the left motor is moving
        SlideL.setPower(0);
        SlideR.setPower(0);
        // now computer can continue
        // here, the linear slide has reached the target position
        // add the claw
    }

}
