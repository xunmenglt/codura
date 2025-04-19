package com.xunmeng.codura.system.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.system.apis.AiUseLogAPI;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import com.xunmeng.codura.system.logs.constans.LogType;
import com.xunmeng.codura.system.logs.pojo.*;
import com.xunmeng.codura.utils.JsonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static com.xunmeng.codura.constants.Constants.WORKER_DIR;

@Service
public final class LogExecutor implements Disposable {
    
    /**
     * 日志文件对象
     */
    private final static String infoLogFile="info.log";
    private final static String warningLogFile="warning.log";
    private final static String errorLogFile="error.log";
    private final static String aiUseLogFile="aiuse.log";

    private BufferedWriter infoWriter;
    private BufferedWriter  warningWriter;
    private BufferedWriter  errorWriter;
    private BufferedWriter  aiUseWriter;

    private String currentDate;
    
    public static final String LOG_BASE_DIR=new File(WORKER_DIR,"logs").getPath();

    private ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("SystemLogScheduler", 6);


    public LogExecutor(){
        currentDate=getCurrentDate();
        infoWriter=createWriter(infoLogFile);
        warningWriter=createWriter(warningLogFile);
        errorWriter=createWriter(errorLogFile);
        aiUseWriter=createWriter(aiUseLogFile);
    }
    
    private BufferedWriter createWriter(String fileName){
        File file= Paths.get(LOG_BASE_DIR,currentDate,fileName).toFile();
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 打开 BufferedWriter
        BufferedWriter writer=null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }
    
    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date()).toString();
    }
    

    
    public static LogExecutor me(){
        return ApplicationManager.getApplication().getService(LogExecutor.class);
    }
    
    
    private void execute(LogTask task){
        scheduler.execute(task);
    }
    
    public void dolog(LogType logType,String logstr){
        execute(()->{
            BufferedWriter writer=getWriterByLogType(logType);
            writeLog(writer,logstr);
        });
    }

    public void dolog(LogType logType,BaseLog log){
        execute(()->{
            BufferedWriter writer=getWriterByLogType(logType);
            writeLog(writer,log);
        });
    }
    
    public void info(String value){
        BaseLog log = LogUtil.createDefaultInfoLog();
        log.setValue(value);
        dolog(LogType.INFO,log);
    }
    public void error(String value){
        BaseLog log = LogUtil.createDefaultErrorLog();
        log.setValue(value);
        dolog(LogType.ERROR,log);
    }
    
    public void warning(String value){
        BaseLog log = LogUtil.createDefaultWarningLog();
        log.setValue(value);
        dolog(LogType.WARNING,log);
    }

    public void aiuse(AIUsageType eventType, List<ConversationMessage> inputContent, String outputContent){
        AIUsageLog log = LogUtil.createAiUseLog(eventType,inputContent,outputContent);
        dolog(LogType.INFO,log);
    }
    
    
    private BufferedWriter getWriterByLogType(LogType logType){
        BufferedWriter writer=null;
        if (logType==LogType.INFO){
            writer=infoWriter;
        } else if (logType==LogType.WARNING) {
            writer=warningWriter;
        } else if (logType==LogType.ERROR) {
            writer=errorWriter;
        }
        return writer;
    }
    private void writeLog(BufferedWriter writer,String logstr){
        if (writer==null){
            return;
        }
        // 检查log是否为json字符串
        try {
            new ObjectMapper().readTree(logstr);
        } catch (JsonProcessingException e) {
            return;
        }
        if (writer!=null){
            try {
                synchronized (writer){
                    String log=LogUtil.encryptContent(logstr);
                    writer.write(log);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException e) {
                return;
            }
        }
    }
    
    private void writeLog(BufferedWriter writer,BaseLog log){
        if (writer==null){
            return;
        }
        String logstr;
        try {
            logstr = JsonUtils.convert2Json(log);
        } catch (JsonProcessingException e) {
            return;
        }
        HttpClient.scheduler.submit(()->{
            AiUseLogAPI.add(log);
        });
        writeLog(writer,logstr);
    }
    
    

    private void close(BufferedWriter writer){
        if (writer != null) {
            try {
                writer.close(); // 关闭写入流
            } catch (IOException e) {
                e.printStackTrace(); // 处理异常
            }
        }
    }
    
    @Override
    public void dispose() {
        close(infoWriter);
        close(warningWriter);
        close(errorWriter);
        close(aiUseWriter);
    }
}
