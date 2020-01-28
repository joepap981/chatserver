package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import model.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {
    private String host;
    private int port;
    public static int localId;

    public static void main(String[] args) throws Exception{
        new ChatClient("localhost", 9090).run();
    }

    public ChatClient (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run (){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();

            String input;
            String receiverId;
            String content;

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            Message message = new Message();

            while (true) {
                input = in.readLine().trim();
                receiverId = input.substring(0, input.indexOf(' '));
                content = input.substring(input.indexOf(' ') + 1);

                message.setSenderId(localId);
                message.setReceiverId(Integer.parseInt(receiverId));
                message.setText(content);
                message.setLength(content.length());

                channel.writeAndFlush(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
