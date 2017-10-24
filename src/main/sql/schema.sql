-- 数据库初始化

-- 创建数据库
CREATE database seckill;

use seckill;

-- 创建表
-- 自增每1000就增 ` 左上角波浪符
CREATE  TABLE  seckill(
 `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name` VARCHAR(120) NOT NULL  COMMENT '商品名称',
`number` int NOT NULL  COMMENT '库存数量',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀创建时间',
`start_time` TIMESTAMP NOT NULL  COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL  COMMENT '秒杀结束时间',

-- idx:index缩写
-- 索引
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀自增表';

-- 一些初始化数据

INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES ('1000元秒杀iPhone7P',100,'2017-10-20 00:00:00','2017-10-21 10:00:00'),
('100元秒杀iPhone7',200,'2017-10-20 00:00:00','2017-10-21 10:00:00'),
('50元秒杀xiaomi4',300,'2017-10-20 00:00:00','2017-10-21 10:00:00'),
('40元秒杀xiaomi3',400,'2017-10-20 00:00:00','2017-10-21 10:00:00');

-- 秒杀成功明细
-- 用户登陆认证

CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMNET 'ID',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT  -1 COMMENT '状态标识 -1 无效 0 成功 1 已付款 2 已发货',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀成功创建时间',
-- 联合主键
-- 通过两个主键唯一
PRIMARY KEY (seckill_id,user_phone),
KEY idx_create_time(create_time)

)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';