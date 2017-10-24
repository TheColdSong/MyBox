package com.gjp.exception;

/**
 * 秒杀关闭异常
 * Created by gjp on 0022 2017/10/22.
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
