package com.xunmeng.codura.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {
    private static final Logger logger=LoggerFactory.getLogger(AESUtil.class);
    private static final int KEY_SIZE=16;
    private static final String SECRET_KEY_PATH="META-INF/aesKey.key";
    private static SecretKey SECRET_KEY;
    
    static {
        try {
            SECRET_KEY=loadAESKeyFromFile(SECRET_KEY_PATH);
        }catch (Exception e){
            logger.warn("AES密钥加载失败");
        }
    }

    // 生成 AES 密钥
    private static SecretKey generateAESKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize); // 可选择 128, 192 或 256 位
        return keyGen.generateKey();    
    }

    // 将 AES 密钥保存到文件
    private static void saveAESKeyToFile(SecretKey secretKey, String filePath) throws IOException {
        byte[] keyBytes = secretKey.getEncoded();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(keyBytes);
        }
    }

    // 从文件加载 AES 密钥
    private static SecretKey loadAESKeyFromFile(String filePath){
        try(InputStream inputStream=AESUtil.class.getClassLoader().getResourceAsStream(SECRET_KEY_PATH)){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            byte[] keyBytes=new byte[KEY_SIZE];
            inputStream.read(keyBytes);
            return new SecretKeySpec(keyBytes, "AES");
        }catch (Exception e){
            logger.warn("加载密钥失败");
        }
        return null;
    }

    // 使用 AES 加密文本
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 使用 AES 解密文本
    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
}
