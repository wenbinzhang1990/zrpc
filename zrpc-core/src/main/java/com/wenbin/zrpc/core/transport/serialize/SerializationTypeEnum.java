package com.wenbin.zrpc.core.transport.serialize;

/**
 * The serialization type enum
 * @author wenbin
 * Date：2023/3/31
 */
public enum SerializationTypeEnum {
    /**
     * The protostuff serialization
     */
    PROTOSTUFF((byte) 0x00),

    /**
     * The hessian serialization
     */
    HESSIAN((byte) 0x01),
    ;

    private byte code;

    SerializationTypeEnum(byte code) {
        this.code = code;
    }

    public Byte getByte() {
        return code;
    }

    public static SerializationTypeEnum getSerializationTypeEnum(byte code) {
        for (SerializationTypeEnum type : SerializationTypeEnum.values()) {
            if (type.getByte() == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("No such serialization type: " + code);
    }
}
