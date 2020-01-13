package com.maxwit.course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 */
public class Server {
    void task(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        Socket client = ss.accept();

        DataInputStream dis = new DataInputStream(client.getInputStream());
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        String str = dis.readUTF();
        String fpath = str.replaceAll("mycurl /", "");

        File f = new File(fpath);
        int flen = (int) f.length();
        dos.writeLong(flen);
        dos.writeUTF(f.getName());
        dos.flush();

        DataInputStream fis = new DataInputStream(new FileInputStream(f));
        byte[] bt = new byte[flen];
        int len;
        while ((len = fis.read(bt)) > 0) {
            dos.write(bt, 0, len);
        }
        dos.flush();

        fis.close();
        dos.close();
        client.close();
        ss.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        int port = 9001;
        try {
            server.task(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
