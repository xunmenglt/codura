package com.xunmeng.codura.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryptionUtil {
    
    private static final Logger logger= LoggerFactory.getLogger(RSAEncryptionUtil.class);
    
    // 公钥文件路径和私钥文件路径
    private static final String PUBLIC_KEY_FILE = "META-INF/publicKey.pem";
    private static final String PRIVATE_KEY_FILE = "META-INF/privateKey.pem";
    
    private static PublicKey PUBLIC_KEY;
    private static PrivateKey PRIVATE_KEY;
    
    
    static {

        try {
            PUBLIC_KEY=loadPublicKey();
        } catch (Exception e) {
            logger.warn("公钥加载失败");
        }

        try {
            PRIVATE_KEY=loadPrivateKey();
        } catch (Exception e) {
            logger.warn("私钥加载失败");
        }
    }
    
    /**
     * 加载公钥
     */
    private static PublicKey loadPublicKey() throws Exception {
        try (InputStream inputStream = RSAEncryptionUtil.class.getClassLoader().getResourceAsStream(PUBLIC_KEY_FILE)) {
            String publicKeyPEM = readKeyFile(inputStream);
            publicKeyPEM = publicKeyPEM.replaceAll("\\s", "");
            // 解码 Base64
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        }
    }

    /**
     * 加载私钥
     */
    private static PrivateKey loadPrivateKey() throws Exception {
        try (InputStream inputStream = RSAEncryptionUtil.class.getClassLoader().getResourceAsStream(PRIVATE_KEY_FILE)) {
            String privateKeyPEM = readKeyFile(inputStream);

            privateKeyPEM =privateKeyPEM.replaceAll("\\s", "");
            // 解码 Base64
            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    /**
     * 加密文本
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密文本
     */
    public static String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 从文件读取密钥内容
     */
    private static String readKeyFile(InputStream inputStream) throws Exception {
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keyBuilder.append(line).append('\n');
            }
        }
        return keyBuilder.toString();
    }

}
