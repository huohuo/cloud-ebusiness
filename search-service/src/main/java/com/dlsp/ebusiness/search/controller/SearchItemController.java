package com.dlsp.ebusiness.search.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dlsp.ebusiness.common.SimpleResponse;
import com.dlsp.ebusiness.common.TypedPagination;
import com.dlsp.ebusiness.common.item.Item;
import com.dlsp.ebusiness.common.item.ItemParams;
import com.dlsp.ebusiness.search.service.ElasticsearchItemService;

@RestController
@RequestMapping("/search/item")
public class SearchItemController {

	@Resource
	private ElasticsearchItemService elasticsearchItemService;

	@RequestMapping(method = RequestMethod.POST, value = "/index")
	public SimpleResponse getItemList(@RequestBody Item item) {
		elasticsearchItemService.indexItem(item);
		return new SimpleResponse();
	}

	@RequestMapping(method = RequestMethod.POST)
	public TypedPagination<Item> getItemList(@RequestBody ItemParams params) {
		TypedPagination<Item> pagination = elasticsearchItemService.searchItem(params);
		return pagination;
	}
}
