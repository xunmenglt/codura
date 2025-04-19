package com.xunmeng.codura.statusBar;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup;
import com.xunmeng.codura.status.CodeStatus;
import com.xunmeng.codura.status.CodeStatusService;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeStatusBarWidget extends EditorBasedStatusBarPopup {
    private static final String WIDGET_ID= CodeBundle.message("code.widget.id");

    public CodeStatusBarWidget(Project project){
        super(project,false);
    }
    
    @Override
    public @NotNull @NonNls String ID() {
        return WIDGET_ID;
    }

    /*创建小部件实例*/
    @Override
    protected @NotNull StatusBarWidget createInstance(@NotNull Project project) {
        return new CodeStatusBarWidget(project);
    }

    /*创建Popup弹出,用于展示action动作*/
    @Override
    protected @Nullable ListPopup createPopup(@NotNull DataContext context) {
        AnAction anAction = ActionManager.getInstance().getAction(WIDGET_ID);
        if (anAction==null) return null;
        ActionGroup configuredGroup=(ActionGroup) anAction;
        return JBPopupFactory.getInstance().createActionGroupPopup(CodeBundle.message("code.statusbar.popup.title"),
                new DefaultActionGroup(configuredGroup),
                context,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true);
    }

    /*获取小部件状态*/
    @Override
    protected @NotNull WidgetState getWidgetState(@Nullable VirtualFile file) {
        Pair<CodeStatus, String> currentStatus = CodeStatusService.getCurrentStatus();
        CodeStatus codeStatus = currentStatus.first;
        String toolTip= CodeStatusService.getCurrentStatus().second;
        WidgetState widgetState = new WidgetState(toolTip, "", true);
        widgetState.setIcon(codeStatus.getIcon());
        return widgetState;
    }

    /*该方法用于触发更新状态*/
    public static void update (Project project){
        StatusBar bar = WindowManager.getInstance().getStatusBar(project);
        StatusBarWidget barWidget = bar.getWidget(WIDGET_ID);
        if (barWidget==null) return;
        CodeStatusBarWidget codeStatusBarWidget =(CodeStatusBarWidget) barWidget;
        codeStatusBarWidget.update(()->{
            if (codeStatusBarWidget.myStatusBar!=null){
                codeStatusBarWidget.myStatusBar.updateWidget(WIDGET_ID);
            }
        });
    }
}
