package com.xunmeng.codura.system.logs.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.pojo.ConversationMessage;
import com.xunmeng.codura.system.logs.constans.AIUsageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AIUsageLog extends BaseLog {
    @JsonProperty(value = "eventType")
    private AIUsageType eventType; // Code completion, code Q&A, test case writing, etc.
    @JsonProperty(value = "inputContent")
    private List<ConversationMessage> inputContent; // Input content as a dictionary
    @JsonProperty(value = "outputContent")
    private String outputContent; // Model's output content
}