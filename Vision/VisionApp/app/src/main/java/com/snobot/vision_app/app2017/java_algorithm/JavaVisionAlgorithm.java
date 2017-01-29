package com.snobot.vision_app.app2017.java_algorithm;

import android.graphics.Bitmap;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by preiniger on 1/24/2017.
 */

public class JavaVisionAlgorithm
{

    public enum DisplayType
    {
        OriginalImage,
        PostThreshold
    }

    private GripPegAlgorithm mPegGripAlgorithm;
    private GripPegAlgorithm mRopeGripAlgorithm;
    private DisplayType mDisplayType;
    private int cameraDirection;

    public JavaVisionAlgorithm()
    {
        mPegGripAlgorithm = new GripPegAlgorithm();
        mRopeGripAlgorithm = new GripPegAlgorithm();
        mDisplayType = DisplayType.OriginalImage;
        cameraDirection = CameraBridgeViewBase.CAMERA_ID_FRONT;
    }

    public void setDisplayType(DisplayType aDisplayType)
    {
        mDisplayType = aDisplayType;
    }

    public void iterateDisplayType()
    {
        int nextIndex = mDisplayType.ordinal() + 1;

        mDisplayType = DisplayType.values()[nextIndex % DisplayType.values().length];
    }

    public void setCameraDirection(int cameraDirection) {
        this.cameraDirection = cameraDirection;
    }



    public Mat processImage(Bitmap aBitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(aBitmap, mat);

        return processImage(mat);
    }

    public Mat processImage(Mat mat) {

        if(cameraDirection == CameraBridgeViewBase.CAMERA_ID_FRONT)
        {
            return processPegImage(mat);
        }
        else
        {
            return processRopeImage(mat);
        }
    }


    protected Mat processPegImage(Mat aOriginal)
    {
        mPegGripAlgorithm.process(aOriginal);


        Mat output;

        switch(mDisplayType) {
            case PostThreshold:
            {
                output = new Mat();
                Imgproc.cvtColor(mPegGripAlgorithm.hslThresholdOutput(), output, 9); //TODO magic number, should be CV_GRAY2RGBA but I can't find it
                break;
            }
            case OriginalImage:
            default: // Intentional fallthrough
            {
                output = aOriginal;
                break;
            }

        }

        return output;
    }

    protected Mat processRopeImage(Mat aOriginal)
    {
        mRopeGripAlgorithm.process(aOriginal);

        Mat output;

        switch(mDisplayType) {
            case PostThreshold:
            {
                output = new Mat();
                Imgproc.cvtColor(mRopeGripAlgorithm.hslThresholdOutput(), output, 9); //TODO magic number, should be CV_GRAY2RGBA but I can't find it
                break;
            }
            case OriginalImage:
            default: // Intentional fallthrough
            {
                output = aOriginal;
                break;
            }

        }

        return output;
    }


}
