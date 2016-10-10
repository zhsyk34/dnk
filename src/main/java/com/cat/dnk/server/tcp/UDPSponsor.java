package com.cat.dnk.server.tcp;

import com.cat.dnk.server.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class UDPSponsor {

	private static void send(String host, int port, String msg) {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();

		bootstrap.group(group).channel(NioDatagramChannel.class);
		bootstrap.option(ChannelOption.SO_BROADCAST, false);

		bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
			@Override
			protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
				//Do nothing.
			}
		});

		if (host == null || host.isEmpty()) {
			host = Config.BROADCAST_HOST;
			System.err.println("broadcast :" + host);
		}
		try {
			Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
			ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
			InetSocketAddress address = new InetSocketAddress(host, port);
			channel.writeAndFlush(new DatagramPacket(buf, address)).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	/**
	 * UDP唤醒指定网关
	 */
	public static void awake(String host, int port) {
		send(host, port, Config.LOGIN_COMMAND);
	}
}
