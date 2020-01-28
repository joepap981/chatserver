package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import model.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ChatClientHandler extends ChannelInboundHandlerAdapter {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;

        if (message.getSenderId() == 0) {
            ChatClient.localId = message.getReceiverId();
        }

        System.out.println(message.getText());
    }
}
