package com.gjp.dao;

import com.gjp.entity.SecKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by gjp on 0020 2017/10/20.
 */
public interface SeckillDao {
    //操作数据库 库存数量

    /**
     * 减少库存 入参为商品id 和 秒杀时间
     * @return 数量 影响行数
     */
    int reduceNumber(@Param("seckillId")long seckillId , @Param("killTime")Date killTime);

    /**
     * 查询秒杀库存通过Id
     * @param seckillId
     * @return
     */
    SecKill queryById(long seckillId);

    /**
     * param注解 来保存参数名 往xml文件中传参
     * 查询秒杀所有库存通过偏移量
     * @param
     *
     * @return
     */
    List<SecKill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
