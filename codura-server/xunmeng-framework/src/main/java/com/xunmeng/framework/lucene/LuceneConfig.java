package com.xunmeng.framework.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Configuration
public class LuceneConfig {

    /**
     * 注入ik分词器
     * @return
     */
    @Bean
    public Analyzer analyzer(){
        Analyzer analyzer = new IKAnalyzer();
        return analyzer;
    }
}
