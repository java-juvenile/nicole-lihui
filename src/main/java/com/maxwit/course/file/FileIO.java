package com.maxwit.course.file;

import java.io.File;

public class FileIO {
    
    String fileCopy() {
         File f = new File("/Users/nicole/tmp/text.txt");
         if (!f.exists()) {
             System.out.println();
             return "File Not Existes";
         } else {
             
         }
        return "Copy Done";

    }
}
