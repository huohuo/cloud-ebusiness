package com.dlsp.ebusiness.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.dlsp.ebusiness.elasticsearch.EsService;

@Configuration
public class ESConfiguration {

	@Resource
	private Environment env;

	@Bean
	public EsService esService() {
		Map<String, String> configs = new HashMap<>();
		configs.put(EsService.ES_CLUSTER_NAME, env.getRequiredProperty("es.cluster-name"));
		configs.put(EsService.ES_NODES, env.getRequiredProperty("es.nodes"));
		configs.put(EsService.ES_BULK_ACTIONS, env.getRequiredProperty("es.bulk-actions"));
		return new EsService(configs);
	}
}
