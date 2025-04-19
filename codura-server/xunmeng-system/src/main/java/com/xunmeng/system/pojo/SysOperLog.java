package com.xunmeng.system.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunmeng.common.utils.excel.BusinessTypeConverter;
import com.xunmeng.common.utils.excel.ConvertToExcelDate;
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
 * @since 2024-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_oper_log")
@ApiModel(value="SysOperLog对象", description="")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志主键")
    @TableId(value = "oper_id", type = IdType.AUTO)
    @ExcelProperty("日志编号")
    private Long operId;

    @ApiModelProperty(value = "模块标题")
    @ExcelProperty("模块标题")
    private String title;

    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
    @TableField("business_type")
    @ExcelProperty(value = "业务类型",converter = BusinessTypeConverter.class)
    private Integer businessType;

    @ApiModelProperty(value = "方法名称")
    @ExcelProperty(value = "方法名称")
    private String method;

    @ApiModelProperty(value = "请求方式")
    @TableField("request_method")
    @ExcelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）")
    @TableField("operator_type")
    @ExcelProperty(value = "操作类别")
    private Integer operatorType;

    @ApiModelProperty(value = "操作人员")
    @TableField("oper_name")
    @ExcelProperty(value = "操作人员")
    private String operName;

    @ApiModelProperty(value = "请求URL")
    @TableField("oper_url")
    @ExcelProperty(value = "请求URL")
    private String operUrl;

    @ApiModelProperty(value = "主机地址")
    @TableField("oper_ip")
    @ExcelProperty(value = "主机地址")
    private String operIp;

    @ApiModelProperty(value = "操作地点")
    @TableField("oper_location")
    @ExcelProperty(value = "操作地点")
    private String operLocation;

    @ApiModelProperty(value = "请求参数")
    @TableField("oper_param")
    @ExcelProperty(value = "请求参数")
    private String operParam;

    @ApiModelProperty(value = "返回参数")
    @TableField("json_result")
    @ExcelProperty(value = "返回参数")
    private String jsonResult;

    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    @ExcelProperty(value = "操作状态")
    private Integer status;

    @ApiModelProperty(value = "错误消息")
    @TableField("error_msg")
    @ExcelProperty(value = "错误消息")
    private String errorMsg;

    @ApiModelProperty(value = "耗时（毫秒）")
    @TableField("cost_time")
    @ExcelProperty(value = "耗时（毫秒）")
    private Long costTime;

    @ApiModelProperty(value = "操作时间")
    @TableField("oper_time")
    @ExcelProperty(value = "操作时间",converter = ConvertToExcelDate.class)
    private Date operTime;

    /*附加信息*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ExcelIgnore
    private Map<String,Object> params;


    public Map<String, Object> getParams() {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }
}
