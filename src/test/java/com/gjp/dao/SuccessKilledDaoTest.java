package com.gjp.dao;

import com.gjp.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by gjp on 0021 2017/10/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
       int i  = successKilledDao.insertSuccessKilled(1001,13514527318L);
       System.out.println(i);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
          SuccessKilled successKilled =  successKilledDao.queryByIdWithSeckill(1001,13514527318L);
        System.out.println(successKilled);
    }

}