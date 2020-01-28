package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class ChatServer {

    private int PORT;

    public static void main(String[] args) throws Exception{
        new ChatServer(9090).run();
    }

    public ChatServer (int PORT) {
        this.PORT = PORT;
    }

    public void run () throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer());
            ChannelFuture future = bootstrap.bind(PORT).sync();
            System.out.println("Server is running on port " + PORT);
            future.channel().closeFuture().sync();
        } finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.printf("Shutting down server...");
        }

    }
}
