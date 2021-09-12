package com.leyou.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCorsConfiguration {
    Logger logger = LoggerFactory.getLogger(MyCorsConfiguration.class);
    @Bean
    public CorsFilter corsFilter(){
        //初始化 cors对象**
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //用户跨域的域名，如果要携带cookie，不能写 *.*（代表所有路径都可以跨域）
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        corsConfiguration.addAllowedOrigin("http://www.leyou.com");

//        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        corsConfiguration.setAllowCredentials(true);//允许cookie
        corsConfiguration.addAllowedHeader("*");//允许携带任何头部信息

        corsConfiguration.addAllowedMethod("*");//所有请求方式都支持

        //初始化cors资源对象
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        logger.info("进来这里了，这里是：CorsFilter:{}",urlBasedCorsConfigurationSource);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


}
