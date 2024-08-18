package com.fofa.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Properties;

public class AESUtil {

    private static final String ALGORITHM = "AES";

    @Value("${secretKey}")
    private static String secretKey; // 注意：密钥长度应为128位（16字节）、192位（24字节）或256位（32字节）才能工作

//    @PostConstruct
//    public void loadProperties() {
//        Resource resource = new ClassPathResource("settings.ini");
//        try {
//            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//            this.secretKey = properties.getProperty("secretKey");
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load properties from settings.ini", e);
//        }
//    }
    @PostConstruct
    public void loadProperties() {
        Resource resource = new FileSystemResource("settings.ini"); // 从当前目录读取
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            this.secretKey = properties.getProperty("secretKey");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from settings.ini", e);
        }
    }

    /**
     * 加密方法
     * @param data 待加密的数据
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密方法
     * @param encryptedData 已加密的Base64编码字符串
     * @return 解密后的原始数据
     */
    public static String decrypt(String encryptedData) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 打印异常信息到控制台或日志系统
            System.err.println("解密失败: " + e.getMessage());
            return null; // 返回null表示解密失败
        }
    }

    /**
     * 生成密钥
     * @return 密钥
     */
    public static Key generateKey() throws Exception {
        byte[] keyAsBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        // 确保密钥长度正确，这里我们填充或截断密钥使其长度为16字节
        int length = 16;
        if (keyAsBytes.length > length) {
            keyAsBytes = java.util.Arrays.copyOf(keyAsBytes, length);
        } else if (keyAsBytes.length < length) {
            byte[] extendedKey = new byte[length];
            System.arraycopy(keyAsBytes, 0, extendedKey, 0, keyAsBytes.length);
            keyAsBytes = extendedKey;
        }
        return new SecretKeySpec(keyAsBytes, ALGORITHM);
    }

    public static void main(String[] args) throws Exception {
            new AESUtil().loadProperties(); // 首先加载属性
            String originalData = "A";
            String encryptedData = encrypt(originalData);
            String decryptedData = decrypt(encryptedData);
            System.out.println("Original Data: " + originalData);
            System.out.println("Encrypted Data: " + encryptedData);
            System.out.println("Decrypted Data: " + decryptedData);
    }
}
