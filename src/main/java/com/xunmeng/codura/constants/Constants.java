package com.xunmeng.codura.constants;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class Constants {

    public static final String APP_NAME="codura";

    public static final String TOOL_WIN_NAME="Codura";

    public static final String THINKING_TEXT = "// AI 正在思考中...";
    public static final String WORKER_DIR= new File(System.getProperty("user.home"),APP_NAME).getPath();
    
    public static final String SYSTEM ="system";
    public static final String USER = "user";
    public static final String ASSISTANT = "assistant";

    
    public static final String HOST="127.0.0.1";
    public static final int PORT=80;
    public static final String FIM_SERVER_PATH="/v1/completions";
    public static final String CHAT_SERVER_PATH ="/v1/chat/completions";
    public static final Set<String> SKIP_DECLARATION_SYMBOLS  = new HashSet<>(Arrays.asList("="));
    public static final Set<String> OPENING_BRACKETS = new HashSet<>(Arrays.asList("[", "{", "("));
    public static final Set<String> CLOSING_BRACKETS =new HashSet<>(Arrays.asList("]", "}", ")"));
    public static final Set<String> OPENING_TAGS = new HashSet<>(Arrays.asList("<"));
    public static final Set<String> CLOSING_TAGS = new HashSet<>(Arrays.asList("</"));
    public static final Set<String> QUOTES = new HashSet<>(Arrays.asList("\"", "'", "`"));
    public static final Set<String> ALL_BRACKETS = new HashSet<>();
    
    public static  final Set<String> MULTI_LINE_DELIMITERS = new HashSet<>(Arrays.asList("\n\n", "\r\n\r\n"));
    
    public static final int MAX_EMPTY_COMPLETION_CHARS=512;
    
    public static final Pattern LINE_BREAK_REGEX=Pattern.compile("\\r?\\n|\\r|\\n");
    
    public static final int MIN_COMPLETION_CHUNKS=2;
    
    public static String CONTENT_AES_KEY="CodeAlien_AES_KEY";

    public static final Set<String> MULTILINE_OUTSIDE = new HashSet<>(
            Arrays.asList(
                    "class_body",
                    "interface_body",
                    "interface",
                    "class",
                    "program",
                    "identifier",
                    "export"
            )
    );

    public static final Set<String> MULTILINE_INSIDE = new HashSet<>(
            Arrays.asList(
                    "body",
                    "export_statement",
                    "formal_parameters",
                    "function_definition",
                    "named_imports",
                    "object_pattern",
                    "object_type",
                    "object",
                    "parenthesized_expression",
                    "statement_block",
                    "block_statement",
                    "code_block"
            )
    );
    public static final Set<String> MULTILINE_TYPES = new HashSet<>();

    static {
        ALL_BRACKETS.addAll(OPENING_BRACKETS);
        ALL_BRACKETS.addAll(CLOSING_BRACKETS);
        MULTILINE_TYPES.addAll(MULTILINE_OUTSIDE);
        MULTILINE_TYPES.addAll(MULTILINE_INSIDE);
    }

    public static int getLineBreakCount(String line){
        if (line==null){
            return 0;
        }
        String[] lines = line.split("\n");
        return lines.length;
    }
    
    
}
