package com.CDIO3;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;

import java.util.ArrayList;

public class QRDetector {
    private static final double CV_PI = 3.14159;
    private Mat orgImg, grayImg, binImg;
    private String name = "";

    public  QRDetector(String name, String filePath) {
        this.name = name;
        orgImg = Imgcodecs.imread(filePath);
    }

    public void processAll() {
        getGray();
        //edgeDetection();
        thresholding();
        findQR();
    }

    public void draw() {
        HighGui.namedWindow(name, HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(name, orgImg);
    }

    public void write(String path) {
        Imgcodecs.imwrite(path + ".jpg", orgImg);
        Imgcodecs.imwrite(path + "_gray.jpg", grayImg);
        Imgcodecs.imwrite(path + "_edge.jpg", binImg);
        System.out.println("Out: " + path);
    }

    public void drawEdge() {
        HighGui.namedWindow(name+"_edge", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(name+"_edge", binImg);
    }

    public void drawGray() {
        HighGui.namedWindow(name+"_gray", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(name+"_gray", grayImg);
    }
    public void findQR() {
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hir = new Mat();
        Imgproc.findContours(binImg, contours, hir, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE );
        if(contours.size() > 0) {
            ContourTree ct = new ContourTree(hir, contours, 0);
            //drawContourChildren(ct, new Scalar(255, 0, 0), 0, 0);

            //ct.drawContourChildren(orgImg, new Scalar(0, 250, 0), 0, 0);
            //ct.drawContourChildren(orgImg, new Scalar(0, 0, 250), 0, 1);
            //ct.drawContourChildren(orgImg, new Scalar(255, 0, 250), 0, 2);
            //ct.drawContourChildren(orgImg, new Scalar(255, 100, 100), 0, 3);
            ct.drawBoundingIfChildren(orgImg, 3);
        }
    }
    public void getGray() {
        // Convert to HSV
        Mat tmpImg = orgImg.clone();
        Imgproc.cvtColor(orgImg, tmpImg, Imgproc.COLOR_BGR2HSV);

        // Split HSV channels to seperat mats
        ArrayList<Mat> tmpMats = new ArrayList<>();
        Core.split(tmpImg, tmpMats);

        // Get only V channel
        grayImg = tmpMats.get(2).clone();

        //Imgproc.threshold(binImg, binImg, 180, 255,Imgproc.THRESH_BINARY);
    }

    public void edgeDetection() {
        int threshold = 100;
        int ratio = 3;
        int blurAmout = 2;
        binImg = grayImg.clone();
        Imgproc.blur(grayImg, binImg, new Size(blurAmout,blurAmout));
        Imgproc.Canny(binImg, binImg, threshold, threshold*ratio);
    }

    public void thresholding() {
        binImg = new Mat();
        int thres = 190;
        Imgproc.threshold(grayImg, binImg, thres, 255,Imgproc.THRESH_BINARY);
    }

    public void addLines(Mat dst, Mat scr) {
        LineSegmentDetector lsd = Imgproc.createLineSegmentDetector();
        Mat lines = new Mat();
        lsd.detect(scr, lines);
    }

}
