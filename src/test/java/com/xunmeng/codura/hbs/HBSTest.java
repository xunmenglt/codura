package com.xunmeng.codura.hbs;

import com.github.jknack.handlebars.Template;
import com.xunmeng.codura.service.TemplateService;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class HBSTest {
    @Test
    public void resoursePath(){
//        TemplateService templateService = new TemplateService();
        String path = TemplateService.class.getClassLoader().getResource("codura/templates").getPath();
        System.out.println(new File(path).getPath());
        File file = new File(System.getProperty("user.home"), ".codealien/templates");
        System.out.println(file.getPath());
    }
    
    @Test
    public void templateServiceTest() throws IOException {
        TemplateService templateService = new TemplateService();
        String systemMessageTemplate = templateService.readSystemMessageTemplate("");
        System.out.println(systemMessageTemplate);
        System.out.println("==============================");
        String templateContentFromFile = templateService.getTemplateContentFromFile("commit-message");
        System.out.println(templateContentFromFile);
        System.out.println("==============================");
        Template template = templateService.compileTemplateFromFile(templateContentFromFile);
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("code","aaa");
        objectObjectHashMap.put("language","java");
        String con = templateService.renderTemplate("explain", objectObjectHashMap);
        System.out.println(con);
    }
}
