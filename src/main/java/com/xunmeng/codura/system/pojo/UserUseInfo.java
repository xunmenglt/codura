package com.xunmeng.codura.system.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class UserUseInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonProperty(value = "id")
    private Integer id;
    @JsonProperty(value = "userId")
    private String userId;
    @JsonProperty(value = "codeCompletionChars")
    private Long codeCompletionChars=0L;
    @JsonProperty(value = "codeCompletionTokens")
    private Long codeCompletionTokens=0L;
    @JsonProperty(value = "codeCompletionQaTimes")
    private Long codeCompletionQaTimes=0L;
    @JsonProperty(value = "codeCompletionQaChars")
    private Long codeCompletionQaChars=0L;
    @JsonProperty(value = "codeCompletionQaTokens")
    private Long codeCompletionQaTokens=0L;
    @JsonProperty(value = "testCaseWritingChars")
    private Long testCaseWritingChars=0L;
    @JsonProperty(value = "testCaseWritingTokens")
    private Long testCaseWritingTokens=0L;
    @JsonProperty(value = "variableTypeDeclarationChars")
    private Long variableTypeDeclarationChars=0L;
    @JsonProperty(value = "variableTypeDeclarationTokens")
    private Long variableTypeDeclarationTokens=0L;
    @JsonProperty(value = "codeExplanationChars")
    private Long codeExplanationChars=0L;
    @JsonProperty(value = "codeExplanationTokens")
    private Long codeExplanationTokens=0L;
    @JsonProperty(value = "dcoumentionWritingChars")
    private Long dcoumentionWritingChars=0L;
    @JsonProperty(value = "dcoumentionWritingTokens")
    private Long dcoumentionWritingTokens=0L;
    @JsonProperty(value = "codeRefactoringChars")
    private Long codeRefactoringChars=0L;
    @JsonProperty(value = "codeRefactoringTokens")
    private Long codeRefactoringTokens=0L;
    @JsonProperty(value = "qucikCodeInsertionChars")
    private Long qucikCodeInsertionChars=0L;
    @JsonProperty(value = "qucikCodeInsertionTokens")
    private Long qucikCodeInsertionTokens=0L;
    @JsonProperty(value = "editorUsageTime")
    private Long editorUsageTime=0L;
    @JsonProperty(value = "editorActiveTime")
    private Long editorActiveTime=0L;
    @JsonProperty(value = "addedCodeLines")
    private Long addedCodeLines=0L;
    @JsonProperty(value = "deletedCodeLines")
    private Long deletedCodeLines=0L;

    @JsonProperty(value = "totalKeyboardInputs")
    private Long totalKeyboardInputs=0L;

    @JsonProperty(value = "ctrlCCount")
    private Long ctrlCCount=0L;
    @JsonProperty(value = "ctrlVCount")
    private Long ctrlVCount=0L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime=new Date();

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime=new Date();
    

}
