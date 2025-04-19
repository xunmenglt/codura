package com.xunmeng.codura.utils;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.xunmeng.codura.constants.Languages;
import com.xunmeng.codura.pojo.PrefixSuffix;
import com.xunmeng.codura.toolwin.sharedchat.events.eidtor.Snippet;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xunmeng.codura.constants.Constants.*;

public class CompletionUtils {


    public static PrefixSuffix getPrefixSuffix(int numLineContext, Document document, CaretModel caretModel) {
        double contextRatioPrefix = 0.85;
        double contextRatioSuffix = 0.15;
        return getPrefixSuffix(numLineContext,document,caretModel,contextRatioPrefix,contextRatioSuffix);
    }
    
    public static PrefixSuffix getPrefixSuffix(int numLines, Document document, CaretModel caretModel,double contextRatioPrefix,double contextRatioSuffix) {
        int offset=caretModel.getOffset();
        int currentLine=document.getLineNumber(offset);
        int numLinesToEnd = document.getLineCount() - currentLine;
        int numLinesPrefix = Double.valueOf(Math.floor(Math.abs(numLines * contextRatioPrefix))).intValue();
        int numLinesSuffix = Double.valueOf(Math.ceil(Math.abs(numLines * contextRatioSuffix))).intValue();
        if (numLinesPrefix > currentLine) {
            numLinesSuffix += numLinesPrefix - currentLine;
            numLinesPrefix = currentLine;
        }
        if (numLinesSuffix > numLinesToEnd) {
            numLinesPrefix += numLinesSuffix - numLinesToEnd;
            numLinesSuffix = numLinesToEnd;
        }
        TextRange prefixRange = new TextRange(
                document.getLineStartOffset(Math.max(0, currentLine - numLinesPrefix)),
                offset
        );
        TextRange suffixRange = new TextRange(
                offset,
                document.getLineEndOffset(currentLine + numLinesSuffix-1)
        );
        
        String prefix=document.getText(prefixRange);
        String suffix=document.getText(suffixRange);
        
        return new PrefixSuffix(prefix,suffix);
    }


    //确定在给定的上下文和配置下是否应该跳过内联代码补全。
    public static boolean getShouldSkipCompletion(Document document, Editor editor, boolean autoSuggestEnabled) {
        if (editor == null) return true;
        // 获取当前鼠标位置到当前行末位置
        int startOffset=editor.getCaretModel().getOffset();
        // 获取当前行
        int lineNumber = document.getLineNumber(startOffset);
        // 获取当前行末尾便宜
        int endOffset = document.getLineEndOffset(lineNumber);
        // 获取文本
        String textAfter=document.getText(new TextRange(startOffset,endOffset));
        
        // 获取光标前后字符
        String charBefore = getBeforeAndAfter(editor)[0];
        if (getSkipVariableDeclataion(charBefore, textAfter)) {
            return true;
        }
        return false;
    }
    
    
    // 判断前字符是否为=，且后面的字符不全都是括号
    private static boolean getSkipVariableDeclataion(String charBefore, String textAfter) {
        if (charBefore!=null&&
                SKIP_DECLARATION_SYMBOLS.contains(charBefore)&&
                textAfter.length()>0&&
                (textAfter.charAt(0)!='?' &&
                !getIsOnlyBrackets(textAfter))){
            return true;
        }
        return false;
    }

    private static String[] getBeforeAndAfter(Editor editor){
        String charBefore="";
        String charAfter="";
        if (editor==null){
            return new String[]{charBefore,charAfter};
        }
        // 获取offset
        int offset=editor.getCaretModel().getOffset();
        int currentLineNumber=editor.getDocument().getLineNumber(offset);
        int startOffset=editor.getDocument().getLineStartOffset(currentLineNumber);
        int endOffset=editor.getDocument().getLineEndOffset(currentLineNumber);
        String beforeText = editor.getDocument().getText(new TextRange(startOffset, offset)).trim();
        if (!StringUtils.isEmpty(beforeText)){
            charBefore=String.valueOf(beforeText.charAt(beforeText.length()-1));
        }
        String afterText = editor.getDocument().getText(new TextRange(offset, endOffset)).trim();
        if (!StringUtils.isEmpty(afterText)){
            charAfter=String.valueOf(afterText.charAt(0));;
        }
        return new String[]{charBefore,charAfter};
    }

