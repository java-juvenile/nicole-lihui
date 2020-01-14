package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * MyCurl
 */
public class MyCurl {
    Socket socket = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    String dpath = "Client";

    void curl(String host, int port, String command, String fname) throws IOException {
        socket = new Socket(host, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(command);
        dos.flush();

        String rsp = dis.readUTF();
        
        String[] rspParas = rsp.split("\n");
        String[] rspH = rspParas[0].split(" ");
        Integer flen = Integer.valueOf(rspParas[1]);

        if (rspH[1].equals("200")) {
            saveFile((int) flen, dpath, fname);
        } else {
            System.out.println("404");
        }

        dos.close();
        socket.close();
    }

    void saveFile(int flen, String dpath, String fname) throws IOException {
        File directory = new File(dpath);
        if(!directory.exists()) {
            directory.mkdir();
        }
        String fpath = directory.getAbsolutePath() + File.separatorChar + fname;
        File file = new File(fpath);
        DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
        byte[] bt = new byte[flen];
        dis.read(bt);
        fos.write(bt, 0, flen);
        fos.flush();

        dis.close();
        fos.close();
    }

    public static void main(String[] args) {
        MyCurl mCurl = new MyCurl();
        String host = "localhost";
        int port = 9003;

        String method = "GET";
        String uri = "/aa/bb.txt";
        String httpv = "HTTP/1.1";

        String[] paths = uri.split("/");
        String fname = paths[paths.length - 1];

        String requestStm = method + " " + uri + " " + httpv;
        try {
            mCurl.curl(host, port, requestStm, fname);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
