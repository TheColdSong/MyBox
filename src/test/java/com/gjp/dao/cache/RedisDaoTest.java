package com.gjp.dao.cache;

import com.gjp.dao.SeckillDao;
import com.gjp.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by gjp on 0025 2017/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    //测试秒杀ID
    private long id = 1005;
    //测试dao
    @Resource
    private RedisDao redisDao;
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void testSeckill() throws Exception {

        SecKill secKill = redisDao.getSeckill(id);
        if (secKill==null){
          secKill =  seckillDao.queryById(id);
          if(secKill!=null) {
              String status = redisDao.putSeckill(secKill);
              System.out.println(status + ":" + secKill);
          }
        }else {
            System.out.println(secKill);
        }
    }

}