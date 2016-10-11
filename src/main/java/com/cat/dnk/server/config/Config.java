package com.cat.dnk.server.config;

public class Config {

	public static final String LOCAL_HOST = "127.0.0.1";

	public static final int TCP_SERVER_PORT = 15999;

	public static final int UDP_SERVER_PORT = 15998;

	public static final int SERVER_BACKLOG = 1024;

	public static final String BROADCAST_HOST = "255.255.255.255";

	public static final int UDP_CLIENT_PORT = 3002;

	public static final int TIME_OUT = 10;//sec

	public static final String LOGIN_COMMAND = "{\"action\":\"loginReady\"}";

}
