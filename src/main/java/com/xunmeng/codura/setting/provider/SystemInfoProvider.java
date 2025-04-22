package com.xunmeng.codura.setting.provider;

import com.xunmeng.codura.constants.enums.ComponentType;
import com.xunmeng.codura.constants.enums.ProtocolType;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import com.xunmeng.codura.utils.CodeBundle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemInfoProvider implements Provider{

    @UIComponent(label = "后管平台主机地址",componentType = ComponentType.JTEXTFILED,order = 1)
    private String host;
    
    @UIComponent(label = "服务协议",componentType = ComponentType.JRADIOBUTTON,tabs = ProtocolType.class,order = 2)
    private ProtocolType protocol;

    @UIComponent(label = "服务端口",componentType = ComponentType.JTEXTFILED,order = 3)
    private Integer port;

    @UIComponent(label = "基础路径",componentType = ComponentType.JTEXTFILED,order = 4)
    private String base;


    public static SystemInfoProvider getDefaultProvider() {
        SystemInfoProvider systemInfoProvider = new SystemInfoProvider();
        systemInfoProvider.base="/app";
        systemInfoProvider.host="175.178.58.161";
        systemInfoProvider.port=80;
        systemInfoProvider.protocol=ProtocolType.HTTP;
        return systemInfoProvider;
    }
    
    public String getRequestBaseUrl(){
        String url = "%s://%s:%s%s".formatted(protocol.V(), host, port, StringUtils.isEmpty(base)?"":base);
        return url;
    }

    @Override
    public String ID() {
        return CodeBundle.message("code.configurable.system.provider.name");
    }
}
