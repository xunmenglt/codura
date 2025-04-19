package com.xunmeng.codura.editor;

import com.intellij.openapi.editor.InlayProperties;

public class InlayUtils {
    InlayUtils() {
    }

    public static InlayProperties createInlineProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
    }

    public static InlayProperties createAfterLineEndProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
    }

    public static InlayProperties createBlockProperties(int n) {
        return new InlayProperties().relatesToPrecedingText(true).showAbove(false).priority(Integer.MAX_VALUE - n);
	}
}
