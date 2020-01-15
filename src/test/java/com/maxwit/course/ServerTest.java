package com.maxwit.course;

import org.junit.Test;

import java.io.IOException;

public class ServerTest {

    @Test
    public void  server() {
        Server server = new Server();
        int port = 8080;
        try {
            server.start(port);
            server.task();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
