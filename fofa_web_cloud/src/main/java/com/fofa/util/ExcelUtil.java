package com.fofa.util;

import com.alibaba.excel.EasyExcel;
import com.fofa.entity.ResultEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {

    public static void export(HttpServletResponse response, String fileName, String sheetName, List<ResultEntity> data, Class<ResultEntity> modelClass) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 创建ExcelWriter
        EasyExcel.write(response.getOutputStream(), modelClass)
                .sheet(sheetName)
                .doWrite(data);
    }
}
