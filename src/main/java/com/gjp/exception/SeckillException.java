package com.gjp.exception;

/**
 * 所有秒杀异常
 * Created by gjp on 0022 2017/10/22.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
