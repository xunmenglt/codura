package com.xunmeng.codura.editor;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.Strings;
import com.xunmeng.codura.constants.CodeCompletionType;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CodeAlien 模型补全渲染器
 * 将代码渲染到idea界面中。一般以灰色表示
 */
public class CodeDefaultInlayRenderer implements CodeInlayRenderer {

    private final List<String> lines;
    private final String content;

    /*补全类型*/
    private final CodeCompletionType type;

    private Inlay<CodeInlayRenderer> inlay;

    /*预提示代码块宽度*/
    private int cachedWidth;
    /*预提示代码块高度*/
    private int cachedHeight;

    public CodeDefaultInlayRenderer(CodeCompletionType type, List<String> lines) {
        if (type == null) {
            throw new IllegalStateException("type cannot be null!");
        }
        if (lines == null) {
            throw new IllegalStateException("lines cannot be null!");
        }
        // todo add request
        this.lines = CodeDefaultInlayRenderer.replaceLeadingTabs(lines);
        this.cachedWidth = -1;
        this.cachedHeight = -1;
        this.type = type;
        this.content = StringUtils.join(lines, (String) "\n");
    }

    @Override
    public Inlay<CodeInlayRenderer> getInlay() {
        assert (this.inlay != null);
        Inlay<CodeInlayRenderer> inlay = this.inlay;
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        return inlay;
    }

    public void setInlay(Inlay<CodeInlayRenderer> inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        this.inlay = inlay;
    }


    /*获取补全类型*/
    @Override
    public CodeCompletionType getType() {
        CodeCompletionType codeAlienCompletionType = this.type;
        if (codeAlienCompletionType == null) {
            throw new IllegalStateException("copilotCompletionType cannot be null!");
        }
        return codeAlienCompletionType;
    }


    @Override
    public List<String> getContentLines() {
        List<String> list = this.lines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    /*计算文字内容高度*/
    public int calcHeightInPixels(Inlay inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (this.cachedHeight < 0) {
            this.cachedHeight = inlay.getEditor().getLineHeight() * this.lines.size();
            return this.cachedHeight;
        }
        return this.cachedHeight;
    }

    /*计算机文字内容宽度*/
    public int calcWidthInPixels(Inlay inlay) {
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (this.cachedWidth < 0) {
            int width = InlayRendering.calculateWidth(inlay.getEditor(), this.content, this.lines);
            this.cachedWidth = Math.max(1, width);
            return this.cachedWidth;
        }
        return this.cachedWidth;
    }

    public void paint(Inlay inlay, Graphics2D g, Rectangle2D region, TextAttributes textAttributes) {
        Editor editor;
        if (inlay == null) {
            throw new IllegalStateException("inlay cannot be null!");
        }
        if (g == null) {
            throw new IllegalStateException("g cannot be null!");
        }
        if (region == null) {
            throw new IllegalStateException("region cannot be null!");
        }
        if (textAttributes == null) {
            throw new IllegalStateException("textAttributes cannot be null!");
        }
        if ((editor = inlay.getEditor()).isDisposed()) {
            return;
        }
        InlayRendering.renderCodeBlock(editor, this.content, this.lines, g, region,
                getTextColor(editor, textAttributes));
    }


    static List<String> replaceLeadingTabs(List<String> lines) {
        if (lines == null) {
            throw new IllegalStateException("lines cannot be null!");
        }
//        if (request == null) {
//            throw new IllegalStateException("request cannot be null!");
//        }
        return lines.stream().map(line -> {
            /*计算每一行代码中制表符的数量，并将对应的制表符替换成对应数量的空格，如果在java中一个制表符代表4个空格*/
            int tabCount = Strings.countChars((CharSequence) line, (char) '\t', (int) 0, (boolean) true);
            if (tabCount > 0) {
                String tabSpaces = StringUtil.repeatSymbol((char) ' ', (int) (tabCount * 4));
                return tabSpaces + line.substring(tabCount);
            }
            return line;
        }).collect(Collectors.toList());
    }

    /*获取文本颜色*/
    private static Color getTextColor(Editor editor, TextAttributes contextAttributes) {
        Color userColor;
        if (editor == null) {
            throw new IllegalStateException("editor cannot be null!");
        }
        if (contextAttributes == null) {
            throw new IllegalStateException("contextAttributes cannot be null!");
        }
//        if ((userColor = CopilotApplicationSettings.settings().inlayTextColor) != null) {
//            return userColor;
//        }
        TextAttributes attributes = editor.getColorsScheme()
                .getAttributes(DefaultLanguageHighlighterColors.INLAY_TEXT_WITHOUT_BACKGROUND);
        if (attributes == null) {
            attributes = contextAttributes;
        }
        return attributes.getForegroundColor();
    }

    public List<String> getLines() {
        List<String> list = this.lines;
        if (list == null) {
            throw new IllegalStateException("list cannot be null!");
        }
        return list;
    }

    public String getContent() {
        String string = this.content;
        if (string == null) {
            throw new IllegalStateException("string cannot be null!");
        }
        return string;
    }

    public int getCachedWidth() {
        return this.cachedWidth;
    }

    public int getCachedHeight() {
        return this.cachedHeight;
    }

    public void setCachedWidth(int cachedWidth) {
        this.cachedWidth = cachedWidth;
    }

    public void setCachedHeight(int cachedHeight) {
        this.cachedHeight = cachedHeight;
    }
}
