package com.wenbin.zrpc.core.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The rpc header
 * @author wenbin
 * Date：2023/3/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcHeader {

    private long requestId;

    private byte serializationType;

    public int getHeaderLength() {
        return Long.BYTES + Byte.BYTES;
    }
}
