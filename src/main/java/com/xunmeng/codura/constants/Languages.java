package com.xunmeng.codura.constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.*;

public class Languages {


    public enum CodeLanguage{
        bat,
        c,
        csharp,
        cpp,
        css,
        go,
        html,
        java,
        javascript,
        javascriptreact,
        json,
        jsx,
        kotlin,
        objective_c,
        php,
        python,
        rust,
        sass,
        scss,
        shellscript,
        swift,
        typescript,
        typescriptreact,
        xml,
        yaml,
        lua,
        perl,
        r,
        ruby,
        scala,
        sql,
        typescriptreactnative,
        xaml;
    }
    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    public static class SyntaxComments{
        private String start="";
        private String end="";
    }
    
    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    public static class CodeLanguageDetails{
        private Set<String> fileExtensions;
        private Set<String> filenamePatterns;
        private SyntaxComments syntaxComments;
        private CodeLanguage derivedFrom;
        private String langName;
    }
    
    public static final Map<CodeLanguage,CodeLanguageDetails> supportedLanguages=new HashMap<>();
    
    static {
        supportedLanguages.put(CodeLanguage.bat, new CodeLanguageDetails()
                .setLangName("BAT file")
                .setFileExtensions(new HashSet<>(Arrays.asList(".bat", ".cmd")))
                .setSyntaxComments(new SyntaxComments().setStart("REM").setEnd("")));

        supportedLanguages.put(CodeLanguage.c, new CodeLanguageDetails()
                .setLangName("C")
                .setFileExtensions(new HashSet<>(Arrays.asList(".c", ".h")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.csharp, new CodeLanguageDetails()
                .setLangName("C#")
                .setFileExtensions(new HashSet<>(Arrays.asList(".cs")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.cpp, new CodeLanguageDetails()
                .setLangName("C++")
                .setFileExtensions(new HashSet<>(Arrays.asList(".cpp", ".h")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.css, new CodeLanguageDetails()
                .setLangName("CSS")
                .setFileExtensions(new HashSet<>(Arrays.asList(".css")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.go, new CodeLanguageDetails()
                .setLangName("Go")
                .setFileExtensions(new HashSet<>(Arrays.asList(".go")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.html, new CodeLanguageDetails()
                .setLangName("HTML")
                .setFileExtensions(new HashSet<>(Arrays.asList(".htm", ".html")))
                .setSyntaxComments(new SyntaxComments().setStart("<!--").setEnd("-->")));

        supportedLanguages.put(CodeLanguage.java, new CodeLanguageDetails()
                .setLangName("Java")
                .setFileExtensions(new HashSet<>(Arrays.asList(".java")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.javascript, new CodeLanguageDetails()
                .setLangName("Javascript")
                .setFileExtensions(new HashSet<>(Arrays.asList(".js", ".jsx", ".cjs")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.javascriptreact, new CodeLanguageDetails()
                .setLangName("Javascript JSX")
                .setFileExtensions(new HashSet<>(Arrays.asList(".jsx")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.json, new CodeLanguageDetails()
                .setLangName("JSON")
                .setFileExtensions(new HashSet<>(Arrays.asList(".json", ".jsonl", ".geojson")))
                .setSyntaxComments(new SyntaxComments().setStart("").setEnd("")));

        supportedLanguages.put(CodeLanguage.jsx, new CodeLanguageDetails()
                .setLangName("JSX")
                .setFileExtensions(new HashSet<>(Arrays.asList(".jsx")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.kotlin, new CodeLanguageDetails()
                .setLangName("Kotlin")
                .setFileExtensions(new HashSet<>(Arrays.asList(".kt", ".ktm", ".kts")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.objective_c, new CodeLanguageDetails()
                .setLangName("Objective C")
                .setFileExtensions(new HashSet<>(Arrays.asList(".h", ".m", ".mm")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.php, new CodeLanguageDetails()
                .setLangName("PHP")
                .setFileExtensions(new HashSet<>(Arrays.asList(".aw", ".ctp", ".fcgi", ".inc", ".php", ".php3", ".php4", ".php5", ".phps", ".phpt")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.python, new CodeLanguageDetails()
                .setLangName("Python")
                .setFileExtensions(new HashSet<>(Arrays.asList(".py")))
                .setSyntaxComments(new SyntaxComments().setStart("'''").setEnd("'''")));

        supportedLanguages.put(CodeLanguage.rust, new CodeLanguageDetails()
                .setLangName("Rust")
                .setFileExtensions(new HashSet<>(Arrays.asList(".rs", ".rs.in")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.sass, new CodeLanguageDetails()
                .setLangName("SASS")
                .setFileExtensions(new HashSet<>(Arrays.asList(".sass")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.scss, new CodeLanguageDetails()
                .setLangName("SCSS")
                .setFileExtensions(new HashSet<>(Arrays.asList(".scss")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.shellscript, new CodeLanguageDetails()
                .setLangName("Shell")
                .setFileExtensions(new HashSet<>(Arrays.asList(".bash", ".sh")))
                .setSyntaxComments(new SyntaxComments().setStart("#")));

        supportedLanguages.put(CodeLanguage.swift, new CodeLanguageDetails()
                .setLangName("Swift")
                .setFileExtensions(new HashSet<>(Arrays.asList(".swift")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.typescript, new CodeLanguageDetails()
                .setLangName("Typescript")
                .setFileExtensions(new HashSet<>(Arrays.asList(".ts", ".cts", ".mts")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.typescriptreact, new CodeLanguageDetails()
                .setLangName("Typescript React")
                .setFileExtensions(new HashSet<>(Arrays.asList(".tsx")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/"))
                .setDerivedFrom(CodeLanguage.typescript));

        supportedLanguages.put(CodeLanguage.xml, new CodeLanguageDetails()
                .setLangName("XML")
                .setFileExtensions(new HashSet<>(Arrays.asList(".xml")))
                .setSyntaxComments(new SyntaxComments().setStart("<!--").setEnd("-->")));

        supportedLanguages.put(CodeLanguage.yaml, new CodeLanguageDetails()
                .setLangName("YAML")
                .setFileExtensions(new HashSet<>(Arrays.asList(".yml", ".yaml")))
                .setSyntaxComments(new SyntaxComments().setStart("#")));
        supportedLanguages.put(CodeLanguage.lua, new CodeLanguageDetails().setLangName("Lua")
                .setFileExtensions(new HashSet<>(Arrays.asList(".lua")))
                .setSyntaxComments(new SyntaxComments().setStart("--").setEnd("")));

        supportedLanguages.put(CodeLanguage.perl, new CodeLanguageDetails()
                .setLangName("Perl")
                .setFileExtensions(new HashSet<>(Arrays.asList(".pl", ".pm")))
                .setSyntaxComments(new SyntaxComments().setStart("#")));

        supportedLanguages.put(CodeLanguage.r, new CodeLanguageDetails()
                .setLangName("R")
                .setFileExtensions(new HashSet<>(Arrays.asList(".r", ".R")))
                .setSyntaxComments(new SyntaxComments().setStart("#")));

        supportedLanguages.put(CodeLanguage.ruby, new CodeLanguageDetails()
                .setLangName("Ruby")
                .setFileExtensions(new HashSet<>(Arrays.asList(".rb")))
                .setSyntaxComments(new SyntaxComments().setStart("=begin").setEnd("=end")));

        supportedLanguages.put(CodeLanguage.scala, new CodeLanguageDetails()
                .setLangName("Scala")
                .setFileExtensions(new HashSet<>(Arrays.asList(".scala")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.sql, new CodeLanguageDetails()
                .setLangName("SQL")
                .setFileExtensions(new HashSet<>(Arrays.asList(".sql")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/")));

        supportedLanguages.put(CodeLanguage.typescriptreactnative, new CodeLanguageDetails()
                .setLangName("Typescript React Native")
                .setFileExtensions(new HashSet<>(Arrays.asList(".tsx")))
                .setSyntaxComments(new SyntaxComments().setStart("/*").setEnd("*/"))
                .setDerivedFrom(CodeLanguage.typescript));

        supportedLanguages.put(CodeLanguage.xaml, new CodeLanguageDetails()
                .setLangName("XAML")
                .setFileExtensions(new HashSet<>(Arrays.asList(".xaml")))
                .setSyntaxComments(new SyntaxComments().setStart("<!--").setEnd("-->")));
    }

    public static CodeLanguageDetails getLang(String languageId) {
        CodeLanguage codeLanguage = CodeLanguage.valueOf(languageId.toLowerCase());
        return supportedLanguages.get(codeLanguage);
    }
}
