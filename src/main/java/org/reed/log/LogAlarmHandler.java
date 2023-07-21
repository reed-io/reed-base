/**
 * springbootup @ org.reed.log.LogAlarmHandler.java
 */
package org.reed.log;

import org.reed.admin.ReedAdminChannelHandler;
import org.reed.bootup.ReedStarter;
import org.reed.system.ReedContext;
import org.reed.utils.EnderUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;

/**
 * @author chenxiwen
 * @createTime 2021-12-12 15:41
 */
public final class LogAlarmHandler implements LogAlarmEventListener{
    private static LogAlarmHandler instance;

    private ReedAdminChannelHandler reedAdminChannelHandler;
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private String host;

    private ChannelFuture future;

    private LogAlarmHandler(){}



    private LogAlarmHandler(final String host){
        reedAdminChannelHandler = new ReedAdminChannelHandler();
        group = new NioEventLoopGroup();
        this.host = host;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    //todo error future.channel null
                    if(future == null || !future.channel().isActive()){
//                    if(!future.channel().isActive()){
                        ReedLogger.debug("connect to reed admin...");
                        connect();
                    }
                    EnderUtil.block(15000);
                }catch(Exception e){
                    e.printStackTrace();
                    ReedLogger.error(e.getMessage());
                }
            }
        }).start();
    }

    private void connect() throws InterruptedException{
        bootstrap = new Bootstrap().group(group)
                .option(ChannelOption.TCP_NODELAY, true) //禁止使用Nagle算法，使用于小数据即时传输
                .channel(NioSocketChannel.class)
                .handler(reedAdminChannelHandler);
        future = bootstrap.connect(host, 33550).sync();
//                            .sync();
    }

    public static LogAlarmHandler getInstance(String reedAdminServerUrl){
        if(instance == null){
            String host = null;
            if(reedAdminServerUrl.contains(",")){
                String[] hosts = reedAdminServerUrl.split(",");
                host = hosts[new Random().nextInt(hosts.length-1)];
                if(host.contains("://")){
                    host = host.split("://")[1].split(":")[0].split("/")[0];
                }
            }else{
                host = reedAdminServerUrl.split("://")[1].split(":")[0].split("/")[0];
            }
            instance = new LogAlarmHandler(host);
        }
        return instance;
    }

    @Override
    public void handleLoggerAlarmEvent(LogAlarmEvent event) {
        if(future != null && future.channel().isActive()){
            System.out.println("--------------");
            System.out.println(event.getLoggerObject().toString());
            System.out.println("--------------");
            future.channel().writeAndFlush(event.getLoggerObject().toString());
            ReedLogger.debug("sent loggerObject to reed admin!");
        }else{
            ReedLogger.info("abandon LoggerObject: "+event.getLoggerObject().toString());
        }
    }
}
