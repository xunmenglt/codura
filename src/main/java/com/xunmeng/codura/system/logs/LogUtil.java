package com.xunmeng.codura.system.logs;

import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.system.SecurityUtil;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import com.xunmeng.codura.system.logs.constans.LogType;
import com.xunmeng.codura.system.logs.pojo.*;
import com.xunmeng.codura.system.utils.SystemInfoUtil;
import com.xunmeng.codura.utils.AESUtil;
import com.xunmeng.codura.utils.UUIDUtil;

import java.util.List;

public abstract class LogUtil {
    private static String getNextLogId(){
        return UUIDUtil.generateUUID();
    }
    private static String getUserID(){
        // todo 获取用户名
        return SecurityUtil.userId();
    }
    public static BaseLog createDefaultInfoLog(){
        BaseLog log = new BaseLog();
        log.setId(getNextLogId());
        log.setType(LogType.INFO);
        log.setUserId(getUserID());
        log.setIdeVersion(SystemInfoUtil.ideVersion());
        log.setTimestamp(System.currentTimeMillis());
        log.setIpAddress(SystemInfoUtil.ipAddress());
        return log;
    }
    public static BaseLog createDefaultErrorLog(){
        BaseLog log = new BaseLog();
        log.setId(getNextLogId());
        log.setType(LogType.INFO);
        log.setUserId(getUserID());
        log.setIdeVersion(SystemInfoUtil.ideVersion());
        log.setTimestamp(System.currentTimeMillis());
        log.setIpAddress(SystemInfoUtil.ipAddress());
        return log;
    }

    public static BaseLog createDefaultWarningLog(){
        BaseLog log = new BaseLog();
        log.setId(getNextLogId());
        log.setType(LogType.INFO);
        log.setUserId(getUserID());
        log.setIdeVersion(SystemInfoUtil.ideVersion());
        log.setTimestamp(System.currentTimeMillis());
        log.setIpAddress(SystemInfoUtil.ipAddress());
        return log;
    }
    public static BaseLog createDefaultBaseLog(){
        BaseLog log = new BaseLog();
        log.setId(getNextLogId());
        log.setType(LogType.INFO);
        log.setUserId(getUserID());
        log.setIdeVersion(SystemInfoUtil.ideVersion());
        log.setTimestamp(System.currentTimeMillis());
        log.setIpAddress(SystemInfoUtil.ipAddress());
        return log;
    }

    public static AIUsageLog createDefaultAiUseLog(){
        AIUsageLog log = new AIUsageLog();
        log.setId(getNextLogId());
        log.setType(LogType.INFO);
        log.setUserId(getUserID());
        log.setIdeVersion(SystemInfoUtil.ideVersion());
        log.setTimestamp(System.currentTimeMillis());
        log.setIpAddress(SystemInfoUtil.ipAddress());
        return log;
    }

    public static AIUsageLog createAiUseLog(AIUsageType eventType, List<ConversationMessage> inputContent, String outputContent){
        AIUsageLog log = createDefaultAiUseLog();
        log.setEventType(eventType);
        log.setInputContent(inputContent);
        log.setOutputContent(outputContent);
        return log;
    }
    
    
    public static String encryptContent(String content){
        if (content!=null && !content.equals("")){
            try {
                String result = AESUtil.encrypt(content);
                return result;
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    } 
    
}
