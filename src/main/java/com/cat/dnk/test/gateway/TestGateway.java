package com.cat.dnk.test.gateway;

public class TestGateway {

    private static final int BASE_PORT = 50000;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            final int k = i;
            new Thread(() -> new GatewayUDP().bind(BASE_PORT + k)).start();
        }
    }
}
