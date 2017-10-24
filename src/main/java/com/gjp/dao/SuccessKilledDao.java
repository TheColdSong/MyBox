package com.gjp.dao;

import com.gjp.entity.SecKill;
import com.gjp.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by gjp on 0020 2017/10/20.
 */
public interface SuccessKilledDao {

    /**
     * 通过ID和用户插入明细
     * @param seckillId
     * @param userPhone
     * @return 影响行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId ,@Param("userPhone") long userPhone);

    /**
     * 根据ID查询成功并有秒杀对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
