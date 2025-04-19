package com.xunmeng.codura.setting.provider;

import com.xunmeng.codura.system.pojo.User;
import com.xunmeng.codura.utils.CodeBundle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoProvider implements Provider{
    private User user;

    //代码补全字符数
    private Long codeCompletionChars=0L;

    // 代码补全token数
    private Long codeCompletionTokens=0L;

    // 代码问答 次数
    private Long codeCompletionQaTimes=0L;

    // 代码问答qa字符数
    private Long codeCompletionQaChars=0L;

    // 代码问答 token数
    private Long codeCompletionQaTokens=0L;

    // 测试用例编写字符数
    private Long testCaseWritingChars=0L;

    // 测试用例编写token数
    private Long testCaseWritingTokens=0L;

    // 变量类型声明字符数
    private Long variableTypeDeclarationChars=0L;

    // 变量类型声明token数
    private Long variableTypeDeclarationTokens=0L;

    // 代码解释字符数
    private Long codeExplanationChars=0L;

    // 代码解释token数
    private Long codeExplanationTokens=0L;

    // 文档编写字符数
    private Long dcoumentionWritingChars=0L;

    // 文档编写token数
    private Long dcoumentionWritingTokens=0L;

    // 代码重构字符数
    private Long codeRefactoringChars=0L;

    // 代码重构token数
    private Long codeRefactoringTokens=0L;

    //代码插入字符数
    private Long qucikCodeInsertionChars=0L;

    // 代码插入token数
    private Long qucikCodeInsertionTokens=0L;

    private Long editorUsageTime=0L;
    private Long editorActiveTime=0L;
    private Long addedCodeLines=0L;
    private Long deletedCodeLines=0L;
    private Long totalKeyboardInputs=0L;
    private Long ctrlCCount=0L;
    private Long ctrlVCount=0L;
    

    public static UserInfoProvider getDefaultProvider() {
        User defaultUser = User.getDefaultUser();
        UserInfoProvider userInfoProvider = new UserInfoProvider();
        userInfoProvider.user=defaultUser;
        return userInfoProvider;
    }


    @Override
    public String ID() {
        return CodeBundle.message("code.configurable.user.provider.name");
    }
}
