package com.technerd.easyblog;

import com.technerd.easyblog.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <pre>
 *     easyblog run!
 * </pre>
 *
 * @author : technerd
 */
@Slf4j
@SpringBootApplication(scanBasePackages ={"com.technerd.easyblog"} )
@EnableCaching
@EnableScheduling
@MapperScan("com.technerd.easyblog.mapper*")
public class EasyBlogApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EasyBlogApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
