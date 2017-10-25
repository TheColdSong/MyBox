package com.gjp.service;

import com.gjp.dto.Exposer;
import com.gjp.dto.SeckillExecution;
import com.gjp.entity.SecKill;
import com.gjp.exception.RepeatKillException;
import com.gjp.exception.SeckillCloseException;
import com.gjp.exception.SeckillException;

import java.util.List;

/**
 * 设计业务接口 站在 使用者 角度
 * 三个方面 方法定义明确 参数 返回类型 或 异常
 * Created by gjp on 0022 2017/10/22.
 */
public interface SeckillService {

    //列表页 展示所有秒杀产品
    List<SecKill>  querySeckillList();
    //查询单个信息
    SecKill getById(long seckillId);
    //秒杀开始了输出秒杀接口地址 , 否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);
    //执行秒杀
    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5) throws
            SeckillException,RepeatKillException,SeckillCloseException;
    //执行秒杀通过存储过程
    SeckillExecution excuteSeckillByProcedure(long seckillId, long userPhone, String md5);
}
