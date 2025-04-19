package com.xunmeng.common.core.pojo.model;

import lombok.Data;

@Data
public class JoinEmbeddingBody {
    /*文章id*/
    private String articleId;
    
    /*是否向量化*/
    private Integer isEmbedding;
}
