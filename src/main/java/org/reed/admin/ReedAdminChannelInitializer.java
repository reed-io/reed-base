/**
 * springbootup @ org.reed.admin.ReedAdminChannelInitializer.java
 */
package org.reed.admin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 15:50
 */
public class ReedAdminChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ReedAdminChannelHandler reedAdminChannelHandler = new ReedAdminChannelHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("decoder", new StringDecoder());
        socketChannel.pipeline().addLast("encoder", new StringEncoder());
        socketChannel.pipeline().addLast(reedAdminChannelHandler);
    }
}
