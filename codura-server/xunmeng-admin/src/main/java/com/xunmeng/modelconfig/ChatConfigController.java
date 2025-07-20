package com.xunmeng.modelconfig;

import com.xunmeng.common.annotation.Anonymous;
import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.system.pojo.ChatConfig;
import com.xunmeng.system.pojo.FimConfig;
import com.xunmeng.system.pojo.ModelServiceConfig;
import com.xunmeng.system.service.IChatConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "对话参数配置接口")
@RestController
@RequestMapping("/chatConfig")
public class ChatConfigController extends BaseController {
    @Autowired
    private IChatConfigService configService;

    @ApiOperation("新增配置")
    @Log(title = "模型chat配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ChatConfig config) {
        return toAjax(configService.save(config));
    }
    @ApiOperation("根据用户名获取配置")
    @GetMapping("/user/{userName}")
    public AjaxResult getByUserName(@PathVariable String userName) {
        ChatConfig config = configService.getByUserName(userName);
        return AjaxResult.success(config) ;
    }
    @ApiOperation("获取公共模型配置（匿名访问）")
    @Anonymous
    @GetMapping("/public")
    public AjaxResult getPublicConfig() {
        ChatConfig config = configService.getByUserName("admin");
        return AjaxResult.success(config) ;
    }
    @ApiOperation("修改模型配置")
    @Log(title = "模型服务配置", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult update(@RequestBody ChatConfig config) {
        return toAjax(configService.updateById(config));
    }
}
