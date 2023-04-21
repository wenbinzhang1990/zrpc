package com.wenbin.zrpc.core.transport.serialize;

import java.util.HashMap;
import java.util.Map;

/**
 * The serialization support
 * @author wenbin
 * Date：2023/3/31
 */
public class SerializationSupport {

    private static Map<SerializationTypeEnum, Serializer> map = new HashMap<>();

    static {
        map.put(SerializationTypeEnum.PROTOSTUFF, new ProtostuffSerializer());
        map.put(SerializationTypeEnum.HESSIAN, new HessianSerializer());
    }

    public static Serializer getSerializer(SerializationTypeEnum type) {
        return map.get(type);
    }
}
