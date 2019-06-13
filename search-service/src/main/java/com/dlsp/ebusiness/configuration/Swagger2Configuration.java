package com.dlsp.ebusiness.configuration;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

	@Bean
	public Docket actuatorDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName("actuator")
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(not(regex("/search/.*")))
			.build()
			.pathMapping("/")
			.apiInfo(apiInfo());
	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName("search-service")
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(regex("/search/.*"))
			.build()
			.pathMapping("/")
			.directModelSubstitute(DateTime.class, String.class)
			.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Search Service")
			.description("DLSP e-business Api Documentation")
			.termsOfServiceUrl("git address")
			.contact(new Contact("DLSP", "dlsp", "huohuo5234@qq.com"))
			.license("Apache 2.0")
			.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
			.version("2.0")
			.build();
	}
}
