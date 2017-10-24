package com.gjp.dao;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gjp.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gjp on 0021 2017/10/21.
 */

/**
 * 配置spring与juint整合，juint启动加载springIOC容器
 * spring-test juint
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉juint spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    @Resource
    SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int i = seckillDao.reduceNumber(1000,killTime);
        System.out.println(i);
    }

    @Test
    public void queryById() throws Exception {
       SecKill secKill =  seckillDao.queryById(1000);
        System.out.println(secKill.getName());
    }

    @Test
    public void queryAll() throws Exception {
        //java`没有保存形参的记录
       List<SecKill> secKillList =  seckillDao.queryAll(0,100);

       for(SecKill secKill:secKillList){
           System.out.println(secKill.getName());
       }
    }

}