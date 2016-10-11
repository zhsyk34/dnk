package com.cat.dnk.other;

import com.cat.dnk.server.tcp.UDPSponsor;

public class TestUDPSponsor {

	public static void main(String[] args) throws Exception {
		UDPSponsor.awake(null, 6666);
//		new UDPSponsor().start();
	}
}
