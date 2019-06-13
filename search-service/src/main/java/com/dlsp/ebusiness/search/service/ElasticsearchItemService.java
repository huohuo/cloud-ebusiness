package com.dlsp.ebusiness.search.service;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.springframework.stereotype.Service;

import com.dlsp.ebusiness.common.TypedPagination;
import com.dlsp.ebusiness.common.item.Item;
import com.dlsp.ebusiness.common.item.ItemParams;
import com.dlsp.ebusiness.common.utils.JsonUtils;
import com.dlsp.ebusiness.elasticsearch.EsService;
import com.dlsp.ebusiness.elasticsearch.model.EsIndexEnum;
import com.dlsp.ebusiness.elasticsearch.model.EsMessage;

@Service
public class ElasticsearchItemService {

	@Resource
	private EsService esService;

	public void indexItem(Item item) {
		EsMessage message = EsMessage
				.builder()
				.index(EsIndexEnum.ITEM_INDEX.getIndex())
				.type(EsIndexEnum.ITEM_INDEX.getType())
				.id(String.valueOf(item.getId()))
				.source(JsonUtils.writeAsStringSilently(item))
				.build();
		esService.index(message);
	}

	public TypedPagination<Item> searchItem(ItemParams params) {
		SearchRequestBuilder builder = esService
				.client()
				.prepareSearch()
				.setIndices(EsIndexEnum.ITEM_INDEX.getIndex())
				.setTypes(EsIndexEnum.ITEM_INDEX.getType())
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(getItemQuery(params))
				.setExplain(false);
		builder.setSize(params.getPageSize());
		builder.setFrom(params.getOffset());
		SearchResponse response = builder.get();

		TypedPagination<Item> pagination = new TypedPagination<>();
		if (response.status() == RestStatus.OK) {
			List<Item> itemList = Stream.of(response.getHits().getHits()).map(hit -> {
				Item item = JsonUtils.readValueSilently(hit.getSourceAsString(), Item.class);
				return item;
			}).filter(Objects::nonNull).collect(Collectors.toList());
			pagination.setItemList(itemList);
			pagination.setItems(response.getHits().totalHits);
		}
		return pagination;
	}

	private BoolQueryBuilder getItemQuery(ItemParams params) {
		BoolQueryBuilder query = boolQuery();
		if (params.getId() > 0) {
			query.must(matchQuery("_id", params.getId()));
		} else {
			if (StringUtils.isNotEmpty(params.getName())) {
				query.must(matchPhraseQuery("name", params.getName()));
			}
			if (StringUtils.isNotEmpty(params.getDescription())) {
				query.must(matchPhraseQuery("description", params.getDescription()));
			}
		}
		return query;
	}
}
