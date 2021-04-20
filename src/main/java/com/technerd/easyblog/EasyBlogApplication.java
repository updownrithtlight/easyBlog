package com.technerd.easyblog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

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
}
