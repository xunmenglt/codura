package com.xunmeng.system.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("model_service_config")
@ApiModel(value="ModelServiceConfig", description="")
public class ModelServiceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "服务名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "接口类型")
    @TableField("openai_type")
    private String openaiType;

    @ApiModelProperty(value = "业务类型")
    @TableField("business_type")
    private Integer businessType;

    @ApiModelProperty(value = "填充类型")
    @TableField("fill_type")
    private String fillType;

    @ApiModelProperty(value = "模型名称")
    @TableField("model_name")
    private String modelName;

    @ApiModelProperty(value = "路径")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "apikey")
    @TableField("api_key")
    private String apiKey;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime=new Date();

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime=new Date();

    @ApiModelProperty(value = "用户名（唯一）")
    @TableField("user_name")
    private String userName;
}
