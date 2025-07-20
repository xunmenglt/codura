package com.xunmeng.codura.utils;

import com.xunmeng.codura.setting.provider.LlmGateUrlProvider;
import com.xunmeng.codura.service.RemoteConfigService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static volatile JedisPool pool = null;
    private static RedisConfig config = null;

    // 内部静态类，用来存 host/port/password
    private static class RedisConfig {
        String host;
        int port;
        String password;
    }

    /**
     * 获取连接池（懒加载，第一次用时再初始化）
     */
    private static JedisPool getPool() {
        if (pool == null) {
            synchronized (RedisUtil.class) {
                if (pool == null) {
                    // 从配置中心获取动态配置
                    LlmGateUrlProvider provider = RemoteConfigService.fetchLlmGateUrl();
                    config = new RedisConfig();
                    config.host = provider.getHost();
                    config.port = provider.getRedisPort();
                    config.password = provider.getRedisPassword();

                    pool = new JedisPool(new JedisPoolConfig(),
                            config.host, config.port, 2000, config.password);

                    // 简单 ping 测试
                    try (Jedis jedis = pool.getResource()) {
                        String response = jedis.ping();
                        if ("PONG".equalsIgnoreCase(response)) {
                            System.out.println("✅ Redis 已连接！");
                        } else {
                            System.err.println("⚠ Redis ping 返回: " + response);
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Redis 初始化失败: " + e.getMessage());
                    }
                }
            }
        }
        return pool;
    }

    /**
     * 外部显式调用，确保连接成功
     */
    public static boolean ensureConnected() {
        try {
            try (Jedis jedis = getPool().getResource()) {
                return "PONG".equalsIgnoreCase(jedis.ping());
            }
        } catch (Exception e) {
            System.err.println("❌ Redis 连接检测失败: " + e.getMessage());
            return false;
        }
    }

    public static String get(String key) {
        try (Jedis jedis = getPool().getResource()) {
            return jedis.get(key);
        }
    }

    public static void set(String key, String value, int ttlSeconds) {
        try (Jedis jedis = getPool().getResource()) {
            jedis.setex(key, ttlSeconds, value);
        }
    }

    public static void del(String key) {
        try (Jedis jedis = getPool().getResource()) {
            jedis.del(key);
        }
    }
}
