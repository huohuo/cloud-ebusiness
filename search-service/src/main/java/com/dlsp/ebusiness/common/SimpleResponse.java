package com.dlsp.ebusiness.common;

import lombok.Data;

@Data
public class SimpleResponse {
	private int code = 200;
	private String message = "success";
}
