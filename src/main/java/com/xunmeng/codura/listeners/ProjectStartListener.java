package com.xunmeng.codura.listeners;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.setting.state.TimerMasterStateService;
import com.xunmeng.codura.system.apis.UserUseInfoAPI;
import com.xunmeng.codura.system.pojo.UserUseInfo;
import com.xunmeng.codura.utils.JsonUtils;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProjectStartListener implements StartupActivity, Disposable {

    private TimerMasterStateService state = TimerMasterStateService.settings();

    private ScheduledExecutorService scheduler = AppExecutorUtil.createBoundedScheduledExecutorService("ProjectStartListener", 1);
    private ScheduledFuture<?> future = null;
    private volatile boolean running = true;


    @Override
    public void runActivity(@NotNull Project project) {
        String projectPath = project.getLocationHash();
        Project firstOpenProject = Arrays.stream(ProjectManager.getInstance().getOpenProjects())
                .filter(p -> p.getLocationHash().equals(state.getRunProjectPath()))
                .findFirst()
                .orElse(null);
        if (state.getRunProjectPath().isEmpty() || state.getRunProjectPath().equals(projectPath) || firstOpenProject == null) {
            // 从服务器获取用户信息
            UserUseInfo todayUseInfo = getTodayUseInfo();
            List<UserUseInfo> weekUseInfo = getWeekUseInfo();
            state.setCurrentUserUseInfo(todayUseInfo);
            state.setHistoryUserUseInfos(weekUseInfo);
            state.setRunProjectPath(projectPath);
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
            }
            future = scheduler.schedule(() -> {
                while (running) {
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(state.getUpdateInterval()));

                        // 避免多个项目运行时间统计多次
                        boolean isActive = (System.currentTimeMillis() - state.getActiveTime()) / 1000 <= state.getActiveInterval();

                        state.setRunProjectPath(projectPath);

                        UserUseInfo currentUserUseInfo = state.initAndGetUserUseInfo();
                        
                        currentUserUseInfo.setEditorUsageTime(currentUserUseInfo.getEditorUsageTime() + state.getUpdateInterval());

                        if (isActive) {
                            currentUserUseInfo.setEditorActiveTime(currentUserUseInfo.getEditorActiveTime() + state.getUpdateInterval());
                            state.setActiveTime(0L);
                        }
                        
                        // 每间隔10分钟保存一下用户信息
                        if (currentUserUseInfo.getEditorUsageTime()>0 && currentUserUseInfo.getEditorUsageTime()%state.getSaveTime()==0){
                            // 判断用户是否登录
                            String id = SystemInfoStateService.settings().getUserInfoProvider().getUser().getId();
                            if (!StringUtils.isEmpty(id))currentUserUseInfo.setUserId(id);
                            UserUseInfoAPI.updateUserEditorUseInfo(currentUserUseInfo);
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, TimeUnit.SECONDS);
        }
    }

    
    private UserUseInfo getTodayUseInfo(){
        Future<Response> future = UserUseInfoAPI.todayUseInfo();
        UserUseInfo userUseInfo = state.initAndGetUserUseInfo();
        if (future==null) return userUseInfo;
        try {
            Response response = future.get(10, TimeUnit.SECONDS);
            if (response.code()==200){
                String body = response.body().string();

                if (body == null) return userUseInfo;
                JsonNode jsonNode = JsonUtils.convert2Object(body, JsonNode.class);
                int code = jsonNode.get("code").asInt();
                if (code!=200) return userUseInfo;
                JsonNode data = jsonNode.get("data");
                if (data==null) return userUseInfo;
                UserUseInfo currentUserUseInfo = JsonUtils.tree2Object(data, UserUseInfo.class);
                return currentUserUseInfo;
            }
        } catch (Exception e) {
            return userUseInfo;
        }
        return userUseInfo;
    }

    private List<UserUseInfo> getWeekUseInfo(){
        Future<Response> future = UserUseInfoAPI.weekUseInfo();
        state.initAndGetUserUseInfo();
        List<UserUseInfo> historyUserUseInfos = state.getHistoryUserUseInfos();
        if (future==null) return historyUserUseInfos;
        try {
            Response response = future.get(10, TimeUnit.SECONDS);
            if (response.code()==200){
                String body = response.body().string();

                if (body == null) return historyUserUseInfos;
                JsonNode jsonNode = JsonUtils.convert2Object(body, JsonNode.class);
                int code = jsonNode.get("code").asInt();
                if (code!=200) return historyUserUseInfos;
                JsonNode data = jsonNode.get("data");
                if (data==null) return historyUserUseInfos;
                UserUseInfo[] weekUseInfo = JsonUtils.tree2Object(data, UserUseInfo[].class);
                return Arrays.asList(weekUseInfo);
            }
        } catch (Exception e) {
            return historyUserUseInfos;
        }
        return historyUserUseInfos;
    }
    

    @Override
    public void dispose() {

    }
}
