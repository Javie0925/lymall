package com.leyou.common.vo;

import com.leyou.common.enmus.ExceptionEnmu;
import lombok.Data;

@Data
public class ExceptionResult {

    private int status;
    private String msg;
    private Long timestamp;

    public ExceptionResult(ExceptionEnmu em) {
        this.status = em.getCode();
        this.msg = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
