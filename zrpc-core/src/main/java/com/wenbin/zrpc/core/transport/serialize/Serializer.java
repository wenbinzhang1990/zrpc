package com.wenbin.zrpc.core.transport.serialize;

/**
 * The serializer
 * @author wenbin
 * Date：2023/3/31
 * @Description
 */
public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
