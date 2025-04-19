package com.xunmeng.codura.toolwin.sharedchat.events.eidtor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Snippet {
    private String language="";
    private String code="";
    private String path="";
    private String basename="";

}