    private static boolean getIsOnlyBrackets(String str) {
        if (str!=null || str.length()==0) return false;

        String[] chars = str.split("");
        for (String c : chars) {
            if (!getIsBracket(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean getIsBracket(String c) {
        return ALL_BRACKETS.contains(c);
    }

    public static boolean getIsMiddleOfString(Editor editor) {
        String[] beforeAndAfter = getBeforeAndAfter(editor);
        String charBefore= beforeAndAfter[0]; 
        String charAfter  = beforeAndAfter[1];
        Pattern WORD_PATTERN = Pattern.compile("\\w");
        Matcher charBeforeMatcher = WORD_PATTERN.matcher(charBefore);
        Matcher charAfterMatcher = WORD_PATTERN.matcher(charAfter);
        return (!StringUtils.isEmpty(charBefore)) && (!StringUtils.isEmpty(charAfter)) && charBeforeMatcher.find() && charAfterMatcher.find();
    }

    public static ASTNode getNodeAtPosition(Document document, Editor editor) {
        PsiFile psiFile= PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(document);
        ASTNode foundNode=null;
        Set<ASTNode> visitedNodes=new HashSet<>();
        CaretModel caretModel = editor.getCaretModel();
        if (psiFile==null||caretModel==null){
            return null;
        }
        foundNode=searchNode(psiFile.getNode(),visitedNodes,caretModel.getOffset());
        return foundNode;
    }

    private static ASTNode searchNode(ASTNode node, Set<ASTNode> visitedNodes, int offset) {
        ASTNode foundNode=null;
        TextRange textRange = node.getTextRange();
        int startOffset = textRange.getStartOffset();
        int endOffset = textRange.getEndOffset();
        if (startOffset<=offset && endOffset>=offset){
            foundNode=node;
            visitedNodes.add(node);
            for (ASTNode child : node.getChildren(null)) {
                if (!(child.getPsi() instanceof PsiWhiteSpace)){
                    ASTNode astNode = searchNode(child, visitedNodes, offset);
                    if (astNode!=null){
                        foundNode=astNode;
                        break;
                    }
                }
            }
        }
        return foundNode;
    }

 
    public static boolean getIsMultilineCompletion(ASTNode node, PrefixSuffix prefixSuffix, Editor editor) {
        if (node==null) return false;
        boolean isMultilineCompletion=!getHasLineTextBeforeAndAfter(editor)&&
                !isCursorInEmptyString(editor)&&
                MULTILINE_TYPES.contains(node.getElementType().getDebugName().toLowerCase());
        return (isMultilineCompletion || StringUtils.isEmpty(prefixSuffix.getSuffix()));
    }
    
    // 判断鼠标指针是否在引号之内
    private static boolean isCursorInEmptyString(Editor editor) {
        String[] beforeAndAfter = getBeforeAndAfter(editor);
        return QUOTES.contains(beforeAndAfter[0]) && QUOTES.contains(beforeAndAfter[1]);
    }
    
    // 判断字符串指针前后是否有字符
    private static boolean getHasLineTextBeforeAndAfter(Editor editor) {
        String[] beforeAndAfter = getBeforeAndAfter(editor);
        
        return (!StringUtils.isEmpty(beforeAndAfter[0])) && (!StringUtils.isEmpty(beforeAndAfter[1]));
    }

    public static int getLineBreakCount(String completion) {
        if (StringUtils.isEmpty(completion)){
            return 0;
        }
        return completion.split("\n").length;
    }

    public static String getCurrentLineText(CaretModel position, Document document) {
        int offset=position.getOffset();
        int currentLineNumber=document.getLineNumber(offset);
        int startOffset=document.getLineStartOffset(currentLineNumber);
        int endOffset=document.getLineEndOffset(currentLineNumber);
        String line = document.getText(new TextRange(startOffset, endOffset));
        return line;
    }
    
    public static Languages.CodeLanguageDetails getLanguage(Editor editor){
        String languageId = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument()).getLanguage().getID().toLowerCase();
        Languages.CodeLanguageDetails lang = Languages.getLang(languageId);
        return lang;
    }

    public static Languages.CodeLanguageDetails getLanguage(String languageId){
        Languages.CodeLanguageDetails lang = Languages.getLang(languageId);
        return lang;
    }

    private static Language getLanguage(FileEditorManager fm,Project project){
        Editor editor = fm.getSelectedTextEditor();
        Document document = editor.getDocument();
        if (document!=null){
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (psiFile!=null){
                return psiFile.getLanguage();
            }
        }
        return null;
    }
    
    
    // 获取剪贴版内容
    public static Snippet getSnippet(){
        Project project=LastUsedProject.getLastUsedProject();
        Snippet snippet=null;
        if (!project.isDisposed()){
            VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
            if (selectedFiles==null||selectedFiles.length==0) return null;
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            Editor editor = fileEditorManager.getSelectedTextEditor();
            VirtualFile file = fileEditorManager.getSelectedFiles()[0];
            // 获取文件路径
            String path=file.getPath();
            // 获取文件名称
            String name=file.getName();
            // 获取文件语言类型
            Language language=getLanguage(fileEditorManager,project);
            // 获取caretModel
            CaretModel caretModel = editor.getCaretModel();
            // 获取选择范围
            TextRange selection = caretModel.getCurrentCaret().getSelectionRange();
            int startOffset=selection!=null?selection.getStartOffset():0;
            int endOffset=selection!=null?selection.getEndOffset():0;
            TextRange range=new TextRange(startOffset,endOffset);
            String code=editor.getDocument().getText(range);
            if (language == null || code == null) {
                snippet = new Snippet();
            } else {
                String langeuageId=null;
                if (language!=null){
                    langeuageId=language.getID().toLowerCase();
                }
                snippet = new Snippet(langeuageId, code, path, name);
            }
        }
        return snippet;
    }
    
}
