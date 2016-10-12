package com.cat.dnk.test.server;

import com.cat.dnk.server.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

import static com.cat.dnk.data.EncoderKit.encode;

public class TestTCPClient {

	private static ByteBuf base() {

		byte[] bs = encode("{a:1}");
		return Unpooled.copiedBuffer(bs);
	}

	private static ByteBuf msg() {
		List<Byte> list = new ArrayList<>();
		//header
		list.add((byte) 90);
		list.add((byte) -91);
		//length=data+4
		list.add((byte) 0);
		list.add((byte) 9);
		//data
		list.add((byte) 123);//{
		list.add((byte) 97);//a
		list.add((byte) 58);//:
		list.add((byte) 49);//1
		list.add((byte) 125);//}
		//verify
		list.add((byte) 0);
		list.add((byte) 102);
		//footer
		list.add((byte) -91);
		list.add((byte) 90);

		//TODO add-data
		list.add((byte) 33);//wrong
		list.add((byte) 90);
		list.add((byte) -91);
		list.add((byte) 0);
		list.add((byte) 3);
		list.add((byte) 123);//{
		list.add((byte) 98);//a
		list.add((byte) 58);//:
		list.add((byte) 50);//1
		list.add((byte) 98);//a
		list.add((byte) 58);//:
		list.add((byte) 50);//1
		list.add((byte) 125);//}
		list.add((byte) 0);
		list.add((byte) 102);
		list.add((byte) -95);
		list.add((byte) 99);//

		byte[] bs = new byte[list.size()];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = list.get(i);
		}
		return Unpooled.copiedBuffer(bs);
	}

	public static void main(String[] args) {

		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bootstrap.group(group).channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);

			bootstrap.handler(new ChannelHandlerAdapter() {
				@Override
				public void channelActive(ChannelHandlerContext ctx) throws Exception {
					ctx.writeAndFlush(msg());
//					System.out.println("connect");
				}
			});

			ChannelFuture future = bootstrap.connect(Config.LOCAL_HOST, Config.TCP_SERVER_PORT).sync();
			Channel channel = future.channel();

		/*	BufferedInputStream in = new BufferedInputStream(System.in);
			int read = 0;
			while (read != -1) {
				try {
					read = in.read();
					channel.writeAndFlush((byte) read);
					System.out.println(read);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/

			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
