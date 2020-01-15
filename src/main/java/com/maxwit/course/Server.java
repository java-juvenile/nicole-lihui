package com.maxwit.course;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Server
 */
public class Server {
    ServerSocket ss = null;
    Socket client = null;
    String rsp = null;

    void start(int port) throws IOException {
        ss = new ServerSocket(port);
    }

    void task() throws IOException {
        int i = 0;
        while (i < 9) {
            client = ss.accept();

            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            InputStreamReader is = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(is);
            String str = br.readLine();

            String[] conditions = str.split(" ");
            String fpath = conditions[1].replaceFirst("/", "");
            File f = new File(fpath);
            String hv = "HTTP/1.1";
            String status = "";
            Map<String, String> rspMap = new HashMap<String, String>();

            if (f.exists()) {
                status = "200 OK";
            } else {
                status = "404 Not Found";
                f = new File("aa/error.html");
            }

            InputStream fis = new FileInputStream(f);
            byte[] bt = new byte[1024];
            int len = fis.read(bt);
            String body = new String(bt, 0, len);

            String rl = hv + " " + status;
            rspMap.put("Content-Length", Integer.toString(len));
            rspMap.put("Content-Type", "text/html; charset=UTF-8");

            rsp = rl + "\n";
            rspMap.forEach((k, v) -> {rsp += k + ": " + v + "\n";});
            rsp += "\n" + body;
            dos.writeUTF(rsp);
            dos.flush();

            fis.close();
            is.close();
            dos.close();
        }
        ss.close();
        client.close();
    }

}
