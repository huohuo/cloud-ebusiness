package com.dlsp.ebusiness.elasticsearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EsMessage {
	private String index;
	private String type;
	private String id;
	private String source;
}
