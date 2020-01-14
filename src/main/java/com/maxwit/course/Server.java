package com.maxwit.course;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        int i = 0;
        while (i < 9) {
            client = ss.accept();

            dos = new DataOutputStream(client.getOutputStream());

            InputStreamReader is = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(is);
            String str = br.readLine();

            String[] conditions = str.split(" ");
            String fpath = conditions[1].replaceFirst("/", "");
            File f = new File(fpath);
            String response = null;

            if (f.exists()) {
               response = "HTTP/1.1 200 OK";
            } else {
                response = "HTTP/1.1 404 Not Found";
                f = new File("aa/error.html");
            }

            System.out.println(f.getAbsolutePath());
            InputStream fis = new FileInputStream(f);
            byte[] bt = new byte[1024];
            int len = fis.read(bt);
            String body = new String(bt, 0, len);

            String rsp = response + "\n"
                + "Server: Apache/2.2.14 (Win32)\n" 
                + "Content-Length: " + len + "\n" 
                + "Content-Type: text/html; charset=UTF-8\n" 
                + "\n" + body;
            System.out.println(rsp);
            dos.writeUTF(rsp);
            dos.flush();

            fis.close();
            is.close();
            dos.close();
        }
    }

    // void filedUtil(String command) throws IOException {
    //     String[] conditions = command.split(" ");
    //     switch (conditions[0]) {
    //         case "GET":
    //             String fpath = conditions[1].replaceFirst("/", "");
    //             sendFile(fpath);
    //             break;
    //         default:
    //             break;
    //     }
    // }

    // void sendFile(String fpath) throws IOException {
    //     File f = new File(fpath);

    //     if (f.exists()) {
    //         String response = "HTTP/1.1 200 OK";

    //         File errorf = new File("aa/in.html");
    //         System.out.println(errorf.getAbsolutePath());
    //         InputStream fis = new FileInputStream(errorf);
    //         byte[] bt = new byte[1024];
    //         int len = fis.read(bt);
    //         String body = new String(bt, 0, len);

    //         String rsp = response + "\n"
    //             + "Server: Apache/2.2.14 (Win32)\n" 
    //             // + "Content-Length: " + len + "\n" 
    //             // + "Content-Type: text/html; charset=UTF-8\n" 
    //             + "\n" + body;
    //         System.out.println(rsp);

    //         // InputStream fis = new FileInputStream(f);
    //         // byte[] bt = new byte[1024];
    //         // int len = fis.read(bt);
    //         // String body = new String(bt, 0, len);

    //         // String rsp = response + "\n"
    //         //     + "Server: Apache/2.2.14 (Win32)\n" 
    //         //     // + "Content-Length: " + len + "\n" 
    //         //     // + "Content-Type: text/html; charset=UTF-8\n" 
    //         //     + "\n" + body;
    //         // System.out.println(rsp);
    
    //         dos.writeUTF(rsp);
    //         dos.flush();
    //         fis.close();
    //     } else {
    //         String response = "HTTP/1.1 404 Not Found";

    //         File errorf = new File("aa/error.html");
    //         System.out.println(errorf.getAbsolutePath());
    //         InputStream fis = new FileInputStream(errorf);
    //         byte[] bt = new byte[1024];
    //         int len = fis.read(bt);
    //         String body = new String(bt, 0, len);

    //         String rsp = response + "\n"
    //             + "Server: Apache/2.2.14 (Win32)\n" 
    //             // + "Content-Length: " + len + "\n" 
    //             // + "Content-Type: text/html; charset=UTF-8\n" 
    //             + "\n" + body;
    //         System.out.println(rsp);

    //         dos.writeUTF(rsp);
    //         dos.flush();
    //         fis.close();

    //     }
    //     dos.close();
    // }

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
        int port = 7896;
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
