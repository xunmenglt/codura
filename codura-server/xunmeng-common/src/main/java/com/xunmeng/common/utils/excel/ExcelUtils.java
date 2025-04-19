package com.xunmeng.common.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtils<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
    private List<T> javaList;
    
    private Class<T> clazz;
    
    public ExcelUtils(Class<T> clazz,List<T> javaList){
        this.clazz=clazz;
        this.javaList=javaList;
    }
    
    
    public void exportExcel(HttpServletResponse response, String rawFileName) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        try {
            String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream())
                    .head(this.clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(fileName)
                    .doWrite(this.javaList);
        }catch (Exception e){
            log.error("导出Excel异常{}", e.getMessage());
        }

    }
}
