package com.xunmeng.common.core.pojo.model;

import lombok.Data;

@Data
public class AuditBody {
    
    /*文章id*/
    private String articleId;
    /*审核状态*/
    private Integer auditStatus;
    /*审核信息*/
    private String auditInfo;
}
