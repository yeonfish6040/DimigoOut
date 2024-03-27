package com.yeonfish.multitool.util;

public class UUID {
    public static String generate() {
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        return uuid;
    }
}
