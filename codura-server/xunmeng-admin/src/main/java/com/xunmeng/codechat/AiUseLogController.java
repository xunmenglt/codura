package com.xunmeng.codechat;


import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.system.pojo.AiUseLog;
import com.xunmeng.system.service.IAiUseLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiuTeng
 * @since 2024-11-07
 */
@RestController
@RequestMapping("/ai-use-log")
@Api(tags = "ai使用日志控制接口")
public class AiUseLogController extends BaseController {
    
    @Autowired
    private IAiUseLogService useLogService;
    
    @PostMapping("/add")
    @ApiOperation(value = "新增日志信息")
//    @PreAuthorize("@ss.hasRole('programmer')")
    public AjaxResult add(@RequestBody AiUseLog aiUseLog){
        aiUseLog.setUserId(getUserId());
        boolean flag= useLogService.addAiUseLog(aiUseLog);
        return toAjax(flag);
    }
}
