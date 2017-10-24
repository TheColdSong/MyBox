package com.gjp.exception;

/**
 * 重复秒杀异常 (runtime异常)
 * Created by gjp on 0022 2017/10/22.
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
