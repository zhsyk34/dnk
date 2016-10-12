package com.cat.dnk.test.server;

import com.cat.dnk.server.tcp.TCPServer;

public class TestTCPServer {

    public static void main(String[] args) {
//        new SessionMonitor().start();
        new TCPServer().start();
    }
}
