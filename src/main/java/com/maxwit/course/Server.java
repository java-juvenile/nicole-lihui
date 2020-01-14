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
    ServerSocket ss = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    Socket client = null;

    void start(int port) throws IOException {
        ss = new ServerSocket(port);
    }

    void task() throws IOException {
        client = ss.accept();

        dis = new DataInputStream(client.getInputStream());
        dos = new DataOutputStream(client.getOutputStream());
        String str = dis.readUTF();
        filedUtil(str);
    }

    void filedUtil(String command) throws IOException {
        String[] conditions = command.split(" ");
        switch (conditions[0]) {
            case "GET":
                String fpath = conditions[1].replaceFirst("/", "");
                this.sendFile(fpath);
                break;
            default:
                break;
        }
    }

    void sendFile(String fpath) throws IOException {
        File f = new File(fpath);
        if (f.exists()) {
            int flen = (int) f.length();
            String response = "HTTP/1.1 200 OK";
            dos.writeUTF(response + "\n" + flen + "\n\n");
            dos.flush();

            DataInputStream fis = new DataInputStream(new FileInputStream(f));
            byte[] bt = new byte[flen];
            fis.read(bt);
            dos.write(bt, 0, flen);
            dos.flush();

            fis.close();
        } else {
            int flen = (int) f.length();
            String response = "HTTP/1.1 404 OK";
            dos.writeUTF(response + "\n" + flen);
            dos.flush();
        }
    }

    void close() {
        try {
            client.close();
            dos.close();
            ss.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        Server server = new Server();
        int port = 9003;
        try {
            server.start(port);
            server.task();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            server.close();
        }
    }
}
