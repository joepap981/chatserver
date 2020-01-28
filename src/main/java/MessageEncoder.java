import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class MessageEncoder extends MessageToByteEncoder<Message> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getSenderId());
        out.writeInt(msg.getReceiverId());
        out.writeInt(msg.getText().length());
        out.writeCharSequence(msg.getText(), charset);
    }
}
