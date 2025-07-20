package com.xunmeng.codura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xunmeng.codura.system.pojo.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xunmeng.codura.net.HttpClient;
import com.xunmeng.codura.service.constant.ProviderType;
import com.xunmeng.codura.setting.provider.*;
import com.xunmeng.codura.setting.state.SystemInfoStateService;
import com.xunmeng.codura.utils.RedisUtil;
import okhttp3.Response;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.xunmeng.codura.service.RemoteConfigService.ConfigPaths.*;


public class RemoteConfigService {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final int Provider_CACHE_TTL = 600;
    private static final int LLmGate_CACHE_TTL = 604800;

    private static final int LLmGateUrl_CACHE_TTL = 604800*4;

    // 配置接口路径常量
    public static class ConfigPaths {


        public static final String LLMGATE_URL_CONFIG = "/api/config/llmgate";
        public static final String CHAT_CONFIG_PATH = "/api/chatConfig/user/";
        public static final String FIM_CONFIG_PATH = "/api/fimConfig/user/";
        public static final String MODEL_CONFIG_PATH = "/api/modelConfig/user/";

        public static final String Public_CHAT_CONFIG_PATH = "/api/chatConfig/public";
        public static final String Public_FIM_CONFIG_PATH = "/api/fimConfig/public";
        public static final String Public_MODEL_CONFIG_PATH = "/api/modelConfig/public";

        public static final String LLMGATE_LOG_PATH = "/llmgate/llm-log/detail/";

        public static final String LLMGATE_CONFIG_PATH = "/config/llmgate";
    }

    // ========================== 公共方法 ==========================


