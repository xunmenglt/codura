package com.xunmeng.codura.toolwin.sharedchat.events.payload;

import com.xunmeng.codura.toolwin.sharedchat.events.eidtor.Snippet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SnippetPayload extends Payload{
    private Snippet snippet;
    
    public SnippetPayload(String id,Snippet snippet){
        super(id);
        this.snippet=snippet;
    }
}
