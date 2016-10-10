package com.cat.dnk.test;

import com.cat.dnk.UDPClient;

public class TestUDPClient {
	public static void main(String[] args) throws InterruptedException {
		new UDPClient(6666).start();
	}
}
