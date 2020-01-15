package com.maxwit.course;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MyCurlTest {
    
    @Test
    public void curl() {
        MyCurl mCurl = new MyCurl();

        String host = "localhost";
        int port = 8888;
        String method = "GET";
        String uri = "/aa/index.html";
        String httpv = "HTTP/1.1";
        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("Host", "127.0.0.1:8888");
        String body = "";

        String[] paths = uri.split("/");
        String fname = paths[paths.length - 1];

        mCurl.httpRequest(method, uri, httpv, reqMap, body);
        try {
            String sCode = mCurl.curl(host, port, fname);
            assertTrue(sCode.equals("200"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
