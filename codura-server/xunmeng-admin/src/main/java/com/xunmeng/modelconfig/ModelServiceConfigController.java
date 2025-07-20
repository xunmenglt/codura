package com.xunmeng.modelconfig;

import com.xunmeng.common.annotation.Anonymous;
import com.xunmeng.common.annotation.Log;
import com.xunmeng.common.core.controller.BaseController;
import com.xunmeng.common.core.page.TableDataInfo;
import com.xunmeng.common.core.pojo.AjaxResult;
import com.xunmeng.common.enums.BusinessType;
import com.xunmeng.system.pojo.ModelServiceConfig;
import com.xunmeng.system.service.IModelServiceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "模型服务配置接口")
@RestController
@RequestMapping("/modelConfig")
public class ModelServiceConfigController extends BaseController {

    @Autowired
    private IModelServiceConfigService configService;

    @ApiOperation("获取模型配置列表")
    @GetMapping("/list")
    public TableDataInfo list(ModelServiceConfig query) {
        startPage();
        List<ModelServiceConfig> list = configService.list();
        return getDataTable(list);
    }

    @ApiOperation("新增模型配置")
    @Log(title = "模型服务配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ModelServiceConfig config) {
        return toAjax(configService.save(config));
    }

    @ApiOperation("修改模型配置")
    @Log(title = "模型服务配置", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult update(@RequestBody ModelServiceConfig config) {
        return toAjax(configService.updateById(config));
    }

    @ApiOperation("删除模型配置")
    @Log(title = "模型服务配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult delete(@PathVariable List<Long> ids) {
        return toAjax(configService.removeByIds(ids));
    }

    @ApiOperation("根据用户名获取配置")
    @GetMapping("/user/{userName}")
    public AjaxResult getByUserName(@PathVariable String userName) {
        List<ModelServiceConfig> config = configService.getByUserName(userName);
        return config != null ? AjaxResult.success(config) : AjaxResult.error("未找到配置");
    }

    @ApiOperation("根据用户名更新配置")
    @Log(title = "模型服务配置", businessType = BusinessType.UPDATE)
    @PutMapping("/user/update")
    public AjaxResult updateByUserName(@RequestBody ModelServiceConfig config) {
        return toAjax(configService.updateByUserName(config));
    }

    @ApiOperation("获取公共模型配置（匿名访问）")
    @Anonymous
    @GetMapping("/public")
    public AjaxResult getPublicConfig() {
        List<ModelServiceConfig> config = configService.getByUserName("admin");
        return config != null ? AjaxResult.success(config) : AjaxResult.error("未找到公共配置");
    }

}
