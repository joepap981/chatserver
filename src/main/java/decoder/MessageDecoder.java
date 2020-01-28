package decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import model.Message;

import java.nio.charset.Charset;
import java.util.List;

public class MessageDecoder extends ReplayingDecoder<Message> {

    private final Charset charset = Charset.forName("UTF-8");



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Message message = new Message();
        message.setSenderId(in.readInt());
        message.setReceiverId(in.readInt());
        message.setLength(in.readInt());
        message.setText(in.readCharSequence(message.getLength(),charset).toString());
        out.add(message);
    }
}
