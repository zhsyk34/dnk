package com.cat.dnk.test.gateway;

import com.cat.dnk.kit.Util;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class GatewayTCPHandler extends ChannelHandlerAdapter {

	@Override
	public boolean isSharable() {
		return true;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(">>>>>>>>> error.");
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String data = Util.convert(msg);
		System.out.println("receive " + ctx.channel().remoteAddress() + " data :" + data);

		System.out.println(data);
		//TODO CMD
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush("loginReq");
	}

}
