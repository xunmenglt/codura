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
@TableName("fim_config")
@ApiModel(value = "FimConfig", description = "代码补全模型配置")
public class FimConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "最大生成文本行数")
    @TableField("max_lines")
    private Integer maxLines;

    @ApiModelProperty(value = "最大生成token数")
    @TableField("max_tokens")
    private Integer maxTokens;

    @ApiModelProperty(value = "温度参数")
    @TableField("temperature")
    private Double temperature;

    @ApiModelProperty(value = "是否开启文件上下文")
    @TableField("enable_file_context")
    private Boolean enableFileContext;

    @ApiModelProperty(value = "是否开启连续补全")
    @TableField("enable_continuous_completion")
    private Boolean enableContinuousCompletion;

    @ApiModelProperty(value = "是否开启补全缓存")
    @TableField("enable_completion_cache")
    private Boolean enableCompletionCache;

    @ApiModelProperty(value = "是否开启多行补全")
    @TableField("enable_multiline_completion")
    private Boolean enableMultilineCompletion;

    @ApiModelProperty(value = "是否开启补全功能")
    @TableField("enable_completion")
    private Boolean enableCompletion;

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
