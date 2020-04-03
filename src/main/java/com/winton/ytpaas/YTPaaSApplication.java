package com.winton.ytpaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.thymeleaf.TemplateEngine;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableAutoConfiguration
@SpringBootApplication
public class YTPaaSApplication extends SpringBootServletInitializer {
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
       return builder.sources(YTPaaSApplication.class);
	}
	public static void main(String[] args) {
		TemplateEngine templateEngine = new TemplateEngine(); 
		templateEngine.addDialect(new LayoutDialect());
		
		SpringApplication.run(YTPaaSApplication.class, args);
	}

}
