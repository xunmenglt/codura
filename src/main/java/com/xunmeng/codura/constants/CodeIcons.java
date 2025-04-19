package com.xunmeng.codura.constants;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.AnimatedIcon;
import kotlin.jvm.JvmField;

import javax.swing.*;

public class CodeIcons {
    @JvmField
    public static Icon STORY = IconLoader.getIcon("/icons/story.svg", CodeIcons.class);

    @JvmField
    public static Icon AI_COPILOT = IconLoader.getIcon("/icons/logo.svg", CodeIcons.class);
    @JvmField
    public static Icon TOOL_LOGO = IconLoader.getIcon("/icons/logo_16_16.svg", CodeIcons.class);
    @JvmField
    public static Icon LOGO_13_13 = IconLoader.getIcon("/icons/logo_13_13.svg", CodeIcons.class);
    @JvmField
    public static Icon DARK = IconLoader.getIcon("/icons/logo_dark.svg", CodeIcons.class);

    @JvmField
    public static Icon ERROR = IconLoader.getIcon("/icons/logo_error.svg", CodeIcons.class);

    @JvmField
    public static Icon AI_PAIR = IconLoader.getIcon("/icons/logo_pair.svg", CodeIcons.class);


    @JvmField
    public static Icon IntProgress  = new AnimatedIcon.Default();
}
