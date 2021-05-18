package com.leyou.upload.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class MyCorsConfiguration {


    @Bean
    public CorsFilter corsFilter(){

        //初始化 cors对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //用户跨域的域名，如果要携带cookie，不能写 *.*（代表所有路径都可以跨域）
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        corsConfiguration.addAllowedHeader("*");//允许携带任何头部信息
        corsConfiguration.addAllowedMethod("*");//所有请求方式都支持
        corsConfiguration.setAllowCredentials(true);//允许cookie

        //初始化cors资源对象
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


}
