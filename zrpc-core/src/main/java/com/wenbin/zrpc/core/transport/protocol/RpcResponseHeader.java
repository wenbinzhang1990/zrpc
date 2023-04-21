package com.wenbin.zrpc.core.transport.protocol;

import java.nio.charset.StandardCharsets;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * The rpc header
 * @author wenbin
 * Date：2023/3/31
 */
@Getter
@Setter
public class RpcResponseHeader extends RpcHeader {

    private static final int SUCCESS = 0;

    private int errorCode;

    private String errorMsg;

    public static RpcResponseHeader createSuccess(long requestId, byte serializationType) {
        return new RpcResponseHeader(SUCCESS, null, requestId, serializationType);
    }

    public static RpcResponseHeader createFail(int errorCode, String errorMsg, long requestId, byte serializationType) {
        return new RpcResponseHeader(errorCode, errorMsg, requestId, serializationType);
    }

    public RpcResponseHeader(int errorCode, String errorMsg, long requestId, byte serializationType) {
        super(requestId, serializationType);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public RpcResponseHeader() {
    }

    @Override
    public int getHeaderLength() {
        return super.getHeaderLength() + Integer.BYTES + getErrorMsgLength();
    }

    public int getErrorMsgLength() {
        return errorMsg == null ? 0 : errorMsg.getBytes(StandardCharsets.UTF_8).length;
    }
}
