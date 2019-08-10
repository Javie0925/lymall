package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/25 10:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {

    private String k;
    private Boolean searchable;
    private Boolean global;
    private Boolean numerical;
    private String unit;
    private String v;
    private List<String> options;
}
