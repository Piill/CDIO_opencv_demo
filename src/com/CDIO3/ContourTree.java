package com.CDIO3;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

public class ContourTree {
    public ContourTree Sibling, Child;
    int Number;
    MatOfPoint contour;

    public ContourTree(Mat hir, ArrayList<MatOfPoint> contours, int Num) {
        Number = Num;
        contour = contours.get(Number);
        int sibNum = (int)hir.get(0,Number)[0];
        int childNum = (int)hir.get(0,Number)[2];
        if(sibNum != -1) {
            Sibling = new ContourTree(hir, contours, sibNum);
        } else {
            Sibling = null;
        }

        if(childNum != -1) {
            Child = new ContourTree(hir, contours, childNum);
        } else {
            Child = null;
        }
    }

    public void drawContourChildren(Mat img, Scalar color, int level, int desiredLevel) {
        ArrayList<Integer> alreadyDrawn = new ArrayList<>();
        for(ContourTree current = this; current != null; current = current.Sibling) {
           if(level == desiredLevel) {
               Imgproc.drawContours(img, Arrays.asList(current.contour), -1, color, 1);
           } else if(level < desiredLevel) {
               if(current.Child != null) {
                   current.Child.drawContourChildren(img, color, level + 1, desiredLevel);
               }
           }
        }
    }

    public void drawBoundingIfChildren(Mat img, int reqDepth) {
        for(ContourTree current = this; current != null; current = current.Sibling) {
            if(current.getDepth() >= reqDepth ) {
                MatOfPoint2f newContour = new MatOfPoint2f(current.contour.toArray());

                RotatedRect rotatedRect = Imgproc.minAreaRect(newContour);
                Point[] points = new Point[4];
                rotatedRect.points(points);
                MatOfPoint a = new MatOfPoint(points);
                Imgproc.drawContours(img, Arrays.asList(a), -1, new Scalar(100,255, 100), 2);

                current.Child.drawBoundingIfChildren(img, reqDepth);
            }
        }
    }

    public int getDepth() {
        int depth = 0;
        if(Child != null) {
            for(ContourTree ct = Child; ct != null; ct = ct.Sibling) {
                if(ct.getDepth() > depth) {
                    depth = ct.getDepth();
                }
            }
            depth++;
        }
        return depth;
    }

}
