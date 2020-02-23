package com.neuedu.common;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class BeanConfigClass {
    @Bean
    public HttpMessageConverters getJson(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charset.forName("utf-8"));
        config.setDateFormat("yyyy-MM-dd HH:mm:ss aa");
        config.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(list);
        HttpMessageConverter converter1=converter;
        return new HttpMessageConverters(converter1);
    }
}
