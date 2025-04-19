package com.xunmeng.system.service;

import com.xunmeng.system.pojo.UserUseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
public interface IUserUseInfoService extends IService<UserUseInfo> {

    UserUseInfo getUserUseInfoByUserId(String userId);

    UserUseInfo getTodayUserUseInfoByUserId(String userId);
    

    UserUseInfo getAllUserUseInfo();

    List<UserUseInfo> getWeekUserUseInfoByUserId(String userId);

    boolean updateUserUseEditorUseInfo(UserUseInfo useInfo);

    Integer getTotalUsersNum();

    Integer getTotalAiUseTimes();

    Integer getTotalCodeHelpCount();

    Long getTotalPlugInUseTime();

    Long getTotalPlugInActivateUseTime();

    List<UserUseInfo> getDayUsePlugInStatusInMonth();

    UserUseInfo getPlugInUsagePercentage();

    List<UserUseInfo> getActiveUserLeaderboard(String startTime, String endTime);
}
