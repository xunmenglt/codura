package com.xunmeng.codechat;

import com.xunmeng.common.annotation.Anonymous;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.system.pojo.UserUseInfo;
import com.xunmeng.system.service.IUserUseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/index")
@Api(tags = "报表控制接口")
public class IndexController extends BaseController {
    @Autowired
    private IUserUseInfoService userUseInfoService;
    /*总用户数*/
    @GetMapping("/totalUsers")
    @ApiOperation(value = "总用户数")
    @Anonymous()
    public AjaxResult totalUsers(){
        int count =userUseInfoService.getTotalUsersNum();
        AjaxResult result = AjaxResult.success(count);
        return result;
    }
    
    /*AI总使用次数*/
    @GetMapping("/totalAiUseTimes")
    @ApiOperation(value = "AI总使用次数")
    @Anonymous()
    public AjaxResult totalAiUseTimes(){
        int count =userUseInfoService.getTotalAiUseTimes();
        AjaxResult result = AjaxResult.success(count);
        return result;
    }
    
    
    /*总辅助代码量*/
    @GetMapping("/totalCodeHelpCount")
    @ApiOperation(value = "总辅助代码量-总令牌数")
    @Anonymous()
    public AjaxResult totalCodeHelpCount(){
        int count =userUseInfoService.getTotalCodeHelpCount();
        AjaxResult result = AjaxResult.success(count);
        return result;
    }
    /*用户使用总时间*/
    @GetMapping("/totalPlugInUseTime")
    @ApiOperation(value = "用户使用总时间（秒）")
    @Anonymous()
    public AjaxResult totalPlugInUseTime(){
        Long count =userUseInfoService.getTotalPlugInUseTime();
        AjaxResult result = AjaxResult.success(count);
        return result;
    }
    
    /*用户跃总时间*/
    @GetMapping("/totalPlugInActivateUseTime")
    @ApiOperation(value = "用户跃总时间（秒）")
    @Anonymous()
    public AjaxResult totalPlugInActivateUseTime(){
        Long count =userUseInfoService.getTotalPlugInActivateUseTime();
        AjaxResult result = AjaxResult.success(count);
        return result;
    }
    
    
    /*每天活跃情况柱状图(提供一个月的统计)*/
    @GetMapping("/dayUsePlugInStatusInMonth")
    @ApiOperation(value = "每天活跃情况柱状图（秒）")
    @Anonymous()
    public AjaxResult dayUsePlugInStatusInMonth(){
        List<UserUseInfo> dayUsePlugInStatusList =userUseInfoService.getDayUsePlugInStatusInMonth();
        AjaxResult result = AjaxResult.success(dayUsePlugInStatusList);
        return result;
    }
    
    
    @GetMapping("/plugInUsagePercentage")
    @ApiOperation(value = "各类插件功能使用占比")
    @Anonymous()
    public AjaxResult plugInUsagePercentage(){
        UserUseInfo useInfo =userUseInfoService.getPlugInUsagePercentage();
        AjaxResult result = AjaxResult.success(useInfo);
        return result;
    }
    
    
    /*活跃用户排行榜-可按时间区间查询*/
    @GetMapping("/activeUserLeaderboard")
    @ApiOperation(value = "活跃用户排行榜")
    @Anonymous()
    public TableDataInfo activeUserLeaderboard(@RequestParam(value = "startTime",defaultValue = "",required = false) String startTime,
                                               @RequestParam(value = "endTime",defaultValue = "",required = false) String endTime){
        
        // 构建时间间隔查询字段 startTime~endTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.parse(startTime);
        }catch (Exception e){
            startTime=null;
        }
        
        try {
            dateFormat.parse(endTime);
        }catch (Exception e){
            endTime=null;
        }
        
        startPage();
        List<UserUseInfo> useInfos =userUseInfoService.getActiveUserLeaderboard(startTime,endTime);
        TableDataInfo dataTable=getDataTable(useInfos);
        clearPage();
        return dataTable;
    }
}