    //获取后管配置
    public static <T> T fetchConfig(String path, Function<JsonNode, T> parser) {
        try {
            User user = SystemInfoStateService.settings().getUserInfoProvider().getUser();
            String baseUrl = SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();

            String fullUrl;
            Map<String, String> headers = new HashMap<>();

            if (user != null
                    && user.getName() != null && !user.getName().trim().isEmpty()
                    && user.getToken() != null && !user.getToken().trim().isEmpty()
                    && !(Public_CHAT_CONFIG_PATH.equals(path)
                    || Public_FIM_CONFIG_PATH.equals(path)
                    || Public_MODEL_CONFIG_PATH.equals(path))) {
                // 有 user 且不是访问公共路径 → 拼接私有接口
                fullUrl = baseUrl + path + user.getName();
                headers.put("Authorization", "Bearer " + user.getToken());
            } else {
                // 未登录：请求公共配置
                if(path.contains("chatConfig")){
                    fullUrl = baseUrl + Public_CHAT_CONFIG_PATH;
                }else if(path.contains("fimConfig")){
                    fullUrl = baseUrl + Public_FIM_CONFIG_PATH;
                }else if(path.contains("modelConfig")){
                    fullUrl = baseUrl + Public_MODEL_CONFIG_PATH;
                }else {
                    fullUrl = baseUrl + path;
                }
                // 公共接口不需要 token
            }

            Future<Response> future = HttpClient.request("GET", fullUrl, headers, null, null);
            Response response = future.get(); // 阻塞获取

            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonNode root = mapper.readTree(json);
                if (root.has("code") && root.get("code").asInt() == 200) {
                    JsonNode data = root.get("data");
                    return parser.apply(data);
                } else {
                    System.err.println("接口返回失败 code: " + root.get("code"));
                }
            } else {
                System.err.println("HTTP 请求失败：" + response.code() + " " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T fetchConfig(String path, Class<T> clazz) {
        return fetchConfig(path, node -> {
            try {
                if (node == null || node.isNull()) {
                    return null;
                }
                if (node.isArray() && node.size() == 0) {
                    return null;
                }
                return mapper.treeToValue(node, clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    private static String getCurrentUserKey() {
        User user = SystemInfoStateService.settings().getUserInfoProvider().getUser();
        if (user != null && user.getName() != null && !user.getName().trim().isEmpty()) {
            return "user:" + user.getName();
        } else {
            //默认调用
            return "user:admin";
        }
    }
    public static <T> T fetchCachedConfig(String cacheKey, int ttlSeconds, Supplier<T> supplier, Class<T> clazz) {
        String userKey = getCurrentUserKey();
        String redisKey = "";
        if("llmGateConfig".equals(cacheKey)||"llmGateUrl".equals(cacheKey)){
            redisKey = cacheKey ;
        } else{
            redisKey = userKey + ":" + cacheKey;
        }


        try {
            String json = RedisUtil.get(redisKey);
            if (json != null) {
                return mapper.readValue(json, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 缓存未命中，调用 supplier
        T value = supplier.get();
        if (value != null) {
            try {
                String json = mapper.writeValueAsString(value);
                RedisUtil.set(redisKey, json, ttlSeconds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    // ========================== 具体配置获取 ==========================

    public static CompletionConfigProvider fetchCompletionConfig() {
        CompletionConfigProvider config = fetchConfig(ConfigPaths.FIM_CONFIG_PATH, CompletionConfigProvider.class);

        if (config == null) {
            config = fetchConfig(ConfigPaths.Public_FIM_CONFIG_PATH, CompletionConfigProvider.class);
        }

        return config;
    }

    public static ChatConfigProvider fetchChatConfig() {
        ChatConfigProvider config = fetchConfig(ConfigPaths.CHAT_CONFIG_PATH, ChatConfigProvider.class);
        if (config == null) {
            config = fetchConfig(ConfigPaths.Public_CHAT_CONFIG_PATH, ChatConfigProvider.class);
        }
        return config;
    }

    public static List<ModelProvider> fetchModelProviders() {
        List<ModelProvider> privateProviders = fetchConfig(ConfigPaths.MODEL_CONFIG_PATH, parseModelProviders());

        boolean hasChat = privateProviders.stream().anyMatch(p -> p instanceof ChatModelProvider);
        boolean hasFim  = privateProviders.stream().anyMatch(p -> p instanceof FimModelProvider);

        if (!hasChat || !hasFim) {
            System.out.println("私有配置缺失部分模型，尝试获取公共配置...");
            List<ModelProvider> publicProviders = fetchConfig(ConfigPaths.Public_MODEL_CONFIG_PATH, parseModelProviders());

            if (!hasChat) {
                publicProviders.stream()
                        .filter(p -> p instanceof ChatModelProvider)
                        .findFirst()
                        .ifPresent(privateProviders::add);
            }

            if (!hasFim) {
                publicProviders.stream()
                        .filter(p -> p instanceof FimModelProvider)
                        .findFirst()
                        .ifPresent(privateProviders::add);
            }
        }

        return privateProviders;
    }

    private static Function<JsonNode, List<ModelProvider>> parseModelProviders() {
        return node -> {
            List<ModelProvider> providers = new ArrayList<>();
            if (node != null && node.isArray()) {
                for (JsonNode item : node) {
                    int businessType = item.path("businessType").asInt();
                    try {
                        switch (ProviderType.from(businessType)) {
                            case CHAT:
                                ChatModelProvider chat = mapper.treeToValue(item, ChatModelProvider.class);
                                providers.add(chat);
                                break;
                            case FIM:
                                FimModelProvider fim = mapper.treeToValue(item, FimModelProvider.class);
                                providers.add(fim);
                                break;
                            default:
                                System.err.println("未知 businessType: " + businessType);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return providers;
        };
    }


    public static ChatModelProvider getChatModelProvider() {
        return fetchModelProviders().stream()
                .filter(p -> p instanceof ChatModelProvider)
                .map(p -> (ChatModelProvider) p)
                .findFirst()
                .orElse(null);
    }

    public static FimModelProvider getFimModelProvider() {
        return fetchModelProviders().stream()
                .filter(p -> p instanceof FimModelProvider)
                .map(p -> (FimModelProvider) p)
                .findFirst()
                .orElse(null);
    }
    public static ChatConfigProvider fetchChatConfigCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.fetchChatConfig();
        }
        return fetchCachedConfig("chatConfig", Provider_CACHE_TTL, RemoteConfigService::fetchChatConfig, ChatConfigProvider.class);
    }
    public static CompletionConfigProvider fetchCompletionConfigCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.fetchCompletionConfig();
        }
        return fetchCachedConfig("completionConfig", Provider_CACHE_TTL, RemoteConfigService::fetchCompletionConfig, CompletionConfigProvider.class);
    }
    public static ChatModelProvider getChatModelProviderCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.getChatModelProvider();
        }
        return fetchCachedConfig("chatModelProvider", Provider_CACHE_TTL, RemoteConfigService::getChatModelProvider, ChatModelProvider.class);
    }
    public static FimModelProvider getFimModelProviderCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.getFimModelProvider();
        }
        return fetchCachedConfig("fimModelProvider", Provider_CACHE_TTL, RemoteConfigService::getFimModelProvider, FimModelProvider.class);
    }


    //==============================网关=================
    //获取网关配置
    public static LlmGateConfigProvider fetchLlmGateConfig() {
        try {
            String baseUrl = RemoteConfigService.fetchLlmGateUrlCached().getBaseUrl();
            String path = baseUrl+LLMGATE_CONFIG_PATH;
            Future<Response> future = HttpClient.request("GET", path, null, null, null);
            Response response = future.get(); // 阻塞获取

            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonNode root = mapper.readTree(json);
                return mapper.treeToValue(root, LlmGateConfigProvider.class);

            } else {
                System.err.println("HTTP 请求失败：" + response.code() + " " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static LlmGateConfigProvider fetchLlmGateConfigCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.fetchLlmGateConfig();
        }
        return fetchCachedConfig(
                "llmGateConfig",              // 全局共享 key
                LLmGate_CACHE_TTL,                       // 1 周 TTL
                RemoteConfigService::fetchLlmGateConfig,
                LlmGateConfigProvider.class
        );
    }

    public static LlmGateLogProvider fetchLogByRequestId(String requestId) {


        try {
            String baseUrl = RemoteConfigService.fetchLlmGateUrlCached().getBaseUrl();
            String path = baseUrl+LLMGATE_LOG_PATH+requestId;

            Future<Response> future = HttpClient.request("GET", path, null, null, null);
            Response response = future.get();

            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonNode root = mapper.readTree(json);
                if (root.has("code") && root.get("code").asInt() == 200) {
                    JsonNode res = root.get("data");
                    return mapper.treeToValue(res, LlmGateLogProvider.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取网关ip等信息
    public static LlmGateUrlProvider fetchLlmGateUrl() {
        try {
            String baseUrl = SystemInfoStateService.settings().getSystemInfoProvider().getRequestBaseUrl();
            String path = baseUrl+LLMGATE_URL_CONFIG;
            Future<Response> future = HttpClient.request("GET", path, null, null, null);
            Response response = future.get(); // 阻塞获取

            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonNode root = mapper.readTree(json);
                return mapper.treeToValue(root, LlmGateUrlProvider.class);

            } else {
                System.err.println("HTTP 请求失败：" + response.code() + " " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LlmGateUrlProvider fetchLlmGateUrlCached() {
        if (!RedisUtil.ensureConnected()) {
            // 连接失败，直接调用真实接口，不用缓存
            return RemoteConfigService.fetchLlmGateUrl();
        }
        return fetchCachedConfig(
                "llmGateUrl",              // 全局共享 key
                LLmGateUrl_CACHE_TTL,                       // 4 周 TTL
                RemoteConfigService::fetchLlmGateUrl,
                LlmGateUrlProvider.class
        );
    }
}

