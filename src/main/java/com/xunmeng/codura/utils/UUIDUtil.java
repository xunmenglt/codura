package com.xunmeng.codura.utils;

import java.util.UUID;
import java.util.regex.Pattern;

public abstract class UUIDUtil {
    // UUID 格式的正则表达式
    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    // 命名空间UUID（可以是任意UUID，这里用一个常见的命名空间UUID示例）
    private static final UUID NAMESPACE_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

    // 生成标准UUID（版本4）
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // 生成无连字符的UUID
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 生成基于命名空间的UUID（版本3，基于MD5哈希）
    public static String generateUUIDv3(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }

    // 生成基于命名空间的UUID（版本5，基于SHA-1哈希）
    public static String generateUUIDv5(String name) {
        return generateUUIDv5(NAMESPACE_DNS, name);
    }

    // 生成基于指定命名空间的UUID（版本5，基于SHA-1哈希）
    public static String generateUUIDv5(UUID namespace, String name) {
        return UUID.nameUUIDFromBytes((namespace.toString() + name).getBytes()).toString();
    }

    // 校验是否是有效的UUID格式
    public static boolean isValidUUID(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }

    // 去掉UUID中的连字符
    public static String removeHyphens(String uuid) {
        return uuid != null ? uuid.replace("-", "") : null;
    }

    // 格式化UUID，添加连字符
    public static String addHyphens(String uuid) {
        if (uuid == null || uuid.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID format without hyphens");
        }
        return uuid.substring(0, 8) + "-" +
                uuid.substring(8, 12) + "-" +
                uuid.substring(12, 16) + "-" +
                uuid.substring(16, 20) + "-" +
                uuid.substring(20);
    }
}
