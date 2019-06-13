package com.dlsp.ebusiness.common;

import lombok.Data;

@Data
public class SimplePagination {
	private int page;
	private int pageSize;

	public int getOffset() {
		return (this.page - 1) * this.pageSize;
	}
}
