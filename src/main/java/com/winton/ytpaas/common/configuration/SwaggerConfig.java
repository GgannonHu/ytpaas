package com.winton.ytpaas.common.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置类（接口文档）
 */
@Configuration
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean swagger_enable;

    @Bean
    public Docket createRestApi() {
        //添加head参数start
    	ParameterBuilder tokenPar = new ParameterBuilder();
    	List<Parameter> pars = new ArrayList<Parameter>();
    	tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end
        
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swagger_enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.winton.ytpaas"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("运通YTPaaS API文档")
                .description("springboot利用swagger构建api文档")
                .version("1.0.0")
                .build();
    }

}