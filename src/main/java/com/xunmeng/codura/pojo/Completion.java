package com.xunmeng.codura.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Completion {
    private String prefix;
    private String suffix;
}
