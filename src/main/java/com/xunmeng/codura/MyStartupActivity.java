package com.xunmeng.codura;

import com.intellij.openapi.project.Project;
import org.cef.CefApp;
import org.cef.callback.CefCommandLine;
import org.cef.handler.CefAppHandlerAdapter;

public class MyStartupActivity implements com.intellij.openapi.startup.StartupActivity {
    @Override
    public void runActivity(Project project) {
        if (CefApp.getState()==null) {
            CefApp.addAppHandler(new CefAppHandlerAdapter(new String[]{}) {
                @Override
                public void onBeforeCommandLineProcessing(String processType, CefCommandLine commandLine) {
                    commandLine.appendSwitch("ignore-certificate-errors");
                }
            });

            CefApp.getInstance();  // 必须先手动初始化
        }
    }
}
