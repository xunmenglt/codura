package com.xunmeng.codura.listeners;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.xunmeng.codura.constants.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PluginListener implements DynamicPluginListener {
    private static final Logger logger = Logger.getInstance(PluginListener.class);
    public static String userDir = "";

    public static final String sourceDir="codealien";
    
    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        logger.info("Plugin is loadding and create config file");
        initResourseToUser();
    }

    @Override
    public void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        // 当插件卸载之前触发
        logger.warn("Plugin is being unloaded: " + pluginDescriptor.getPluginId());
        // 调用重启方法
        ApplicationManager.getApplication().invokeLater(() -> {
            ApplicationManager.getApplication().restart();
        });
    }

    // 初始化资源目录到用户目录
    private void initResourseToUser() {
        File file = new File(Constants.WORKER_DIR);
        userDir = file.getPath();
        createUserTmpDirAndData();
    }

    private void createUserTmpDirAndData() {
        File file = new File(userDir);
        try {
            if (!file.exists()) {
                file.mkdirs();
                logger.debug(String.format("The folder %s has been created", userDir));
            }
            copyDefaultTmp();
        } catch (Exception e) {
            logger.error(String.format("Problem creating default templates %s", userDir));
        }
    }

    private void copyDefaultTmp() {

        // 获取类加载器
        ClassLoader classLoader = PluginListener.class.getClassLoader();
        String jarPath=classLoader.getResource(sourceDir).getPath();
        jarPath=jarPath.split("!/"+sourceDir)[0];
        // 获取资源路径
        try {
            URL jarUrl = new URL(jarPath);
            // 复制所有文件和目录
            extractJarDirectory(jarUrl,sourceDir, userDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void extractJarDirectory(URL jarUrl, String directoryPathInJar, String targetDir) {
        try {
            // 打开JAR文件的连接
            URLConnection connection = jarUrl.openConnection();
            try (InputStream jarStream = connection.getInputStream();
                 ZipInputStream zipInputStream = new ZipInputStream(jarStream)) {

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    // 检查条目是否是我们想要复制的目录下的文件
                    if (entry.getName().startsWith(directoryPathInJar + "/")) {
                        // 获取目标文件路径
                        String entryName = entry.getName();
                        File targetFile = new File(targetDir, entryName.substring(directoryPathInJar.length()));
                        // 确保目标文件的目录存在
                        if (entry.isDirectory()){
                            targetFile.mkdirs();
                            continue;
                        }
                        // 复制文件
                        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile))) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zipInputStream.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        }
                    }
                    zipInputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
