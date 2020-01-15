package com.maxwit.course;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class MyCurlTest {
    
    @Test
    public void curl() {
        MyCurl mCurl = new MyCurl();
        String host = "localhost";
        int port = 8080;

        String method = "GET";
        String uri = "/aa/bb.txt";
        String httpv = "HTTP/1.1";

        String[] paths = uri.split("/");
        String fname = paths[paths.length - 1];
        String dpath = "Client";

        String requestStm = method + " " + uri + " " + httpv;
        try {
            String sCode = mCurl.curl(host, port, requestStm, fname, dpath);
            assertTrue(sCode.equals("200"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
