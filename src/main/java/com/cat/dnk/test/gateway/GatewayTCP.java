package com.cat.dnk.test.gateway;

import com.cat.dnk.server.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GatewayTCP {

    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            bootstrap.handler(new GatewayTCPHandler());

            bootstrap.connect(Config.LOCAL_HOST, Config.TCP_SERVER_PORT).sync();

            //new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //group.shutdownGracefully();
        }
    }
}
