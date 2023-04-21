package com.wenbin.zrpc.core.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The rpc command
 * @author wenbin
 * Date：2023/4/7
 */
@AllArgsConstructor
@Getter
public class RpcCommand implements RpcProtocol {

    private RpcHeader header;

    private byte[] body;

    public int getCommandLength() {
        return header.getHeaderLength() + body.length;
    }
}
