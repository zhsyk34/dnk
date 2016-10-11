package com.cat.dnk.test.gateway;

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

public class TestUDPClient {

	public void start() throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bootstrap.group(group).channel(NioDatagramChannel.class);

			bootstrap.option(ChannelOption.SO_BROADCAST, false);

			bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
				@Override
				protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
					System.out.println(msg.content().toString(CharsetUtil.UTF_8));
				}
			});

			Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
			ByteBuf buf = Unpooled.copiedBuffer("This is order.", CharsetUtil.UTF_8);
			channel.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress(Config.LOCAL_HOST, 50000))).sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new TestUDPClient().start();
	}
}
