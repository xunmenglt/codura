package com.xunmeng.codura.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.xunmeng.codura.constants.Languages;
import com.xunmeng.codura.pojo.PrefixSuffix;
import com.xunmeng.codura.service.DefaultService;
import com.xunmeng.codura.service.TemplateService;
import com.xunmeng.codura.setting.provider.FimModelProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FimTemplateUtils {
    public static final Logger logger=Logger.getInstance(FimTemplateUtils.class);
    public static String getFimPrompt(FimModelProvider provider, String fileInteractionContext, PrefixSuffix prefixSuffix, String header, boolean fileContextEnabled, String documentLanguage) {
        String prefix= prefixSuffix.getPrefix();
        String suffix= prefixSuffix.getSuffix();
        String fileContext=getFileContext(fileContextEnabled,fileInteractionContext,documentLanguage);
        String heading=header;
        String templateName = provider.getFimTemplateType().V().toString();
        Map<String,String> data=new HashMap<>();
        data.put("fileContext",fileContext);
        data.put("heading",heading);
        data.put("prefix",prefix);
        data.put("suffix",suffix);
        TemplateService templateService = DefaultService.getServiceByClass(TemplateService.class);
        String prompt="";
        try {
            prompt = templateService.renderTemplate(templateName, data);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
        return prompt;
    }
    
    private static String getFileContext(boolean fileContextEnabled, String fileInteractionContext, String documentLanguage){
        if (fileContextEnabled){
            Languages.CodeLanguageDetails lang = Languages.getLang(documentLanguage.toLowerCase());
            return String.format("%s %s %s", lang.getSyntaxComments().getStart(),fileInteractionContext,lang.getSyntaxComments().getEnd());
        }
        return "";
    }
}
