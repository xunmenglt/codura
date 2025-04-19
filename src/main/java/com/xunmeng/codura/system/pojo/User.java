package com.xunmeng.codura.system.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String id;
    private String name;
    private String token;
    private String avatar;
    private List<String> roles;
    private List<String> permissions;

    public static User getDefaultUser() {
        User user = new User();
        user.id="";
        user.name="";
        user.token="";
        user.avatar="";
        user.roles=new ArrayList<>();
        user.permissions=new ArrayList<>();
        return user;
    }
}
