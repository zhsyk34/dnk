package com.cat.dnk.session;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionInfo {
	private String key;//
	private Channel channel;
	private long login;//登录时间
	private long update;//更新时间

	private SessionInfo() {
	}

	public static SessionInfo build(String key, Channel channel, long login, long update) {
		SessionInfo sessionInfo = new SessionInfo();

		sessionInfo.setKey(key);
		sessionInfo.setChannel(channel);
		sessionInfo.setLogin(login);
		sessionInfo.setUpdate(update);

		return sessionInfo;
	}
}
