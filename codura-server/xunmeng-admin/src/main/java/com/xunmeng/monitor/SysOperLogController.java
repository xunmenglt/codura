package com.xunmeng.monitor;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.common.utils.DataUtils;
import com.xunmeng.common.utils.excel.ExcelUtils;
import com.xunmeng.system.pojo.SysOperLog;
import com.xunmeng.system.service.ISysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/monitor/operlog")
@Api(tags = "操作日志管理接口")
public class SysOperLogController extends BaseController {

    @Autowired
    private ISysOperLogService operLogService;
    
    @ApiOperation("获取操作日志列表")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog){
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }
    
    

    
    @ApiOperation("删除操作日志")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:delete')")
    @DeleteMapping("/delete/{operIds}")
    public AjaxResult delete(@PathVariable Long[] operIds){
        return toAjax(operLogService.removeByIds(Arrays.asList(operIds)));
    }

    
    
    
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @ApiOperation("清空操作日志")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:delete')")
    @DeleteMapping("/clear")
    public AjaxResult clear(){
        operLogService.clearOperLogs();
        return AjaxResult.success();
    }

    
    @ApiOperation("操作日志导出")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog){
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtils<SysOperLog> excelUtils = new ExcelUtils<>(SysOperLog.class, list);
        excelUtils.exportExcel(response, "操作日志"+DataUtils.getTime());
    }
    
}
