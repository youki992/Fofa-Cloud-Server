package com.fofa.entity;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;

@Data
public class ResultEntity {
    @ExcelProperty(value = "IP")
    String ip;

    @ExcelProperty(value = "端口号")
    String port;

    @ExcelProperty(value = "协议")
    String protocol;

    @ExcelProperty(value = "URL")
    String link;

    @ExcelProperty(value = "域名")
    String domain;

    @ExcelProperty(value = "标题")
    String title;

    @ExcelProperty(value = "网站server")
    String server;

    @ExcelProperty(value = "区域")
    String region;

    @ExcelProperty(value = "所在城市")
    String city;
}
