package com.dlsp.ebusiness.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String writeAsStringSilently(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T readValueSilently(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
