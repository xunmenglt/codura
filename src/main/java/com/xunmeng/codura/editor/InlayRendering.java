package com.xunmeng.codura.editor;
import com.intellij.ide.ui.AntialiasingType;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.impl.FontInfo;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.util.ui.UIUtil;
import com.xunmeng.codura.utils.CodeBundle;
import com.xunmeng.codura.utils.Maps;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;
public final class InlayRendering {
    
    /*键-用于缓存FontMetrics*/
    private static final Key<Map<Font, FontMetrics>> KEY_CACHED_FONTMETRICS = Key
            .create((String) CodeBundle.message("code.editor.font.metrics.key"));

    private InlayRendering() {
    }

    /*计算将要显示的文本行的宽度*/
    static int calculateWidth(Editor editor, String text, List<String> textLines) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        if (textLines == null) {
            throw new IllegalStateException("textLines cannot be null!");
        }
        FontMetrics metrics = InlayRendering.fontMetrics(editor, InlayRendering.getFont(editor, text));
        int maxWidth = 0;
        for (String line : textLines) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(line));
        }
        return maxWidth;
    }

    /*渲染代码块*/
    static void renderCodeBlock(Editor editor, String content, List<String> contentLines, Graphics2D g,
                                Rectangle2D region, Color textColor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (content == null) {
            throw new IllegalStateException("content cannot be null!");
        }
        if (contentLines == null) {
            throw new IllegalStateException("contentLines cannot be null!");
        }
        if (g == null) {
            throw new IllegalStateException("g cannot be null!");
        }
        if (region == null) {
            throw new IllegalStateException("region cannot be null!");
        }
        if (textColor == null) {
            throw new IllegalStateException("textColor cannot be null!");
        }
        if (content.isEmpty() || contentLines.isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        UISettings.setupAntialiasing((Graphics) g2);
        g2.setColor(textColor);
        Font font = InlayRendering.getFont(editor, content);
        g2.setFont(font);
        FontMetrics metrics = InlayRendering.fontMetrics(editor, font);
        int lineHeight = editor.getLineHeight();
        /*计算画文字的基线*/
        double fontBaseline = Math
                .ceil(font.createGlyphVector(metrics.getFontRenderContext(), "Alb").getVisualBounds().getHeight());
        float linePadding = (float) (lineHeight - fontBaseline) / 2.0f;
        g2.translate(region.getX(), region.getY());
        g2.setClip(0, 0, (int) region.getWidth(), (int) region.getHeight());
        /*开始一行一行的绘画（文字）代码*/
        for (String line : contentLines) {
            g2.drawString(line, 0.0f, (float) fontBaseline + linePadding);
            g2.translate(0, lineHeight);
        }
        g2.dispose();
    }

    /*根据font获取FontMetrics*/
    private static FontMetrics fontMetrics(Editor editor, Font font) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (font == null) {
            throw new IllegalStateException("font cannot be null!");
        }
        FontRenderContext editorContext = FontInfo.getFontRenderContext((Component) editor.getContentComponent());
        FontRenderContext context = new FontRenderContext(editorContext.getTransform(),
                AntialiasingType.getKeyForCurrentScope((boolean) false), UISettings.getEditorFractionalMetricsHint());
        Map cachedMap = (Map) KEY_CACHED_FONTMETRICS.get((UserDataHolder) editor, Collections.emptyMap());
        FontMetrics fontMetrics = (FontMetrics) cachedMap.get(font);
        if (fontMetrics == null || !context.equals(fontMetrics.getFontRenderContext())) {
            fontMetrics = FontInfo.getFontMetrics((Font) font, (FontRenderContext) context);
            KEY_CACHED_FONTMETRICS.set((UserDataHolder) editor, Maps.merge(cachedMap, Map.of(font, fontMetrics)));
        }
        return fontMetrics;
    }

    /*获取字体*/
    private static Font getFont(Editor editor, String text) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (text == null) {
            throw new IllegalStateException("text cannot be null!");
        }
        Font font = editor.getColorsScheme().getFont(EditorFontType.PLAIN).deriveFont(2);
        Font fallbackFont = UIUtil.getFontWithFallbackIfNeeded((Font) font, (String) text);
        Font font2 = fallbackFont.deriveFont((float) InlayRendering.fontSize(editor));
        if (font2 == null) {
            throw new IllegalStateException("font2 cannot be null");
        }
        return font2;
    }

    /*获取字体大小，改大小为idea中我们所设置的*/
    private static int fontSize(Editor editor) {
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        return editor.getColorsScheme().getEditorFontSize();
    }
}
        