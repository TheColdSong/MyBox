package com.gjp.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.gjp.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by gjp on 0024 2017/10/24.
 */
public class RedisDao {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //连接池
    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip,port);
    }
    //protostuff 代替jdk serilizeble 通过反射得到schema 一个板式来序列化对象
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);
    /**
     * 通过redis拿到秒杀对象
     * @param seckillId
     * @return SecKill秒杀对象
     */
    public SecKill getSeckill(long seckillId){
        //redis的操作（从redis缓存中拿对象）
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                //redis key
                String key ="seckill:"+seckillId;
                //没有实现内部反序列化：字节数组转换成对象
                //访问缓存时 get字节数组 ->反序列化 ->seckill对象
                //prorstuff pojo
                byte[] bytes = jedis.get(key.getBytes());//得到对象字节数组
                //判断缓存是否获取
                if(bytes!=null){
                    //新创建一个空对象 属性没有赋值
                    SecKill secKill = schema.newMessage();
                    //合并一个序列化的对象
                    ProtobufIOUtil.mergeFrom(bytes,secKill,schema);
                    //seckill 被反序列化
                    return secKill;
                }

            }finally {
                jedis.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 如果redis没有，通过put把秒杀对象放入redis
     * @param
     * @return
     */
    public String putSeckill(SecKill secKill){
        //seckill->bytes字节数组->数据库缓存
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+secKill.getSeckillId();
               byte[] bytesOfObject =  ProtobufIOUtil.toByteArray(secKill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
                        );
               //超时缓存 时间到了缓存清除
                int timeOut = 60 * 60; //存储一小时
               //放入redis缓存
                String result = jedis.setex(key.getBytes(),timeOut,bytesOfObject);
                return result;
            }finally {
                jedis.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

}
