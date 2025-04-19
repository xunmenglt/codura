package com.xunmeng.common.core.pojo.model;

import lombok.Data;

@Data
public class AuditCommentBody {
    /*文章id*/
    private Long commentId;
    /*审核状态*/
    private Integer enable;
    /*审核信息*/
    private String auditInfo;
}
