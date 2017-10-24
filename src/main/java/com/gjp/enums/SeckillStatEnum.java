package com.gjp.enums;

/**
 *
 * 枚举表示常量数据字典
 * Created by gjp on 0022 2017/10/22.
 */
public enum SeckillStatEnum {
    SUCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复性秒杀"),
    INNER_ERROR(-2,"异常出现"),
    DATA_REWRITE(-3,"秒杀值被篡改");

    private int state;

    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 根据state字段返回枚举
     * values() 枚举数组
     * @param index
     * @return
     */
    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum seckillStatEnum:values()){
            if(seckillStatEnum.getState() == index){
                return seckillStatEnum;
            }
        }

        return null;
    }
}
