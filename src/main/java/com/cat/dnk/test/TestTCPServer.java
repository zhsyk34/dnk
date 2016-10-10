package com.cat.dnk.test;

import com.cat.dnk.server.tcp.TCPServer;
import com.cat.dnk.session.SessionMonitor;

public class TestTCPServer {

	public static void main(String[] args) {
		new SessionMonitor().start();
		new TCPServer().start();
	}
}
