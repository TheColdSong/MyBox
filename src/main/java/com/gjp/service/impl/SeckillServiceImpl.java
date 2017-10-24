package com.gjp.service.impl;

import com.gjp.dao.SeckillDao;
import com.gjp.dao.SuccessKilledDao;
import com.gjp.dto.Exposer;
import com.gjp.dto.SeckillExecution;
import com.gjp.entity.SecKill;
import com.gjp.entity.SuccessKilled;
import com.gjp.enums.SeckillStatEnum;
import com.gjp.exception.RepeatKillException;
import com.gjp.exception.SeckillCloseException;
import com.gjp.exception.SeckillException;
import com.gjp.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by gjp on 0022 2017/10/22.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);
    //需要配置Spring xml文件
    @Resource
    private SeckillDao seckillDao;
    @Resource
    private SuccessKilledDao successKilledDao;
    //md5的盐值混淆
    private final String salt= "hdasjh7**@^Y&*(61&**￥378*^#%%^=23z987*(~&!@(*@";
    @Override
    public List<SecKill> querySeckillList() {
       List<SecKill> list  = seckillDao.queryAll(0,5);
        return list;
    }

    @Override
    public SecKill getById(long seckillId) {
        SecKill secKill = seckillDao.queryById(seckillId);
        return secKill;
    }

    /**
     * 暴露秒杀地址
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //开启秒杀判斷
        SecKill secKill = seckillDao.queryById(seckillId);
        if (secKill==null){
            return new Exposer(false,seckillId);
        }
         Date startTime = secKill.getStartTime();
         Date endTime =  secKill.getEndTime();
         //系统当前时间
         Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //轉化md5 不可逆
        //以上都不满足这才开启秒杀哦！
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return SeckillExecution 秒杀执行后封装对象
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */

    @Override
    /**
     * 注解事务优点
     * 1.开发项目组的约定，明确一种编程风格
     * 2.保证该配置事务方法的执行时间尽量短，不要插入网络操作（spring通过aop来实现，第一次操作 开起事务 有异常则回滚）
     * 3.不要为每个方法提供事务
     */
    @Transactional
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5==null || ! md5.equals(getMD5(seckillId))){
            throw new SeckillException("md5不一致，改写过了！");
        }
        Date nowTime = new Date();
        try {
            //执行秒杀 减少库存量同时增加用户行为
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount<=0){
                //没有更新 返回结束
                throw new SeckillCloseException("秒杀结束");
            }else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //唯一验证
                if (insertCount <=0){
                    throw  new RepeatKillException("重复秒杀");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCESS,successKilled);
                }

            }
        } catch (SeckillCloseException e1){
                throw e1;
        } catch (RepeatKillException e2){
                throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //编译期异常转为运行时，在spring事务管理中有用，可以回滚
            throw new SeckillException("seckill 内部异常");
        }

    }
    //产生MD5
    private String getMD5(long seckillId){
       String base =  seckillId+"/"+salt;
       String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
       return md5;
    }
}
