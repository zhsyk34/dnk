package com.cat.dnk.server.tcp;

import com.cat.dnk.kit.Util;
import com.cat.dnk.session.SessionMonitor;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 在 Channel 或者 ChannelPipeline 上调用write() 都会把事件在整个管道传播,但是在 ChannelHandler 级别上，
 * 从一个处理程序转到下一个却要通过在 ChannelHandlerContext 调用方法实现。
 */
//TODO:
public class TCPServerHandler extends ChannelHandlerAdapter {

    //private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(">>>>>>>>>>>>>>>client " + ctx.channel().remoteAddress() + " closed.");
        SessionMonitor.AppSessionManager.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(">>>>>>>>>>>>>>>>>>>>client " + ctx.channel().remoteAddress() + " connected.");
        SessionMonitor.AppSessionManager.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(">>>>>>>>>>>>>>>>>>>>client " + ctx.channel().remoteAddress() + " error");
        ctx.channel().close();
        SessionMonitor.AppSessionManager.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.print(">>>>>>>>>>>>>>>>>>>>>receive " + ctx.channel().remoteAddress() + " data:" + Util.convert(msg));
        //Util.read(msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            System.err.println(">>>>>>>>>>>>>>>>find client " + ctx.channel().remoteAddress() + " idle too long, and remove it");
            ctx.channel().close();
        }
    }
}
