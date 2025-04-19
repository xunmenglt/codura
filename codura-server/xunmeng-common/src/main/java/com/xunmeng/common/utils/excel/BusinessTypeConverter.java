package com.xunmeng.common.utils.excel;

import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.xunmeng.common.enums.BusinessType;

public class BusinessTypeConverter implements Converter<String> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext context) throws Exception {
        try {
            Object value = context.getValue();
            Integer index=null;
            if (value instanceof Integer){
                index=(Integer) value;
            }
            return new WriteCellData<>(BusinessType.values()[index.intValue()].getLabel());
        }catch (Exception e){
            return new WriteCellData<>("null");
        }
    }
    
}
