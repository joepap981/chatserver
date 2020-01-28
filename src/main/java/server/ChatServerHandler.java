package server;

import chat.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map <Integer, ChannelId> channelMap = new HashMap<>();
    private static int idSeq = 1;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        Channel incoming = ctx.channel();
        Channel receiverChannel = channels.find(
                channelMap.get(message.getReceiverId())
        );

        String text = "[User " + message.getReceiverId() + "] : " + message.getText();
        message.setText(text);
        message.setLength(text.length());

        ChannelMatcher matcher = (channel -> channel == receiverChannel);
        channels.writeAndFlush(message, matcher);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        int newId = createSessionAndGetId(ctx.channel().id());

        System.out.println(ctx.channel().remoteAddress() + " (User " + newId + ") has joined");
        String text = "[SERVER] - User " + newId + " has joined";
        int textLen = text.length();

        Message message = new Message(
                0, newId, textLen, text
        );

        channels.writeAndFlush(message);
        channels.add(ctx.channel());


    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " has left");

        channelMap.entrySet().stream()
                .filter(e -> e.getValue() == ctx.channel().id())
                .map(e -> channels.remove(e.getValue()));

        String text = "\n[SERVER] - " + ctx.channel().remoteAddress() + " has left";
        Message message = new Message();
        message.setText(text);
        message.setLength(text.length());

        //TODO map에서 remove

        channels.remove(ctx.channel());
        channels.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private static synchronized int createSessionAndGetId(ChannelId channelId) {
        channelMap.put(idSeq, channelId);
        return idSeq++;
    }
}
