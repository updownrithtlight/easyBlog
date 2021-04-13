package com.technerd.easyblog.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author technerd
 */
@Data
public class SearchVo implements Serializable {

    /**
     * 起始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
