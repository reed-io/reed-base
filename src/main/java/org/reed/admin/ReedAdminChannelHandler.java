/**
 * springbootup @ org.reed.admin.ReedAdminChannelHandler.java
 */
package org.reed.admin;

import org.reed.log.ReedLogger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 15:52
 */
public class ReedAdminChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        SocketChannel channel = (SocketChannel) ctx.channel();
        ReedLogger.debug("ReedAdmin channel active: "+channel.id()+" @ "+channel.localAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SocketChannel channel = (SocketChannel) ctx.channel();
        ReedLogger.debug("ReedAdmin channel inactive: "+channel.id()+" @ "+channel.localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        SocketChannel channel = (SocketChannel) ctx.channel();
        ReedLogger.debug("ReedAdmin channel channel read: "+channel.id()+" @ "+channel.localAddress().toString()+" - "+msg.toString());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        SocketChannel channel = (SocketChannel) ctx.channel();
        ReedLogger.debug("ReedAdmin channel read completed: "+channel.id()+" @ "+channel.localAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        SocketChannel channel = (SocketChannel) ctx.channel();
        ReedLogger.debug("ReedAdmin channel exception caught: "+channel.id()+" @ "+channel.localAddress().toString()+" - "+cause.getMessage());
    }
}
