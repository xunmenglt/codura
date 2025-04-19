package com.xunmeng;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class XunMengApplication {

    public static void main(String[] args) {
        SpringApplication.run(XunMengApplication.class, args);
        System.out.println("__   __           ___  ___                 \n" +
                "\\ \\ / /           |  \\/  |                 \n" +
                " \\ V / _   _ _ __ | .  . | ___ _ __   __ _ \n" +
                " /   \\| | | | '_ \\| |\\/| |/ _ \\ '_ \\ / _` |\n" +
                "/ /^\\ \\ |_| | | | | |  | |  __/ | | | (_| |\n" +
                "\\/   \\/\\__,_|_| |_\\_|  |_/\\___|_| |_|\\__, |\n" +
                "                                      __/ |\n" +
                "                                     |___/ \n");
    }
}
