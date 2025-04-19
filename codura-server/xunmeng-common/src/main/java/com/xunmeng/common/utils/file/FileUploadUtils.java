package com.xunmeng.common.utils.file;

import com.xunmeng.common.config.XunMengConfig;
import com.xunmeng.common.constant.Constants;
import com.xunmeng.common.exception.file.FileNameLengthLimitExceededException;
import com.xunmeng.common.exception.file.FileSizeLimitExceededException;
import com.xunmeng.common.exception.file.InvalidExtensionException;
import com.xunmeng.common.utils.DateUtils;
import com.xunmeng.common.utils.StringUtils;
import com.xunmeng.common.utils.uuid.Seq;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUploadUtils {
    /*默认文件最大大小50M*/
    public static final long DEFAULT_MAX_SIZE=50*1024*1024;
    
    /*默认的文件名最大长度 100*/
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = XunMengConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir)
    {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir()
    {
        return defaultBaseDir;
    }

    /**
     * 以默认配置上传文件
     */
    public static final String upload(MultipartFile file) throws IOException{
        try {
            return upload(getDefaultBaseDir(),file,MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }catch (Exception e){
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException
    {
        try
        {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension) throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
    InvalidExtensionException {
        int fileNameLength= Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength>DEFAULT_FILE_NAME_LENGTH){
            throw new FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH);
        }
        assertAllowed(file, allowedExtension);
        
        String fileName = extractFilename(file);

        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath));
        return getPathFileName(baseDir, fileName);
        
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = XunMengConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
    }
    
    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    private static String extractFilename(MultipartFile file) {
        return StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(),
                FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    private static void assertAllowed(MultipartFile file, String[] allowedExtension) 
            throws FileSizeLimitExceededException, InvalidExtensionException {
        long size=file.getSize();
        if (size>DEFAULT_MAX_SIZE){
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);

        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
                        fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    private static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String ae : allowedExtension) {
            if (extension.equalsIgnoreCase(extension)){
                return true;
            }
        }
        return false;
    }

    private static String getExtension(MultipartFile file) {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}
