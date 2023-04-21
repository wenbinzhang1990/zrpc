package com.wenbin.zrpc.core.transport.netty.codec;

import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import com.wenbin.zrpc.core.transport.protocol.RpcResponseHeader;
import io.netty.buffer.ByteBuf;

/**
 * description
 * @author wenbin
 * Date：2023/4/7
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected RpcHeader decodeHeader(ByteBuf byteBuf) {
        RpcResponseHeader rpcHeader = new RpcResponseHeader();
        rpcHeader.setRequestId(byteBuf.readLong());
        rpcHeader.setSerializationType(byteBuf.readByte());
        rpcHeader.setErrorCode(byteBuf.readInt());
        int length = byteBuf.readInt();
        if (length != 0) {
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            rpcHeader.setErrorMsg(new String(bytes));
        }

        return rpcHeader;
    }
}
