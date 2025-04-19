package com.xunmeng.common.core.pojo.model;

import lombok.Data;

@Data
public class JoinSearchBody {
    /*文章id*/
    private String articleId;
    
    /*是否检索*/
    private Integer isSearch;
}
