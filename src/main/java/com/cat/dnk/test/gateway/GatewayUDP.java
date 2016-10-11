package com.cat.dnk.test.gateway;

import com.cat.dnk.server.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class GatewayUDP {

    public void bind(int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group).channel(NioDatagramChannel.class);
            bootstrap.option(ChannelOption.SO_BROADCAST, false);

            bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                @Override
                protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                    String data = msg.content().toString(CharsetUtil.UTF_8);
                    data = data.replaceAll("\r|\n", "");
                    System.out.println("receive " + ctx.channel().remoteAddress() + " [" + data + "]");

                    //TODO:loginReady
                    if ("loginReady".equals(data)) {
                        System.out.println("ready to connect tcp server");
                        //TODO one bind twice or more
                        new GatewayTCP().start();
                    }
                }
            });

            bootstrap.localAddress(new InetSocketAddress(Config.LOCAL_HOST, port));
            //Channel channel = bootstrap.bind(port).syncUninterruptibly().channel();
            Channel channel = bootstrap.bind().syncUninterruptibly().channel();
            System.out.println(port + " bind.");
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
