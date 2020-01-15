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
    String reqStr = null;
    String dpath = "Client";

    void httpRequest(String method, String uri, String proV, Map resMap, String body) {
        reqStr = method + " " + uri + " " + proV + "\n";
        resMap.forEach((k, v) -> {reqStr += k + ": " + v + "\n";});
        reqStr += "\n" + body;
    }

    String curl(String host, int port, String fname) throws IOException {
        socket = new Socket(host, port);

        OutputStream os = socket.getOutputStream();
        os.write(reqStr.getBytes("UTF-8"));
        socket.shutdownOutput();

        InputStream is = socket.getInputStream();
        byte[] b = new byte[1204];
        int len;
        StringBuilder rspB = new StringBuilder();
        while ((len = is.read(b)) > -1) {
            rspB.append(new String(b, 0, len, "UTF-8"));
        }
        String rsp = rspB.toString();

        String rspH = null;
        String body = null;
        Pattern p = Pattern.compile("([\\s\\S]*?)(\n|\r\n){2}([\\s\\S]*)");
        Matcher m = p.matcher(rsp);
        while (m.find()) {
            rspH = m.group(1);
            body = m.group(3);
        }

        p = Pattern.compile("(HTTP/.*?) (\\d+) (.*)[\n|\r\n]{1}");
        m = p.matcher(rspH);
        String httpv = null;
        String sCode = null;
        String des = null;
        while (m.find()) {
            httpv = m.group(1);
            sCode = m.group(2);
            des = m.group(3);
        }

        p = Pattern.compile("[\r\n|\n](.*): (.*)");
        m = p.matcher(rspH);
        Map<String, String> rspInfo = new HashMap<>();
        while (m.find()) {
            rspInfo.put(m.group(1), m.group(2));
        }

        if (sCode.equals("200")) {
            saveFile(fname, body);
        } else {
            //
        }
        is.close();

        return sCode;
    }

    void saveFile(String fname, String body) throws IOException {
        File directory = new File(this.dpath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String fpathStr = this.dpath + File.separatorChar + fname;

        try (PrintWriter out = new PrintWriter(fpathStr, "UTF-8")) {
            out.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
