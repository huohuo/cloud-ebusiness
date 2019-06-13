package com.dlsp.ebusiness.elasticsearch.model;

import lombok.Getter;

@Getter
public enum EsIndexEnum {
	ITEM_INDEX("item", "item");

	private String index;
	private String type;

	EsIndexEnum(String index, String type) {
		this.index = index;
		this.type = type;
	}
}
