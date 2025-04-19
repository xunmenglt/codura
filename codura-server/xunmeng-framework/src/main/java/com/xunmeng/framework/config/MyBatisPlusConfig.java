package com.xunmeng.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis支持*匹配扫描包
 * 
 * @author ruoyi
 */
@Configuration
@MapperScan("com.xunmeng.**.mapper")
@EnableTransactionManagement
public class MyBatisPlusConfig
{

}