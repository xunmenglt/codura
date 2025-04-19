package com.xunmeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunmeng.common.utils.DateUtils;
import com.xunmeng.system.pojo.UserUseInfo;
import com.xunmeng.system.mapper.UserUseInfoMapper;
import com.xunmeng.system.service.IUserUseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
@Service
public class UserUseInfoServiceImpl extends ServiceImpl<UserUseInfoMapper, UserUseInfo> implements IUserUseInfoService {

    @Autowired
    private UserUseInfoMapper userUseInfoMapper;
    @Override
    public UserUseInfo getUserUseInfoByUserId(String userId) {
        UserUseInfo useInfo=userUseInfoMapper.selectUserUseInfoByIdSum(userId);
        if (useInfo==null){
            useInfo=new UserUseInfo();
            useInfo.setUserId(userId);
            useInfo.setCreateTime(new Date());
            useInfo.setUpdateTime(new Date());
            save(useInfo);
            useInfo=userUseInfoMapper.selectUserUseInfoByIdSum(userId);
        }
        return useInfo;
        
    }


    public UserUseInfo getTodayUserUseInfoByUserId(String userId) {
        Date startOfDay = DateUtils.getStartOfDay();
        Date endOfDay = DateUtils.getEndOfDay();
        
        UserUseInfo useInfo = getOne(
                new QueryWrapper<UserUseInfo>()
                        .eq("user_id", userId)
                        .ge("create_time",startOfDay)
                        .le("create_time",endOfDay)
        );
        if (useInfo==null){
            useInfo=new UserUseInfo();
            useInfo.setUserId(userId);
            useInfo.setCreateTime(new Date());
            useInfo.setUpdateTime(new Date());
            save(useInfo);
            useInfo = getOne(new QueryWrapper<UserUseInfo>()
                    .eq("user_id", userId)
                    .ge("create_time",startOfDay)
                    .le("create_time",endOfDay)
            );
        }
        return useInfo;
    }

    @Override
    public UserUseInfo getAllUserUseInfo() {
        UserUseInfo useInfo = userUseInfoMapper.selectUserUseInfoSum();
        return useInfo;
    }

    @Override
    public List<UserUseInfo> getWeekUserUseInfoByUserId(String userId) {

        Date startOfDay = DateUtils.getLastWeekStartOfDay();
        Date endOfDay = DateUtils.getLastWeekEndOfDay();

        List<UserUseInfo> useInfos = list(
                new QueryWrapper<UserUseInfo>()
                        .eq("user_id", userId)
                        .ge("create_time",startOfDay)
                        .le("create_time",endOfDay)
        );
        if (useInfos==null) return new ArrayList<>();
        return useInfos;
    }

    @Override
    public boolean updateUserUseEditorUseInfo(UserUseInfo useInfo) {
        Date startOfDay = DateUtils.getStartOfDay();
        Date endOfDay = DateUtils.getEndOfDay();
        List<UserUseInfo> useInfos = list(
                new QueryWrapper<UserUseInfo>()
                        .eq("user_id", useInfo.getUserId())
                        .ge("create_time",startOfDay)
                        .le("create_time",endOfDay)
        );
        if (useInfos.size()<=0){
            useInfo=new UserUseInfo();
            useInfo.setUserId(useInfo.getUserId());
            useInfo.setCreateTime(new Date());
            useInfo.setUpdateTime(new Date());
            save(useInfo);
        }
        int flag= userUseInfoMapper.updateUserUseEditorUseInfoByUserId(useInfo);
        return flag>=1;
    }

    @Override
    public Integer getTotalUsersNum() {
        Integer count= userUseInfoMapper.selectTotalProgrammerCount();
        if (count==null){
            count=0;
        }
        return count;
    }

    @Override
    public Integer getTotalAiUseTimes() {
        Integer count= userUseInfoMapper.selectTotalAiUseTimes();
        if (count==null){
            count=0;
        }
        return count;
    }

    @Override
    public Integer getTotalCodeHelpCount() {
        Integer count= userUseInfoMapper.selectTotalCodeHelpCount();
        if (count==null){
            count=0;
        }
        return count;
    }

    @Override
    public Long getTotalPlugInUseTime() {
        Long count= userUseInfoMapper.selectTotalPlugInUseTime();
        if (count==null){
            count=0L;
        }
        return count;
    }

    @Override
    public Long getTotalPlugInActivateUseTime() {
        Long count= userUseInfoMapper.selectTotalPlugInActivateUseTime();
        if (count==null){
            count=0L;
        }
        return count;
    }

    @Override
    public List<UserUseInfo> getDayUsePlugInStatusInMonth() {
        return userUseInfoMapper.selectDayUsePlugInStatusInMonth();
    }

    @Override
    public UserUseInfo getPlugInUsagePercentage() {
        return userUseInfoMapper.selectPlugInUsagePercentage();
    }

    @Override
    public List<UserUseInfo> getActiveUserLeaderboard(String startTime, String endTime) {
        return userUseInfoMapper.selectActiveUserLeaderboard(startTime,endTime);
    }
}
