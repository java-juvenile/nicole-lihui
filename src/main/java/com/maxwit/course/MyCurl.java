package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * MyCurl
 */
public class MyCurl {

    void curl(String host, int port, String command) throws IOException {
        Socket socket = new Socket(host, port);
        DataOutputStream fos = null;
        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(command);
        dos.flush();

        int flen = (int) dis.readLong();
        String filename = dis.readUTF();

        File directory = new File("client");
        if(!directory.exists()) {
            directory.mkdir();
        }
        String fpath = directory.getAbsolutePath() + File.separatorChar + filename;
        File file = new File(fpath);
        fos = new DataOutputStream(new FileOutputStream(file));
        byte[] bt = new byte[flen];
        int len;
        while ((len = dis.read(bt)) > 0) {
            fos.write(bt, 0, len);
        }
        dos.flush();

        fos.close();
        dos.close();
        socket.close();
    }

    public static void main(String[] args) {
        MyCurl mCurl = new MyCurl();
        try {
            mCurl.curl("localhost", 9001, "mycurl /aa/bb.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
