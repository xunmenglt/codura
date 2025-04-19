package com.xunmeng.system.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("user_use_info")
@ApiModel(value="UserUseInfo对象", description="")
public class UserUseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private String userId;
    @ApiModelProperty(value = "昵称")
    @TableField(exist = false)
    private String nickName;

    @ApiModelProperty(value = "代码补全字符数")
    @TableField("code_completion_chars")
    private Long codeCompletionChars=0L;

    @ApiModelProperty(value = "代码补全token数")
    @TableField("code_completion_tokens")
    private Long codeCompletionTokens=0L;

    @ApiModelProperty(value = "代码问答qa次数")
    @TableField("code_completion_qa_times")
    private Long codeCompletionQaTimes=0L;

    @ApiModelProperty(value = "代码问答qa字符数")
    @TableField("code_completion_qa_chars")
    private Long codeCompletionQaChars=0L;

    @ApiModelProperty(value = "代码问答qa token数")
    @TableField("code_completion_qa_tokens")
    private Long codeCompletionQaTokens=0L;

    @ApiModelProperty(value = "测试用例编写次数")
    @TableField("test_case_writing_chars")
    private Long testCaseWritingChars=0L;

    @ApiModelProperty(value = "测试用例编写token数")
    @TableField("test_case_writing_tokens")
    private Long testCaseWritingTokens=0L;

    @ApiModelProperty(value = "变量类型声明字符数")
    @TableField("variable_type_declaration_chars")
    private Long variableTypeDeclarationChars=0L;

    @ApiModelProperty(value = "变量类型声明token数")
    @TableField("variable_type_declaration_tokens")
    private Long variableTypeDeclarationTokens=0L;

    @ApiModelProperty(value = "代码解释字符数")
    @TableField("code_explanation_chars")
    private Long codeExplanationChars=0L;

    @ApiModelProperty(value = "代码解释token数")
    @TableField("code_explanation_tokens")
    private Long codeExplanationTokens=0L;

    @ApiModelProperty(value = "文档编写字符数")
    @TableField("dcoumention_writing_chars")
    private Long dcoumentionWritingChars=0L;

    @ApiModelProperty(value = "文档编写token数")
    @TableField("dcoumention_writing_tokens")
    private Long dcoumentionWritingTokens=0L;

    @ApiModelProperty(value = "代码重构字符数")
    @TableField("code_refactoring_chars")
    private Long codeRefactoringChars=0L;

    @ApiModelProperty(value = "代码重构token数")
    @TableField("code_refactoring_tokens")
    private Long codeRefactoringTokens=0L;

    @ApiModelProperty(value = "代码插入字符数")
    @TableField("qucik_code_insertion_chars")
    private Long qucikCodeInsertionChars=0L;

    @ApiModelProperty(value = "代码插入token数")
    @TableField("qucik_code_insertion_tokens")
    private Long qucikCodeInsertionTokens=0L;

    @ApiModelProperty(value = "编辑器使用时间")
    @TableField("editor_usage_time")
    private Long editorUsageTime=0L;

    @ApiModelProperty(value = "编辑器活跃时间")
    @TableField("editor_active_time")
    private Long editorActiveTime=0L;

    @ApiModelProperty(value = "添加的代码行数")
    @TableField("added_code_lines")
    private Long addedCodeLines=0L;

    @ApiModelProperty(value = "删除的代码行数")
    @TableField("deleted_code_lines")
    private Long deletedCodeLines=0L;

    @ApiModelProperty(value = "总键盘输入次数")
    @TableField("total_keyboard_inputs")
    private Long totalKeyboardInputs=0L;

    @ApiModelProperty(value = "CTRL+C次数")
    @TableField("ctrl_c_count")
    private Long ctrlCCount=0L;

    @ApiModelProperty(value = "CTRL+V次数")
    @TableField("ctrl_v_count")
    private Long ctrlVCount=0L;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime=new Date();

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime=new Date();
    

}
