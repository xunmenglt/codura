package com.xunmeng.codura.net.response;

import lombok.Data;

/**
 * 响应数据类
 */
@Data
public abstract class ResponseDataBaseItem {
    public abstract String contentValue();
}
