package com.cat.dnk;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class UDPServer {

	private static final Bootstrap BOOTSTRAP = new Bootstrap();

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			BOOTSTRAP.group(group).channel(NioDatagramChannel.class);

			BOOTSTRAP.option(ChannelOption.SO_BROADCAST, false);//广播

			BOOTSTRAP.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
				@Override
				protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
					System.out.println(msg.content().toString(CharsetUtil.UTF_8));
				}
			});

			Channel channel = BOOTSTRAP.bind(0).syncUninterruptibly().channel();
			//Channel channel = BOOTSTRAP.bind(0).sync().channel();
			ByteBuf buf = Unpooled.copiedBuffer("This is order3.", CharsetUtil.UTF_8);
			//channel.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress(Config.SERVER_HOST, Config.UDP_PORT))).sync();
			channel.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress("255.255.255.255", 7777))).sync();
//			channel.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress("255.255.255.255", Config.UDP_PORT))).sync();
		} finally {
			group.shutdownGracefully();
		}
	}

}
