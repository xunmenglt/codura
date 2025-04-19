package com.xunmeng.system;


import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.common.utils.uuid.Seq;
import com.xunmeng.common.utils.uuid.UUID;
import com.xunmeng.system.pojo.SysNotice;
import com.xunmeng.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/system/notice")
@Api(tags = "通知管理接口")
public class SysNoticeController extends BaseController {
    
    
    @Autowired
    private ISysNoticeService noticeService;
    
    @ApiOperation("获取通知列表")
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice){
        startPage();
        List<SysNotice> list=noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    @ApiOperation("新增通知")
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysNotice notice){
        long noticeId=new Date().getTime();
        notice.setNoticeId(noticeId);
        notice.setCreateBy(getUsername());
        notice.setUpdateBy(getUsername());
        return noticeService.save(notice)? AjaxResult.success(noticeId):AjaxResult.error();
    }

    @ApiOperation("修改通知")
    @Log(title = "通知管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody SysNotice notice){
        notice.setUpdateBy(getUsername());
        notice.setUpdateTime(new Date());
        return toAjax(noticeService.updateById(notice));
    }

    @ApiOperation("获取通知信息")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping("/{noticeId}")
    public AjaxResult query(@PathVariable Long noticeId){
        return AjaxResult.success(noticeService.getById(noticeId));
    }


    @ApiOperation("删除通知")
    @Log(title = "通知管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:notice:delete')")
    @DeleteMapping("/delete/{noticeIds}")
    public AjaxResult delete(@PathVariable Long[] noticeIds){
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
