package com.gjp.service;

import com.gjp.dto.Exposer;
import com.gjp.dto.SeckillExecution;
import com.gjp.entity.SecKill;
import com.gjp.exception.RepeatKillException;
import com.gjp.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gjp on 0023 2017/10/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-service.xml","classpath:spring/spring-dao.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(SeckillServiceTest.class);

    @Resource
    private SeckillService seckillService;

    @Test
    public void querySeckillList() throws Exception{
        List<SecKill> secKillList = seckillService.querySeckillList();

            logger.info("seckill的数据列表={}",secKillList);

    }

    @Test
    public void getById() throws Exception {
        SecKill seckill = seckillService.getById(1000);
        logger.info("seckill={}",seckill);
    }

    //暴漏地址和秒杀执行合一
    @Test
    public void exportSeckillUrl() throws Exception {
        long userPhone = 13888881115L;
        Exposer exposer = seckillService.exportSeckillUrl(1001);
        if( exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.excuteSeckill(1001, userPhone, md5);
            try{
                logger.info("result={}",seckillExecution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e1){
                logger.error(e1.getMessage());
            }
        }else {
            logger.warn("秒杀未开启={}",exposer);
        }
    }

//    @Test
//    public void excuteSeckill() throws Exception {
//        SeckillExecution seckillExecution = seckillService.excuteSeckill(1000, 13434355232L, md5);
//        try{
//            logger.info("result={}",seckillExecution);
//        }catch (RepeatKillException e){
//               logger.error(e.getMessage());
//        }catch (SeckillCloseException e1){
//               logger.error(e1.getMessage());
//        }
//
//    }

}