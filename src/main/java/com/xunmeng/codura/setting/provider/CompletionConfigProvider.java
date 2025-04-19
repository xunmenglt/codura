package com.xunmeng.codura.setting.provider;

import com.xunmeng.codura.constants.enums.ComponentType;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 补全配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CompletionConfigProvider implements Provider{
    @UIComponent(label = "最大生成文本行数",componentType = ComponentType.JTEXTFILED)
    private Integer numLineContext;
    
    @UIComponent(label = "最大生成token数",componentType = ComponentType.JTEXTFILED)
    private Integer numPredictFim;
    
    @UIComponent(label = "温度（temperature）",componentType = ComponentType.JTEXTFILED)
    private Double temperature=0.75;
    
    @UIComponent(label = "开启文件上下文",componentType = ComponentType.JCHECKBOX)
    private boolean fileContextEnabled=false;

    @UIComponent(label = "开启连续补全",componentType = ComponentType.JCHECKBOX)
    private boolean enableSubsequentCompletions=false;

    @UIComponent(label = "开启补全缓存",componentType = ComponentType.JCHECKBOX)
    private boolean completionCacheEnabled=false;

    @UIComponent(label = "开启多行补全",componentType = ComponentType.JCHECKBOX)
    private boolean multilineCompletionsEnabled=true;

    @UIComponent(label = "开启补全",componentType = ComponentType.JCHECKBOX)
    private boolean enabled;

    public static CompletionConfigProvider defaultProvider() {
        CompletionConfigProvider provider = new CompletionConfigProvider();
        provider.setNumLineContext(100);
        provider.setNumPredictFim(512);
        provider.setTemperature(0.75);
        provider.setFileContextEnabled(false);
        provider.setEnableSubsequentCompletions(false);
        provider.setCompletionCacheEnabled(false);
        provider.setMultilineCompletionsEnabled(true);
        provider.setEnabled(true);
        return provider;
    }
    
    
    @Override
    public String ID() {
        return "";
    }
}
