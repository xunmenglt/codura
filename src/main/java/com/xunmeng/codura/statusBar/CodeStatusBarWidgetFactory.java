package com.xunmeng.codura.statusBar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import com.xunmeng.codura.utils.CodeBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/*用于构建Widget组件*/
public class CodeStatusBarWidgetFactory implements StatusBarWidgetFactory {

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "CodeAlien";
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return CodeBundle.message("code.statusbar.name");
    }


    @Override
    public StatusBarWidget createWidget(Project project){
        return new CodeStatusBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        widget.dispose();
    }
}
