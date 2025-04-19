package com.xunmeng.codechat;


import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.system.pojo.AiUseLog;
import com.xunmeng.system.pojo.UserUseInfo;
import com.xunmeng.system.service.IUserUseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
@RestController
@RequestMapping("/user-use-info")
@Api(tags = "用户ai使用情况控制接口")
public class UserUseInfoController extends BaseController {

    @Autowired
    private IUserUseInfoService userUseInfoService;
    
    /*根据用户ID获取使用情况信息*/
    @GetMapping("/info")
    @ApiOperation(value = "根据用户ID获取用户使用情况")
//    @PreAuthorize("@ss.hasRole('programmer')")
    public AjaxResult info(){
        String userId = getUserId();
        UserUseInfo useInfo=userUseInfoService.getUserUseInfoByUserId(userId);
        AjaxResult result = AjaxResult.success(useInfo);
        return result;
    }

    /*获取所有用户使用情况信息*/
    @GetMapping("/allinfo")
    @ApiOperation(value = "获取所有用户使用情况信息")
    public AjaxResult allinfo(){
        UserUseInfo allUseInfo=userUseInfoService.getAllUserUseInfo();
        AjaxResult result = AjaxResult.success(allUseInfo);
        return result;
    }
    
    /*获取所有用户使用情况信息*/
    @GetMapping("/todayInfo")
    @ApiOperation(value = "根据用户ID获取用户当天使用情况")
    public AjaxResult todayInfo(){
        String userId = getUserId();
        UserUseInfo useInfo=userUseInfoService.getTodayUserUseInfoByUserId(userId);
        AjaxResult result = AjaxResult.success(useInfo);
        return result;
    }

    /*获取周信息*/
    @GetMapping("/weekInfo")
    @ApiOperation(value = "根据用户ID获取用户周使用情况")
    public AjaxResult weekInfo(){
        String userId = getUserId();
        List<UserUseInfo> useInfoList=userUseInfoService.getWeekUserUseInfoByUserId(userId);
        AjaxResult result = AjaxResult.success(useInfoList);
        return result;
    }
    
    @PostMapping("/updateUserUseEditorUseInfo")
    @ApiOperation(value = "根据用户ID获取用户周使用情况")
    public AjaxResult updateUserUseEditorUseInfo(@RequestBody UserUseInfo useInfo){
        if (!useInfo.getUserId().equals(getUserId())){
            return AjaxResult.error("当前操作信息非本用户所属");
        }
        useInfo.setUserId(getUserId());
        boolean flag=userUseInfoService.updateUserUseEditorUseInfo(useInfo);
        AjaxResult result = toAjax(flag);
        return result;
    }
}
