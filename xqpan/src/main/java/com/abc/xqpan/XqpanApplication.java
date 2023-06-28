package com.abc.xqpan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.abc.xqpan.mapper")
public class XqpanApplication {

	public static void main(String[] args) {
		SpringApplication.run(XqpanApplication.class, args);
	}

}
