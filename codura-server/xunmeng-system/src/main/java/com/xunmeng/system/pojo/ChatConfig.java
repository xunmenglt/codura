package com.xunmeng.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_config")
@ApiModel(value = "ChatConfig", description = "聊天模型配置")
public class ChatConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "最大生成token数")
    @TableField("max_tokens")
    private Integer maxTokens;

    @ApiModelProperty(value = "温度参数")
    @TableField("temperature")
    private Double temperature;

    @ApiModelProperty(value = "历史对话轮数")
    @TableField("history_turns")
    private Integer historyTurns;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime = new Date();

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime = new Date();

    @ApiModelProperty(value = "用户名（唯一）")
    @TableField("user_name")
    private String userName;
}
