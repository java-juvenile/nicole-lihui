package com.maxwit.course;

import java.io.*;
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

    void httpResponse(String proV, String status, String desc, Map rspMap, String body) {
        rsp = proV + " " + status + " " + desc + "\n";
        rspMap.forEach((k, v) -> {rsp += k + ": " + v + "\n";});
        rsp += "\n" + body;
    }

    void start(int port) throws IOException {
        ss = new ServerSocket(port);
    }

    void task() throws IOException {
        int i = 0;
        while (i < 9) {
            client = ss.accept();

            OutputStream os = client.getOutputStream();
            InputStreamReader is = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(is);
            String str = br.readLine();

            String hv = "HTTP/1.1";
            String sCode = null;
            String desc = null;
            Map<String, String> rspMap = new HashMap<String, String>();

            String[] conditions = str.split(" ");
            String fpath = conditions[1].replaceFirst("/", "");
            File f = new File(fpath);
            if (f.exists()) {
                sCode = "200";
                desc = "OK";
            } else {
                sCode = "404";
                desc = "Not Found";
                f = new File("aa/error.html");
            }
            InputStream fis = new FileInputStream(f);
            byte[] bt = new byte[1024];
            int len = fis.read(bt);
            String body = new String(bt, 0, len);

            rspMap.put("Content-Length", Integer.toString(len));
            rspMap.put("Content-Type", "text/html; charset=UTF-8");
            httpResponse(hv, sCode, desc, rspMap, body);
            os.write(rsp.getBytes("UTF-8"));

            fis.close();
            is.close();
            os.close();
            rsp = null;
        }
        ss.close();
        client.close();
    }

}
