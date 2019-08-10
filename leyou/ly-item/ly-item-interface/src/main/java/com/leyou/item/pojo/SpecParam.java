package com.leyou.item.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/25 10:54
 */
@Data
public class SpecParam {

    private String group;
    private List<Param> params;
}
