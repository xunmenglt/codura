package com.xunmeng.codura.service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public final class TemplateService implements Disposable {

    private Logger logger = Logger.getInstance(TemplateService.class);
    
    private String basePath;
    private String tmpPath;
    private Handlebars handlebars;
    public TemplateService(){
        initBasePath();
        initHandlebars();
        createTemplateDir();
//        registerHandlebarsHelpers();
    }
    
    private void initHandlebars(){
        handlebars=new Handlebars();
        handlebars.registerHelper("eq", (a,b)->a==b);
    }
    
    private void initBasePath(){
        File file = new File(System.getProperty("user.home"), ".codealien/templates");
        tmpPath=file.getPath();
        String path = TemplateService.class.getClassLoader().getResource("codura/templates").getPath();
        basePath=new File(path).getPath();
    }    
    // 创建模板目录
    private void createTemplateDir(){
        File file = new File(tmpPath);
        try {
            if (!file.exists()){
                file.mkdirs();
                logger.debug(String.format("The folder %s has been created", basePath));
            }
            copyDefaultTemplates();
        }catch (Exception e){
            logger.error(String.format("Problem creating default templates %s", basePath));
        }
    }

    private void copyDefaultTemplates() {
        File baseFileDir=new File(basePath);
        if (baseFileDir.isDirectory()){
            File[] files = baseFileDir.listFiles();
            for (File file : files) {
                String filename=file.getName();
                if (file.isFile() && filename.endsWith(".hbs")){
                    Path sourcePath  = Paths.get(file.getPath());
                    Path targetPath   = Paths.get(this.tmpPath,file.getName());
                    try {
                        if (!Files.exists(targetPath)){
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        logger.error(String.format("file: %s copy to %s failure,please change template tmpdir", sourcePath.toString(),targetPath.toString()));
                    }
                }
            }
        }
    }
    
    // 获取系统内容
    public String readSystemMessageTemplate(String templateName){
        // 获取当前系统语言
        String templatePrefix = StringUtils.isEmpty(templateName) ?"" : templateName.concat("-");
        String templateNameSystem=templatePrefix.concat("system");
        String filePath = getFilePathByTemplateName(templateNameSystem);
        return getFileContent(templateName, filePath);
    }

    public String getTemplateContentFromFile(String templateName){
        // 获取当前系统语言
        String filePath = getFilePathByTemplateName(templateName);
        return getFileContent(templateName, filePath);
    }
    
    public Template compileTemplateFromFile(String content){
        try {
            return handlebars.compileInline(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String getFileContent(String templateName, String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()){
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error(String.format("read %s error", filePath));
                throw new RuntimeException(e);
            }
        }else {
            logger.warn(String.format("the template(%s) is not exist in tmpdir", templateName));
            return "";
        }
    }


    private String getFilePathByTemplateName(String templateName){
        String language = Locale.getDefault().getLanguage();
        String fileName=templateName.concat(String.format("-%s.hbs", language));
        File file = new File(tmpPath, fileName);
        if (file.exists() && file.isFile()){
            return file.getPath();
        }
        // 如果不存在本地对应语言的模板文件，尝试使用英文
        if (!language.contains("en")){
            fileName=templateName.concat(String.format("-%s.hbs", "en"));
            file = new File(tmpPath, fileName);
            if (file.exists() && file.isFile()){
                return file.getPath();
            }
        }
        file = new File(tmpPath,templateName);
        return file.getPath();
    }
    
    // 过滤系统模板
    private boolean filterSystemTemplates (String filterName) {
        return !filterName.equals("chat")&& filterName.contains("system") == false;
    }


    // 获取模板列表
    public List<String> listTemplates(){
        List <String> fileList=new ArrayList<>();
        File tmpFileDir=new File(tmpPath);
        if (tmpFileDir.isDirectory()) {
            File[] files = tmpFileDir.listFiles();
            for (File file : files) {
                String filename=file.getName();
                if (file.isFile() && filename.endsWith(".hbs")){
                    String templateName=filename.replace(".hbs","");
                    if (filterSystemTemplates(templateName)) {
                        fileList.add(templateName);
                    }
                }
            }
        }
        return fileList;
    }
    
    public String renderTemplate(String templateName,Object data) throws IOException {
        String content = getTemplateContentFromFile(templateName);
        Template template = compileTemplateFromFile(content);
        String result = template.apply(data);
        return result;
    }
    
    
    @Override
    public void dispose() {
        // pass    
    }
}
