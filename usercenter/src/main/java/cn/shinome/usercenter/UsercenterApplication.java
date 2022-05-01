package cn.shinome.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.shinome.usercenter.mapper")
@SpringBootApplication
public class UsercenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsercenterApplication.class, args);
    }

}
