package com.dlsp.ebusiness.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.dlsp.ebusiness.elasticsearch.bulk.SimpleBulkListener;
import com.dlsp.ebusiness.elasticsearch.model.EsMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EsService {

	public final static String ES_CLUSTER_NAME = "es.cluster-name";
	public final static String ES_NODES = "es.nodes";
	public final static String ES_BULK_ACTIONS = "es.bulk-actions";
	public final static String ES_BULK_SIZE_VALUE = "es.bulk-size-value";
	public final static String ES_FLUSH_INTERVAL = "es.bulk-flush-interval";

	private final Map<String, String> configs;
	private final TransportClient client;
	private final BulkProcessor processor;

	public EsService(Map<String, String> configs) {
		this.configs = configs;
		this.client = getClient();
		this.processor = getProcessor();
	}

	@PreDestroy
	public void destroy() throws InterruptedException {
		this.processor.awaitClose(10, TimeUnit.SECONDS);
		this.client.close();
	}

	/**
	 * index
	 * @param message
	 */
	public void index(EsMessage message) {
		IndexRequest request = new IndexRequest()
				.index(message.getIndex())
				.type(message.getType())
				.source(message.getSource(), XContentType.JSON);
		if (StringUtils.isNotEmpty(message.getId())) {
			request.id(message.getId());
		}
		this.processor.add(request);
	}

	/**
	 * getClient
	 * @return
	 */
	private TransportClient getClient() {
		String name = configs.get(ES_CLUSTER_NAME);
		String nodes = configs.get(ES_NODES);
		log.debug("ES - cluster '{}' with nodes {}", name, nodes);

		Settings settings = Settings.builder()
									.put("cluster.name", name)
									.put("client.transport.sniff", true)
									.build();
		TransportClient client = new PreBuiltTransportClient(settings);
		try {
			for (String node : nodes.split(",")) {
				String[] address = node.split(":");
				client.addTransportAddress(
						new TransportAddress(InetAddress.getByName(address[0]), Integer.valueOf(address[1])));
			}
		} catch (UnknownHostException e) {
			log.error("init ES client error {} {} {}", name, nodes,
					  ExceptionUtils.getFullStackTrace(e));
		}
		return client;
	}

	/**
	 * getProcessor
	 * @return
	 */
	private BulkProcessor getProcessor() {
		int bulkActions = Integer.valueOf(StringUtils.defaultIfBlank(configs.get(ES_BULK_ACTIONS), "100"));
		int bulkSizeValue = Integer.valueOf(StringUtils.defaultIfBlank(configs.get(ES_BULK_SIZE_VALUE), "1"));
		int flushInterval = Integer.valueOf(StringUtils.defaultIfBlank(configs.get(ES_FLUSH_INTERVAL), "1"));
		return BulkProcessor.builder(this.client, new SimpleBulkListener())
							.setBulkActions(bulkActions)
							.setBulkSize(new ByteSizeValue(bulkSizeValue, ByteSizeUnit.MB))
							.setFlushInterval(TimeValue.timeValueSeconds(flushInterval))
							.setConcurrentRequests(1)
							.setBackoffPolicy(
									BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
							.build();
	}

	public Client client() {
		return this.client;
	}

	public BulkProcessor processor() {
		return this.processor;
	}

}
