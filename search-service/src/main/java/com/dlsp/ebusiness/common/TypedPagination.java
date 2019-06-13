package com.dlsp.ebusiness.common;

import java.util.List;

import lombok.Data;

@Data
public class TypedPagination<T> {
	private int code = 200;
	private String message = "success";
	private String scrollId;
	private long items;
	private List<T> itemList;
}
