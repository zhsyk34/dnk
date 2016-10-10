package com.cat.dnk.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionMonitor {

	private static final Map<String, SessionInfo> APP_SESSION_MAP = new ConcurrentHashMap<>();
	//private static final Map<String, SessionInfo> GATEWAY_SESSION_MAP = new ConcurrentHashMap<>();

	public static class AppSessionManager {

		public static void add(Channel channel) {
			String key = UUID.randomUUID().toString();
			SessionInfo sessionInfo = SessionInfo.build(key, channel, System.currentTimeMillis(), System.currentTimeMillis());
			APP_SESSION_MAP.put(key, sessionInfo);
		}

		public static void update(Channel channel) {
			for (Map.Entry<String, SessionInfo> entry : APP_SESSION_MAP.entrySet()) {
				SessionInfo sessionInfo = entry.getValue();
				Channel client = sessionInfo.getChannel();
				if (channel.equals(client)) {
					sessionInfo.setUpdate(System.currentTimeMillis());
					break;
				}
			}
		}

		public static void remove(String key) {
			APP_SESSION_MAP.remove(key);
		}

		public static void remove(Channel channel) {
			for (Map.Entry<String, SessionInfo> entry : APP_SESSION_MAP.entrySet()) {
				if (channel.equals(entry.getValue().getChannel())) {
					remove(entry.getKey());
					break;
				}
			}
		}
	}

	/*public static class GatewaySessionManager {

		*/

	/**
	 * @param key     gateway uuid
	 * @param channel gateway connect tcp channel
	 *//*
		public static void update(String key, Channel channel) {
			SessionInfo sessionInfo = SessionInfo.build(key, channel, System.currentTimeMillis(), System.currentTimeMillis());
			GATEWAY_SESSION_MAP.put(key, sessionInfo);
		}

		public static void remove(String key) {
			GATEWAY_SESSION_MAP.remove(key);
		}

		public static void remove(Channel channel) {
			for (Map.Entry<String, SessionInfo> entry : GATEWAY_SESSION_MAP.entrySet()) {
				if (channel.equals(entry.getValue())) {
					remove(entry.getKey());
					break;
				}
			}
		}
	}*/

	//TODO
	public void start() {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		//ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(1);

		Runnable task = () -> {
			if (APP_SESSION_MAP.size() > 0) {
				System.out.println("active app client count: " + APP_SESSION_MAP.size());
			}
		};
		service.scheduleWithFixedDelay(task, 1000, 2000, TimeUnit.MILLISECONDS);
	}
}
