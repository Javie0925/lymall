package com.leyou.order.enums;

/**
 * @author javie
 * @date 2019/6/2 15:35
 */
public enum  PayStatusEnum {

    NOT_PAY(0),
    SUCCESS(1),
    FAIL(2),
    ;
    private Integer status;

    PayStatusEnum(Integer status) {
        this.status = status;
    }
    public int getValue(){
        return this.status;
    }
}
