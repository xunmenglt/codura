package com.xunmeng.codura.setting.state;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.xunmeng.codura.system.pojo.UserUseInfo;
import com.xunmeng.codura.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@State(name = "coduraTimerMaster",storages = {@Storage("coduraTimerMasterState.xml")})
public class TimerMasterStateService implements PersistentStateComponent<TimerMasterStateService> {
    // 运行项目路径
    private String runProjectPath = "";

    // 更新间隔,秒
    private Integer updateInterval = 60;

    // 当日统计数据
    private UserUseInfo currentUserUseInfo;

    // 历史统计数据，保留7天
    private List<UserUseInfo> historyUserUseInfos=new ArrayList<>();
    
    // 活跃时间
    private Long activeTime = Long.MAX_VALUE;

    // 定时保存时间,10分钟
    private Long saveTime=600L;

    // 活跃间隔,如果在60*5秒之内操控了编辑器，那么说明此时活跃，并纪录活跃时间。
    private Integer activeInterval = 60*5;
    
    
    @Override
    public TimerMasterStateService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TimerMasterStateService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
    
    
    public static TimerMasterStateService settings() {
        return ApplicationManager.getApplication().getService(TimerMasterStateService.class).getState();
    }
    
    
    // 初始并刷新用户信息
    public UserUseInfo initAndGetUserUseInfo(){
        UserUseInfo currentUserUseInfo = this.getCurrentUserUseInfo();
        if (currentUserUseInfo == null) {
            currentUserUseInfo = new UserUseInfo();
            currentUserUseInfo.setCreateTime(new Date());
            this.setCurrentUserUseInfo(currentUserUseInfo);
        }
        if (!DateUtils.getTodayYmd().equals(DateUtils.getYmd(currentUserUseInfo.getCreateTime()))){
            List<UserUseInfo> historyUserUseInfos = this.getHistoryUserUseInfos();
            if (historyUserUseInfos==null){
                historyUserUseInfos=new ArrayList<>();
                this.setHistoryUserUseInfos(historyUserUseInfos);
            }
            historyUserUseInfos.add(currentUserUseInfo);
            currentUserUseInfo=new UserUseInfo();
            currentUserUseInfo.setCreateTime(new Date());
            this.setCurrentUserUseInfo(currentUserUseInfo);
        }
        this.setActiveTime(DateUtils.getcurrentTimeMillis());
        return currentUserUseInfo;
    }
    
}
