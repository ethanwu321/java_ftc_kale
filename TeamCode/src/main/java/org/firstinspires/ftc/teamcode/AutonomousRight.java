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
    CRServo Claw;/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class AutonomousRight extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

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
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_Left(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    public void Motors_TurnRight(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_TurnLeft(double power){
        frontLeft.setPower(power);
        frontRight.setPower(-power);
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

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.038;

    //int ID_TAG_OF_INTEREST = 277; // Tag ID 18 from the 36h11 family
    // AprilTags:
    // Parking 1 - ID 277
    // Parking 2 - ID 283
    // Parking 3 - ID 326

    int left = 277;
    int middle = 283;
    int right = 326;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                System.out.println(errorCode);
            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == left || tag.id == middle || tag.id ==right)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        claw = hardwareMap.get(Servo.class, "RightClaw");
        claw.scaleRange(0.5, 1);
        //clawextension = hardwareMap.get(Servo.class, "ClawExtension");
        //claw.scaleRange(0.1, 1);
        SlideL = hardwareMap.get(DcMotorEx.class, "SlideL");
        SlideR = hardwareMap.get(DcMotorEx.class, "SlideR");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        /* Actually do something useful */
        sleep(1000);
        if(tagOfInterest == null || tagOfInterest.id == middle) {
            Motors_Forward(0.4);
            sleep(1000);
        } else if(tagOfInterest.id == left) {
            Motors_Forward(0.4);
            sleep(1050);
            Motors_Left(0.4);
            sleep(1300);
        } else if (tagOfInterest.id == right) {
            Motors_Forward(0.4);
            sleep(1050);
            Motors_Right(0.4);
            sleep(1300);
        }


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        // while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}

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
    }/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous
public class AutonomousLeft extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

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
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_Left(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    public void Motors_TurnRight(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void Motors_TurnLeft(double power){
        frontLeft.setPower(power);
        frontRight.setPower(-power);
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

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.038;

    //int ID_TAG_OF_INTEREST = 277; // Tag ID 18 from the 36h11 family
    // AprilTags:
    // Parking 1 - ID 277
    // Parking 2 - ID 283
    // Parking 3 - ID 326

    int left = 277;
    int middle = 283;
    int right = 326;

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                System.out.println(errorCode);
            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == left || tag.id == middle || tag.id ==right)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        claw = hardwareMap.get(Servo.class, "RightClaw");
        claw.scaleRange(0.5, 1);
        //clawextension = hardwareMap.get(Servo.class, "ClawExtension");
        //claw.scaleRange(0.1, 1);
        SlideL = hardwareMap.get(DcMotorEx.class, "SlideL");
        SlideR = hardwareMap.get(DcMotorEx.class, "SlideR");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        /* Actually do something useful */
        sleep(1000);
        if(tagOfInterest == null || tagOfInterest.id == middle) {
            Motors_Forward(0.4);
            sleep(1000);
        } else if(tagOfInterest.id == left) {
            Motors_Forward(0.4);
            sleep(1050);
            Motors_Left(0.4);
            sleep(1300);
        } else if (tagOfInterest.id == right) {
            Motors_Forward(0.4);
            sleep(1050);
            Motors_Right(0.4);
            sleep(1300);
        }

        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        // while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
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

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

        Motors_Right(0.4);
        sleep(1100);
        Motors_Forward(0.4);
        sleep(2200);

    }

    public void SlideUp(int inches) {
        SlideL.setTargetPosition(inches * ticksPerInch); // set a target tick position
        SlideL.setPower(0.4);
        SlideR.setPower(0.4);
        while (SlideR.isBusy()) {}
        while (SlideL.isBusy()) {} // when its moving, loop
        // it "stalls" the code while the left motor is moving
        SlideL.setPower(0);
        SlideR.setPower(0);
        // now computer can continue
        // here, the linear slide has reached the target position
        // add the claw
    }
}
