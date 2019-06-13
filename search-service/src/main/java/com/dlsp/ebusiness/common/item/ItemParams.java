package com.dlsp.ebusiness.common.item;

import com.dlsp.ebusiness.common.SimplePagination;

import lombok.Data;

@Data
public class ItemParams extends SimplePagination {
	private int id;
	private String name;
	private String description;
}
