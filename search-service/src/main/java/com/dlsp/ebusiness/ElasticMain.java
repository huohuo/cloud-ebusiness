package com.dlsp.ebusiness;

import java.net.InetAddress;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.dlsp.ebusiness.common.item.Item;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticMain {

	public static void main(String args[]) {
		Settings settings = Settings.builder()
									.put("cluster.name", "dlsp")
									.put("client.transport.sniff", true)
									.build();
		TransportClient client = new PreBuiltTransportClient(settings);
		String nodes = "127.0.0.1:9202,127.0.0.1:9302,127.0.0.1:9402";
		try {
			for (String node : nodes.split(",")) {
				String[] address = node.split(":");
				client.addTransportAddress(
						new TransportAddress(InetAddress.getByName(address[0]), Integer.valueOf(address[1])));
			}
			Item item = new Item();
			item.setId(187L);
			item.setName("冰箱");
			item.setDescription("海尔品牌 值得信赖");
			item.setPrice(998L);
			item.setSellerId(10086L);
			ObjectMapper objectMapper = new ObjectMapper();
			IndexResponse response = client
					.prepareIndex("item", "item")
					.setSource(objectMapper.writeValueAsString(item), XContentType.JSON).get();
			/** --
			 * XContentBuilder builder = jsonBuilder()
			 *     .startObject()
			 *         .field("user", "kimchy")
			 *         .field("postDate", new Date())
			 *         .field("message", "trying out Elasticsearch")
			 *     .endObject()
			 */
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}
