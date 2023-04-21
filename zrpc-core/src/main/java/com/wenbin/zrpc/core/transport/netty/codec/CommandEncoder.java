package com.wenbin.zrpc.core.transport.netty.codec;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * The command decoder
 * @author wenbin
 * Date：2023/4/7
 */
public class CommandEncoder extends MessageToByteEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand command, ByteBuf out) throws Exception {
        out.writeInt(command.getCommandLength());
        encodeHeader(command.getHeader(), out);
        out.writeBytes(command.getBody());
    }

    protected void encodeHeader(RpcHeader header, ByteBuf byteBuf) {
        byteBuf.writeLong(header.getRequestId());
        byteBuf.writeByte(header.getSerializationType());
    }
}
