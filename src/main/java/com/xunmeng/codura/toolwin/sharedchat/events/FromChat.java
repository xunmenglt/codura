package com.xunmeng.codura.toolwin.sharedchat.events;

import com.xunmeng.codura.toolwin.sharedchat.events.eventnames.FromChatEvent;
import com.xunmeng.codura.toolwin.sharedchat.events.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FromChat {
    private FromChatEvent type;
    private Payload payload;
}
