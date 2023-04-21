package com.wenbin.zrpc.core.transport.netty.codec;

import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import com.wenbin.zrpc.core.transport.protocol.RpcResponseHeader;
import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;

/**
 * The response encoder
 * @author wenbin
 * Date：2023/4/7
 */
public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(RpcHeader header, ByteBuf byteBuf) {
        super.encodeHeader(header, byteBuf);
        RpcResponseHeader rpcResponseHeader = (RpcResponseHeader) header;
        byteBuf.writeInt(rpcResponseHeader.getErrorCode());
        byteBuf.writeInt(rpcResponseHeader.getErrorMsgLength());
        if (rpcResponseHeader.getErrorMsgLength() != 0) {
            byteBuf.writeBytes(rpcResponseHeader.getErrorMsg().getBytes(Charset.defaultCharset()));
        }
    }
}
