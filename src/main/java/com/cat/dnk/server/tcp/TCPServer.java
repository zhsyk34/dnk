package com.cat.dnk.server.tcp;

import com.cat.dnk.server.config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import static com.cat.dnk.server.config.Config.TIME_OUT;

public class TCPServer {

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        bootstrap.group(mainGroup, workerGroup).channel(NioServerSocketChannel.class);

        //setting options
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, Config.SERVER_BACKLOG);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        //logging
        //bootstrap.childHandler(new LoggingHandler());

        //handlers
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new IdleStateHandler(TIME_OUT, TIME_OUT, TIME_OUT));
                pipeline.addLast(new TCPServerHandler());
            }
        });

        try {
            ChannelFuture future = bootstrap.bind(Config.TCP_SERVER_PORT).sync();
            System.out.println("TCPServer start at port : " + Config.TCP_SERVER_PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
