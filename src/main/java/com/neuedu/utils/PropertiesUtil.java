package com.neuedu.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static String default_properties = "sets.properties";
    private static Properties prop;
    static {
        prop = new Properties();
        try {
            ClassPathResource resource = new ClassPathResource(default_properties);
            InputStream inputStream = resource.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            prop.load(bf);
            //            InputStream is = new BufferedInputStream(new FileInputStream(getPath() + default_properties));
//            BufferedReader bf = new BufferedReader(new  InputStreamReader(is,"UTF-8"));//解决读取properties文件中产生中文乱码的问题
//            prop.load(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = prop.getProperty(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = prop.getProperty(name);
        if (value == null)
            return defaultValue;
        return (new Boolean(value)).booleanValue();
    }

    public static int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    public static int getIntProperty(String name, int defaultValue) {
        String value = prop.getProperty(name);
        if (value == null)
            return defaultValue;
        return (new Integer(value)).intValue();
    }

    public static String getPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    /**
     * 读取指定properties中的值
     * @param properties  文件名
     * @param name  要读取的属性
     * @return
     */
    private String readProper(String properties, String name) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(properties);
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return p.getProperty(name);
    }

    public static void main(String[] args) {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        String name = PropertiesUtil.getProperty("alipay_public_key");
//        String str = propertiesUtil.readProper("config.properties","str");
        Boolean bo = propertiesUtil.getBooleanProperty("boolean", false);
        System.out.println(getPath());
        System.out.println("name=="+name+","+", boolean == " +bo);
    }
}