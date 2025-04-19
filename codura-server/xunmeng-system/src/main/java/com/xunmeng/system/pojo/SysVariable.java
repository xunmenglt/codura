package com.xunmeng.system.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统变量表
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_variable")
@ApiModel(value="SysVariable对象", description="系统变量表")
public class SysVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "变量key")
    @TableId("variable_key")
    private String variableKey;

    @ApiModelProperty(value = "变量key")
    @TableField("variable_value")
    private String variableValue;

    @ApiModelProperty(value = "变量类型(int,str,boolen)")
    @TableField("variable_type")
    private String variableType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
