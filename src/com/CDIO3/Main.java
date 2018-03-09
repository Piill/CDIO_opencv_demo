package com.CDIO3;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }


    public static void main(String[] args) {
	// write your code here
       //init();
           String path = "/home/pil/Desktop/ringe/daniel";
           File folder = new File(path);

           for(File fileEntry : folder.listFiles()) {
               if(fileEntry.getName().toLowerCase().contains(".jpg")) {
                   QRDetector qr = new QRDetector(fileEntry.getName(), fileEntry.getAbsolutePath());
                   qr.processAll();
                   String fileName = fileEntry.getName().replace(".jpg", "");
                   qr.draw();
                   qr.drawEdge();

               }
           }
           HighGui.waitKey(0);
    }
}
