package com.github.tobato;

import com.github.tobato.fastdfs.annotation.EnableFastdfsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiuzulin
 */
@SpringBootApplication
@EnableFastdfsConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
