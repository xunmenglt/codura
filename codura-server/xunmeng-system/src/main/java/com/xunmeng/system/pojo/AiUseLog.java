package com.xunmeng.system.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigInteger;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ai_use_log")
@ApiModel(value="AiUseLog对象", description="")
public class AiUseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志唯一ID")
    private String id;

    @ApiModelProperty(value = "日志类型")
    private String type;

    @ApiModelProperty(value = "时间戳")
    private Long timestamp;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "IP地址")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty(value = "mac地址")
    @TableField("mac_address")
    private String macAddress;

    @ApiModelProperty(value = "IDE版本号")
    @TableField("ide_version")
    private String ideVersion;

    @ApiModelProperty(value = "日志通用记录字段")
    private String value;

    @ApiModelProperty(value = "插件使用类型")
    @TableField("event_type")
    private String eventType;

    @ApiModelProperty(value = "用户输入")
    @TableField("input_content")
    private String inputContent;

    @ApiModelProperty(value = "插件输出")
    @TableField("output_content")
    private String outputContent;

    @ApiModelProperty(value = "输入长度")
    @TableField("input_len")
    private Long inputLen;
    

    @ApiModelProperty(value = "输出长度")
    @TableField("output_len")
    private Long outputLen;

    @ApiModelProperty(value = "输入token长度")
    @TableField("input_tokens")
    private Long inputTokens;

    @ApiModelProperty(value = "输出token长度")
    @TableField("output_tokens")
    private Long outputTokens;
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
