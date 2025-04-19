package com.xunmeng.codura.status;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Pair;
import com.xunmeng.codura.statusBar.CodeStatusBarWidget;
import kotlin.jvm.JvmOverloads;

import java.util.Arrays;

@Service
public final class CodeStatusService implements CodeStatusListener, Disposable {

    /*同步锁*/
    private Object lock=new Object();

    /*CodeAlien图标的起始状态*/
    private CodeStatus codeStatus = CodeStatus.Ready;
    
    /*CodeAlien图标的起始提示信息*/
    private String message=null;
    
    public CodeStatusService(){
        /*注册监听事件，将CodeAlienStatusListener注册到MessageBus中，并与指定TOPIC绑定*/
        ApplicationManager.getApplication().getMessageBus()
                .connect(this)
                .subscribe(CodeStatusListener.TOPIC, this);
    }


    /*通知函数，用于其他函数动态发送TOPIC事件，一般用于监听状态呢栏变化，然后更新图标*/
    @JvmOverloads
    public static void notifyApplication(CodeStatus status, String customMessage) {
        ApplicationManager.getApplication().getMessageBus()
                .syncPublisher(CodeStatusListener.TOPIC)
                .onCodeAlienStatus(status, customMessage);
    }

    public Pair<CodeStatus, String> getStatus() {
        synchronized(lock) { return Pair.create(codeStatus, message);}
    }
    
    

    /*监听CodeAlien状态变化*/
    @Override
    public void onCodeAlienStatus(CodeStatus status, String customMessage) {
        synchronized(lock) {
            this.codeStatus = status;
            message =customMessage;
        }
        updateAllStatusBarIcons();
    }
    
    /*更新wdiget状态*/
    public void updateAllStatusBarIcons() {
        Runnable action = new Runnable() {
            @Override
            public void run() {
                Arrays.stream(ProjectManager.getInstance().getOpenProjects()).filter((e)->!e.isDisposed()).forEach(e->{
                    CodeStatusBarWidget.update(e);
                });
            }
        };

        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread()) {
            action.run();
        } else {
            application.invokeLater(action);
        }
    }

    /*获取到当前状态*/
    public static Pair<CodeStatus, String> getCurrentStatus(){
        return ApplicationManager.getApplication().getService(CodeStatusService.class).getStatus();
    }
    
    
    
    @Override
    public void dispose() {}

    public CodeStatus getCodeAlienStatus() {
        return codeStatus;
    }

    public void setCodeAlienStatus(CodeStatus codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
