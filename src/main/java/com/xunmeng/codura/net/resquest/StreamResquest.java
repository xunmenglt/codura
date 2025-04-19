package com.xunmeng.codura.net.resquest;

import com.xunmeng.codura.net.options.RequestOptions;
import com.xunmeng.codura.net.response.ResponseCallable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamResquest {
    private RequestBodyBase body;
    private RequestOptions options;
    private ResponseCallable responseCallable;
}
