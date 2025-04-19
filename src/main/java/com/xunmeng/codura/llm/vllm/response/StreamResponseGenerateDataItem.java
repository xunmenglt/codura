package com.xunmeng.codura.llm.vllm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.net.response.ResponseDataBaseItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=true)
/**
 * 实现的请求体为粗略版
 */
public class StreamResponseGenerateDataItem extends ResponseDataBaseItem {
    @JsonProperty(value = "text",required = true)
    private List<String> text;

    @Override
    public String contentValue() {
        if (text==null || text.size()<=0) return "";
        return text.get(0);
    }
}
