package com.maxwit.course;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyCurl
 */
public class MyCurl {
    Socket socket = null;

    String curl(String host, int port, String command, String fname, String dpath) throws IOException {
        socket = new Socket(host, port);
        InputStream dis = new DataInputStream(socket.getInputStream());

        OutputStream os = socket.getOutputStream();
        os.write(command.getBytes("UTF-8"));
        socket.shutdownOutput();

        byte[] b = new byte[1204];
        int len = dis.read(b);
        StringBuilder rspB = new StringBuilder();
        rspB.append(new String(b, 0, len, "UTF-8"));
        String rsp = rspB.toString();

        String rspH = null;
        String body = null;
        Pattern p = Pattern.compile("([\\s\\S]*?)\n\n([\\s\\S]*)");
        Matcher m = p.matcher(rsp);
        while (m.find()) {
            rspH = m.group(1);
            body = m.group(2);
        }

        p = Pattern.compile("(.*?) (\\d+) (.*)\n");
        m = p.matcher(rspH);
        String httpv = null;
        String sCode = null;
        String des = null;
        while (m.find()) {
            httpv = m.group(1);
            sCode = m.group(2);
            des = m.group(3);
        }

        p = Pattern.compile("\n(.*): (.*)");
        m = p.matcher(rspH);
        Map<String, String> rspInfo = new HashMap<>();
        while (m.find()) {
            rspInfo.put(m.group(1), m.group(2));
        }


        if (sCode.equals("200")) {
            saveFile(dpath, fname, body);
        } else {
            //
        }
        dis.close();

        return sCode;
    }

    void saveFile(String dpath, String fname, String body) throws IOException {
        File directory = new File(dpath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String fpathStr = dpath + File.separatorChar + fname;

        try (PrintWriter out = new PrintWriter(fpathStr, "UTF-8")) {
            out.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
