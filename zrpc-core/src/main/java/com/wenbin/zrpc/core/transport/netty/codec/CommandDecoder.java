package com.wenbin.zrpc.core.transport.netty.codec;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import com.wenbin.zrpc.core.transport.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * The command decoder
 * @author wenbin
 * Date：2023/4/7
 */
public class CommandDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable(RpcProtocol.PROTOCOL_LENGTH)) {
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();
        if (!in.isReadable(length)) {
            in.resetReaderIndex();
            return;
        }

        RpcHeader rpcHeader = decodeHeader(in);
        RpcCommand rpcCommand = new RpcCommand(rpcHeader, decodeBody(in, length - rpcHeader.getHeaderLength()));
        out.add(rpcCommand);
    }


    protected RpcHeader decodeHeader(ByteBuf byteBuf) {
        return new RpcHeader(byteBuf.readLong(), byteBuf.readByte());
    }

    private byte[] decodeBody(ByteBuf byteBuf, int bodyLength) {
        byte[] body = new byte[bodyLength];
        byteBuf.readBytes(body);
        return body;
    }
}
