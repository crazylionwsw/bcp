package com.fuze.bcp.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created by Lily on 2017/5/25.
 */
public class KryoRedisSerializer<T>  implements RedisSerializer<T> {
    private Kryo kryo = new Kryo();

    @Override
    public byte[] serialize(T t) throws SerializationException {
        byte[] buffer = new byte[2048];
        Output output = new Output(buffer);
        kryo.writeClassAndObject(output, t);
        return output.toBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Input input = new Input(bytes);
        @SuppressWarnings("unchecked")
        T t = (T) kryo.readClassAndObject(input);
        return t;
    }

}
