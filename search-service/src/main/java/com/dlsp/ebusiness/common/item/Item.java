package com.dlsp.ebusiness.common.item;

import lombok.Data;

@Data
public class Item {
	private Long id;
	private Long sellerId;
	private String name;
	private String description;
	private Long price;
}
