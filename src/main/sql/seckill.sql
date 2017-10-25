-- 秒杀执行的存储过程
DELIMITER $$ -- 转换限定符 ;->$$

CREATE PROCEDURE `seckill`.`execute_seckill`
  (in v_seckill_id bigint, in v_phone bigint , in v_kill_time TIMESTAMP,
    out r_result INT
  )
  BEGIN
  DECLARE insert_count int DEFAULT 0;
  START TRANSACTION
  INSERT ignore INTO success_killed
  (seckill_id,user_phone,create_time)
  values(v_seckill_id,v_phone,v_kill_time);
  -- 一个计算影响行数的函数 row——count（）
  SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      SET r_result = -1; -- 重复秒杀 对应enum枚举中的数据 不是秒杀成功的state
    ELSEIF (insert_count < 0) THEN
      ROLLBACK ;
      SET  r_result = -2; -- 秒杀异常
    ELSE
  -- 开始秒杀减库存
      UPDATE seckill
      SET NUMBER=NUMBER - 1
      WHERE seckill_id = v_seckill_id
      AND end_time > v_kill_time
      AND start_time < v_kill_time
      AND NUMBER > 0;
      SELECT ROW_COUNT() INTO insert_count;
        IF (insert_count = 0) THEN
        ROLLBACK;
        SET r_result = 0; -- 秒杀结束
        ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        SET r_result = -2; -- 秒杀异常
        ELSE
        COMMIT ;
        SET r_result = 1;
        END IF;
    END IF;
END ;
$$