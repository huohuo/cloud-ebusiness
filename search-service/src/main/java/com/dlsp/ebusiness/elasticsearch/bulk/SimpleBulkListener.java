package com.dlsp.ebusiness.elasticsearch.bulk;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleBulkListener implements Listener {

	@Override
	public void beforeBulk(long executionId, BulkRequest request) {
		log.debug("request bulk [{}] items.", request.requests().size());
	}

	@Override
	public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
		if (response.hasFailures()) {
			log.warn("error bulk - {}.", response.buildFailureMessage());
		} else {
			log.debug("success bulk [{}] items.", response.getItems().length);
		}
	}

	@Override
	public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
		log.error("failed bulk - {}.", ExceptionUtils.getStackTrace(failure));
	}
}
