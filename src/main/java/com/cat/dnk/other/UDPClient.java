package com.cat.dnk.other;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class UDPClient {

	private static final Bootstrap BOOTSTRAP = new Bootstrap();

	private final int PORT;//udp_port;

	public UDPClient(int port) {
		this.PORT = port;
	}

	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			BOOTSTRAP.group(group).channel(NioDatagramChannel.class);

			BOOTSTRAP.option(ChannelOption.SO_BROADCAST, true);

			BOOTSTRAP.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
				@Override
				protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
					System.out.println(msg.content().toString(CharsetUtil.UTF_8));
				}
			});

//			BOOTSTRAP.localAddress(new InetSocketAddress(Config.LOCAL_HOST, 6666));
//			Channel channel = BOOTSTRAP.bind(6666).syncUninterruptibly().channel();
			Channel channel = BOOTSTRAP.bind(PORT).sync().channel();
			System.out.println("client wait for udp...");
			channel.closeFuture().await();
		} finally {
			group.shutdownGracefully();
		}
	}
}
