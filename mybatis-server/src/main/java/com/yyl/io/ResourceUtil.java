package com.yyl.io;

import java.io.InputStream;

public class ResourceUtil {
    public static InputStream getResourceAsStream(String path) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(path);
    }
}
