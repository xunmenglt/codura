package com.xunmeng.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunmeng.common.config.XunMengConfig;
import com.xunmeng.common.constant.AIUsageType;
import com.xunmeng.common.utils.JsonUtils;
import com.xunmeng.system.pojo.AiUseLog;
import com.xunmeng.system.mapper.AiUseLogMapper;
import com.xunmeng.system.pojo.UserUseInfo;
import com.xunmeng.system.service.IAiUseLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmeng.system.service.IUserUseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
@Service
public class AiUseLogServiceImpl extends ServiceImpl<AiUseLogMapper, AiUseLog> implements IAiUseLogService {

    @Autowired
    private AiUseLogMapper aiUseLogMapper;
    @Autowired
    private IUserUseInfoService userUseInfoService;
    
    private TokenizerEngine tokenizerEngine = TokenizerUtil.createEngine();
    
    @Override
    public boolean addAiUseLog(AiUseLog aiUseLog) {
        // 将日志保存到服务器相关目录中
        savelog2Dir(aiUseLog);
        // 解析aiuse日志内容，记录到数据表中
        if (aiUseLog.getEventType()!=null){
            aiUseLog.setInputLen(0L);
            aiUseLog.setInputTokens(0L);
            aiUseLog.setOutputLen(0L);
            aiUseLog.setOutputTokens(0L);
            if (aiUseLog.getInputContent()!=null && aiUseLog.getInputContent().length()>0){
                Long inputContentLen=Long.valueOf(aiUseLog.getInputContent()==null?0:aiUseLog.getInputContent().length());
                aiUseLog.setInputLen(inputContentLen);
                Iterator<Word> words = tokenizerEngine.parse(aiUseLog.getInputContent());
                int size = CollUtil.size(words);
                aiUseLog.setInputTokens(Long.valueOf(size));
            }
            if (aiUseLog.getOutputContent()!=null && aiUseLog.getOutputContent().length()>0){
                Long ouputContentLen=Long.valueOf(aiUseLog.getOutputContent()==null?0:aiUseLog.getOutputContent().length());
                aiUseLog.setOutputLen(ouputContentLen);
                Iterator<Word> words = tokenizerEngine.parse(aiUseLog.getOutputContent());
                int size = CollUtil.size(words);
                aiUseLog.setOutputTokens(Long.valueOf(size));
            }
        }
        aiUseLog.setInputContent(null);
        aiUseLog.setOutputContent(null);
        boolean a = aiUseLogMapper.insert(aiUseLog)>0;
        // 更新相关用户使用情况内容
        boolean b = updateUserUseInfo(aiUseLog);
        if (a&&b){
            return true;
        }
        return false;
    }

    private boolean updateUserUseInfo(AiUseLog aiUseLog) {
        if (aiUseLog.getEventType()==null) return true;
        
        UserUseInfo useInfo = userUseInfoService.getOne(new QueryWrapper<UserUseInfo>().eq("user_id", aiUseLog.getUserId()));

        if (useInfo == null) {
            useInfo = new UserUseInfo();
            useInfo.setUserId(aiUseLog.getUserId());
            userUseInfoService.save(useInfo);
            useInfo = userUseInfoService.getOne(new QueryWrapper<UserUseInfo>().eq("user_id", aiUseLog.getUserId()));
        }

        // 通用字段更新
        Long inputLen = aiUseLog.getInputLen();
        Long outputLen = aiUseLog.getOutputLen();
        Long inputTokens = aiUseLog.getInputTokens();
        Long outputTokens = aiUseLog.getOutputTokens();

        useInfo.setCodeCompletionQaTimes(useInfo.getCodeCompletionQaTimes() + 1);
        useInfo.setCodeCompletionQaChars(useInfo.getCodeCompletionQaChars() + inputLen + outputLen);
        useInfo.setCodeCompletionQaTokens(useInfo.getCodeCompletionQaTokens() + inputTokens + outputTokens);

        // 根据事件类型分别处理
        String eventType = aiUseLog.getEventType();
        if (AIUsageType.CODE_COMPLETION.getValue().equals(eventType)) {
            useInfo.setCodeCompletionChars(useInfo.getCodeCompletionChars() + inputLen + outputLen);
            useInfo.setCodeCompletionTokens(useInfo.getCodeCompletionTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.CODE_QA.getValue().equals(eventType)) {
            // CODE_QA 处理逻辑已经包含在通用字段更新中，无需额外处理
        } else if (AIUsageType.TEST_CASE_WRITING.getValue().equals(eventType)) {
            useInfo.setTestCaseWritingChars(useInfo.getTestCaseWritingChars() + inputLen + outputLen);
            useInfo.setTestCaseWritingTokens(useInfo.getTestCaseWritingTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.VARIABLE_TYPE_DECLARATION.getValue().equals(eventType)) {
            useInfo.setVariableTypeDeclarationChars(useInfo.getVariableTypeDeclarationChars() + inputLen + outputLen);
            useInfo.setVariableTypeDeclarationTokens(useInfo.getVariableTypeDeclarationTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.CODE_EXPLANATION.getValue().equals(eventType)) {
            useInfo.setCodeExplanationChars(useInfo.getCodeExplanationChars() + inputLen + outputLen);
            useInfo.setCodeExplanationTokens(useInfo.getCodeExplanationTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.DOCUMENTATION_WRITING.getValue().equals(eventType)) {
            useInfo.setDcoumentionWritingChars(useInfo.getDcoumentionWritingChars() + inputLen + outputLen);
            useInfo.setDcoumentionWritingTokens(useInfo.getDcoumentionWritingTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.CODE_REFACTORING.getValue().equals(eventType)) {
            useInfo.setCodeRefactoringChars(useInfo.getCodeRefactoringChars() + inputLen + outputLen);
            useInfo.setCodeRefactoringTokens(useInfo.getCodeRefactoringTokens() + inputTokens + outputTokens);
        } else if (AIUsageType.QUICK_CODE_INSERTION.getValue().equals(eventType)) {
            useInfo.setQucikCodeInsertionChars(useInfo.getQucikCodeInsertionChars() + inputLen + outputLen);
            useInfo.setQucikCodeInsertionTokens(useInfo.getCodeCompletionTokens() + inputTokens + outputTokens);
        }

        return userUseInfoService.updateById(useInfo);
    }

    private boolean savelog2Dir(AiUseLog aiUseLog) {
        String log_save_dir= XunMengConfig.getLogsPath();
        File file = new File(log_save_dir);
        if (!file.exists() || !file.isDirectory()){
            file.mkdirs();
        }
        // 获取当前时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDataStr = simpleDateFormat.format(date);
        File save_dir=new File(log_save_dir, aiUseLog.getUserId()+File.separator+currentDataStr);
        if (!save_dir.exists() || !save_dir.isDirectory()){
            save_dir.mkdirs();
        }
        if (aiUseLog.getType()==null){
            aiUseLog.setType("info");
        }
        File save_file = new File(save_dir, aiUseLog.getType() + ".log");

        // 打开 BufferedWriter
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(save_file, true))) {
            if (writer!=null){
                synchronized (save_file.getPath()){
                    writer.write(JsonUtils.convert2Json(aiUseLog));
                    writer.newLine();
                    writer.flush();
                }
            }
        }catch (Exception e){
            return false;
        }
        
        return true;
    }
}
