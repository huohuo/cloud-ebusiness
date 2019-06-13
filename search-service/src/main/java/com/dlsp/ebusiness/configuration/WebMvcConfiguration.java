package com.dlsp.ebusiness.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@Configuration
public class WebMvcConfiguration {

	@Bean
	BeanNameViewResolver viewResolver() {
		return new BeanNameViewResolver();
	}
}
