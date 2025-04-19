package com.xunmeng.codura.pojo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConversationMessage {
    @JsonProperty(value = "role")
    private String role;
    @JsonProperty(value = "content")
    
    private String content;
    @JsonProperty(value = "type")
    
    private String type;
    @JsonProperty(value = "language")
    
    private LanguageType language ;
    @JsonProperty(value = "error")
    
    private boolean error;
    
    public ConversationMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
