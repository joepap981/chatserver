package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import model.Message;

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
        ChannelMatcher matcher = null;
        Channel receiverChannel;

        String text = "[User " + message.getSenderId() + "] : " + message.getText();
        message.setText(text);
        message.setLength(text.length());

        if (message.getReceiverId() == 0) {
            matcher = (channel -> channel != incoming);
        } else {
            receiverChannel = channels.find(
                    channelMap.get(message.getReceiverId())
            );
            matcher = (channel -> channel == receiverChannel);
        }

        channels.writeAndFlush(message, matcher);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Message message;
        int newId = createSessionAndGetId(ctx.channel().id());
        System.out.println(ctx.channel().remoteAddress() + " (User " + newId + ") has joined");

        String joinerText = "[SERVER] - Logged in as User " + newId;
        message = new Message(
                0, newId, joinerText.length(), joinerText);
        ctx.channel().writeAndFlush(message);

        String broadcastText = "[SERVER] - User " + newId + " has joined";
        message = new Message(
                -1, newId, broadcastText.length(), broadcastText
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

        //TODO delete from map

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
