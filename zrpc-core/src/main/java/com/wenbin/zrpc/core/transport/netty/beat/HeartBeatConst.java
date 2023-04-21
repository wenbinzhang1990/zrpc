package com.wenbin.zrpc.core.transport.netty.beat;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import com.wenbin.zrpc.core.transport.protocol.RpcRequest;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;

/**
 * The const for heart beat
 * @author wenbin
 * Date：2023/3/30
 */
public class HeartBeatConst {

    public static final int BEAT_TIME = 60;

    public static final int BEAT_TIME_OUT = 3 * BEAT_TIME;

    public static long BEAT_PING_ID = 0L;

    public static RpcCommand BEAT_PING;

    static {
        BEAT_PING = new RpcCommand(new RpcHeader(BEAT_PING_ID, SerializationTypeEnum.PROTOSTUFF.getByte()), null);
    }
}
