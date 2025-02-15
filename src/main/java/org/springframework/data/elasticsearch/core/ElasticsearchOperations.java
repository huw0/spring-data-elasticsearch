/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.elasticsearch.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.lang.Nullable;

/**
 * ElasticsearchOperations
 *
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Kevin Leturc
 * @author Zetang Zeng
 * @author Dmitriy Yakovlev
 * @author Peter-Josef Meisch
 */
public interface ElasticsearchOperations {

	/**
	 * adding new alias
	 *
	 * @param query
	 * @return
	 */
	boolean addAlias(AliasQuery query);

	/**
	 * removing previously created alias
	 *
	 * @param query
	 * @return
	 */
	boolean removeAlias(AliasQuery query);

	/**
	 * Create an index for a class
	 *
	 * @param clazz
	 * @param <T>
	 */
	<T> boolean createIndex(Class<T> clazz);

	/**
	 * Create an index for given indexName
	 *
	 * @param indexName
	 */
	boolean createIndex(String indexName);

	/**
	 * Create an index for given indexName and Settings
	 *
	 * @param indexName
	 * @param settings
	 */
	boolean createIndex(String indexName, Object settings);

	/**
	 * Create an index for given class and Settings
	 *
	 * @param clazz
	 * @param settings
	 */
	<T> boolean createIndex(Class<T> clazz, Object settings);

	/**
	 * Create mapping for a class
	 *
	 * @param clazz
	 * @param <T>
	 */
	<T> boolean putMapping(Class<T> clazz);

	/**
	 * Create mapping for the given class and put the mapping to the given indexName and type.
	 *
	 * @param indexName
	 * @param type
	 * @param mappings
	 * @since 3.2
	 */
	<T> boolean putMapping(String indexName, String type, Class<T> clazz);

	/**
	 * Create mapping for a given indexName and type
	 *
	 * @param indexName
	 * @param type
	 * @param mappings
	 */
	boolean putMapping(String indexName, String type, Object mappings);

	/**
	 * Create mapping for a class
	 *
	 * @param clazz
	 * @param mappings
	 */
	<T> boolean putMapping(Class<T> clazz, Object mappings);

	/**
	 * Get mapping for a class
	 *
	 * @param clazz
	 * @param <T>
	 */
	<T> Map<String, Object> getMapping(Class<T> clazz);

	/**
	 * Get mapping for a given indexName and type
	 *
	 * @param indexName
	 * @param type
	 */
	Map<String, Object> getMapping(String indexName, String type);

	/**
	 * Get settings for a given indexName
	 *
	 * @param indexName
	 */
	Map<String, Object> getSetting(String indexName);

	/**
	 * Get settings for a given class
	 *
	 * @param clazz
	 */
	<T> Map<String, Object> getSetting(Class<T> clazz);

	/**
	 * get all the alias pointing to specified index
	 *
	 * @param indexName
	 * @return
	 */
	List<AliasMetaData> queryForAlias(String indexName);

	<T> T query(SearchQuery query, ResultsExtractor<T> resultsExtractor);

	/**
	 * Execute the query against elasticsearch and return the first returned object
	 *
	 * @param query
	 * @param clazz
	 * @return the first matching object
	 */
	<T> T queryForObject(GetQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return the first returned object using custom mapper
	 *
	 * @param query
	 * @param clazz
	 * @param mapper
	 * @return the first matching object
	 */
	<T> T queryForObject(GetQuery query, Class<T> clazz, GetResultMapper mapper);

	/**
	 * Execute the query against elasticsearch and return the first returned object
	 *
	 * @param query
	 * @param clazz
	 * @return the first matching object
	 */
	<T> T queryForObject(CriteriaQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return the first returned object
	 *
	 * @param query
	 * @param clazz
	 * @return the first matching object
	 */
	<T> T queryForObject(StringQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return result as {@link Page}
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> Page<T> queryForPage(SearchQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return result as {@link Page} using custom mapper
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> Page<T> queryForPage(SearchQuery query, Class<T> clazz, SearchResultMapper mapper);

	/**
	 * Execute the multi-search against elasticsearch and return result as {@link List} of {@link Page}
	 *
	 * @param queries
	 * @param clazz
	 * @return
	 */
	<T> List<Page<T>> queryForPage(List<SearchQuery> queries, Class<T> clazz);

	/**
	 * Execute the multi-search against elasticsearch and return result as {@link List} of {@link Page} using custom
	 * mapper
	 *
	 * @param queries
	 * @param clazz
	 * @return
	 */
	<T> List<Page<T>> queryForPage(List<SearchQuery> queries, Class<T> clazz, SearchResultMapper mapper);

	/**
	 * Execute the multi-search against elasticsearch and return result as {@link List} of {@link Page}
	 *
	 * @param queries
	 * @param classes
	 * @return
	 */
	List<Page<?>> queryForPage(List<SearchQuery> queries, List<Class<?>> classes);

	/**
	 * Execute the multi-search against elasticsearch and return result as {@link List} of {@link Page} using custom
	 * mapper
	 *
	 * @param queries
	 * @param classes
	 * @return
	 */
	List<Page<?>> queryForPage(List<SearchQuery> queries, List<Class<?>> classes, SearchResultMapper mapper);

	/**
	 * Execute the query against elasticsearch and return result as {@link Page}
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> Page<T> queryForPage(CriteriaQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return result as {@link Page}
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> Page<T> queryForPage(StringQuery query, Class<T> clazz);

	/**
	 * Execute the query against elasticsearch and return result as {@link Page} using custom mapper
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> Page<T> queryForPage(StringQuery query, Class<T> clazz, SearchResultMapper mapper);

	/**
	 * Executes the given {@link CriteriaQuery} against elasticsearch and return result as {@link CloseableIterator}.
	 * <p>
	 * Returns a {@link CloseableIterator} that wraps an Elasticsearch scroll context that needs to be closed in case of
	 * error.
	 *
	 * @param <T> element return type
	 * @param query
	 * @param clazz
	 * @return
	 * @since 1.3
	 */
	<T> CloseableIterator<T> stream(CriteriaQuery query, Class<T> clazz);

	/**
	 * Executes the given {@link SearchQuery} against elasticsearch and return result as {@link CloseableIterator}.
	 * <p>
	 * Returns a {@link CloseableIterator} that wraps an Elasticsearch scroll context that needs to be closed in case of
	 * error.
	 *
	 * @param <T> element return type
	 * @param query
	 * @param clazz
	 * @return
	 * @since 1.3
	 */
	<T> CloseableIterator<T> stream(SearchQuery query, Class<T> clazz);

	/**
	 * Executes the given {@link SearchQuery} against elasticsearch and return result as {@link CloseableIterator} using
	 * custom mapper.
	 * <p>
	 * Returns a {@link CloseableIterator} that wraps an Elasticsearch scroll context that needs to be closed in case of
	 * error.
	 *
	 * @param <T> element return type
	 * @param query
	 * @param clazz
	 * @param mapper
	 * @return
	 * @since 1.3
	 */
	<T> CloseableIterator<T> stream(SearchQuery query, Class<T> clazz, SearchResultMapper mapper);

	/**
	 * Execute the criteria query against elasticsearch and return result as {@link List}
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> queryForList(CriteriaQuery query, Class<T> clazz);

	/**
	 * Execute the string query against elasticsearch and return result as {@link List}
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> queryForList(StringQuery query, Class<T> clazz);

	/**
	 * Execute the search query against elasticsearch and return result as {@link List}
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> List<T> queryForList(SearchQuery query, Class<T> clazz);

	/**
	 * Execute the multi search query against elasticsearch and return result as {@link List}
	 *
	 * @param queries
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	default <T> List<List<T>> queryForList(List<SearchQuery> queries, Class<T> clazz) {
		return queryForPage(queries, clazz).stream().map(Page::getContent).collect(Collectors.toList());
	}

	/**
	 * Execute the multi search query against elasticsearch and return result as {@link List}
	 *
	 * @param queries
	 * @param classes
	 * @return
	 */
	default List<List<?>> queryForList(List<SearchQuery> queries, List<Class<?>> classes) {
		return queryForPage(queries, classes).stream().map(Page::getContent).collect(Collectors.toList());
	}

	/**
	 * Execute the query against elasticsearch and return ids
	 *
	 * @param query
	 * @return
	 */
	<T> List<String> queryForIds(SearchQuery query);

	/**
	 * return number of elements found by given query
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> long count(CriteriaQuery query, Class<T> clazz);

	/**
	 * return number of elements found by given query
	 *
	 * @param query
	 * @return
	 */
	<T> long count(CriteriaQuery query);

	/**
	 * return number of elements found by given query
	 *
	 * @param query
	 * @param clazz
	 * @return
	 */
	<T> long count(SearchQuery query, Class<T> clazz);

	/**
	 * return number of elements found by given query
	 *
	 * @param query
	 * @return
	 */
	<T> long count(SearchQuery query);

	/**
	 * Execute a multiGet against elasticsearch for the given ids
	 *
	 * @param searchQuery
	 * @param clazz
	 * @return
	 */
	<T> List<T> multiGet(SearchQuery searchQuery, Class<T> clazz);

	/**
	 * Execute a multiGet against elasticsearch for the given ids with MultiGetResultMapper
	 *
	 * @param searchQuery
	 * @param clazz
	 * @param multiGetResultMapper
	 * @return
	 */
	<T> List<T> multiGet(SearchQuery searchQuery, Class<T> clazz, MultiGetResultMapper multiGetResultMapper);

	/**
	 * Index an object. Will do save or update
	 *
	 * @param query
	 * @return returns the document id
	 */
	String index(IndexQuery query);

	/**
	 * Partial update of the document
	 *
	 * @param updateQuery
	 * @return
	 */
	UpdateResponse update(UpdateQuery updateQuery);

	/**
	 * Bulk index all objects. Will do save or update.
	 *
	 * @param queries the queries to execute in bulk
	 */
	default void bulkIndex(List<IndexQuery> queries) {
		bulkIndex(queries, BulkOptions.defaultOptions());
	}

	/**
	 * Bulk index all objects. Will do save or update.
	 *
	 * @param queries the queries to execute in bulk
	 * @param bulkOptions options to be added to the bulk request
	 * @since 3.2
	 */
	void bulkIndex(List<IndexQuery> queries, BulkOptions bulkOptions);

	/**
	 * Bulk update all objects. Will do update
	 *
	 * @param queries the queries to execute in bulk
	 */
	default void bulkUpdate(List<UpdateQuery> queries) {
		bulkUpdate(queries, BulkOptions.defaultOptions());
	}

	/**
	 * Bulk update all objects. Will do update
	 *
	 * @param queries the queries to execute in bulk
	 * @param bulkOptions options to be added to the bulk request
	 * @since 3.2
	 */
	void bulkUpdate(List<UpdateQuery> queries, BulkOptions bulkOptions);

	/**
	 * Delete the one object with provided id
	 *
	 * @param indexName
	 * @param type
	 * @param id
	 * @return documentId of the document deleted
	 */
	String delete(String indexName, String type, String id);

	/**
	 * Delete all records matching the criteria
	 *
	 * @param clazz
	 * @param criteriaQuery
	 */
	<T> void delete(CriteriaQuery criteriaQuery, Class<T> clazz);

	/**
	 * Delete the one object with provided id
	 *
	 * @param clazz
	 * @param id
	 * @return documentId of the document deleted
	 */
	<T> String delete(Class<T> clazz, String id);

	/**
	 * Delete all records matching the query
	 *
	 * @param clazz
	 * @param query
	 */
	<T> void delete(DeleteQuery query, Class<T> clazz);

	/**
	 * Delete all records matching the query
	 *
	 * @param query
	 */
	void delete(DeleteQuery query);

	/**
	 * Deletes an index for given entity
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> boolean deleteIndex(Class<T> clazz);

	/**
	 * Deletes an index for given indexName
	 *
	 * @param indexName
	 * @return
	 */
	boolean deleteIndex(String indexName);

	/**
	 * check if index is exists
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> boolean indexExists(Class<T> clazz);

	/**
	 * check if index is exists for given IndexName
	 *
	 * @param indexName
	 * @return
	 */
	boolean indexExists(String indexName);

	/**
	 * check if type is exists in an index
	 *
	 * @param index
	 * @param type
	 * @return
	 */
	boolean typeExists(String index, String type);

	/**
	 * refresh the index
	 *
	 * @param indexName
	 */
	void refresh(String indexName);

	/**
	 * refresh the index
	 *
	 * @param clazz
	 */
	<T> void refresh(Class<T> clazz);

	/**
	 * Returns scrolled page for given query
	 *
	 * @param query The search query.
	 * @param scrollTimeInMillis The time in millisecond for scroll feature
	 *          {@link org.elasticsearch.action.search.SearchRequestBuilder#setScroll(org.elasticsearch.common.unit.TimeValue)}.
	 * @param clazz The class of entity to retrieve.
	 * @return The scan id for input query.
	 */
	<T> ScrolledPage<T> startScroll(long scrollTimeInMillis, SearchQuery query, Class<T> clazz);

	/**
	 * Returns scrolled page for given query
	 *
	 * @param query The search query.
	 * @param scrollTimeInMillis The time in millisecond for scroll feature
	 *          {@link org.elasticsearch.action.search.SearchRequestBuilder#setScroll(org.elasticsearch.common.unit.TimeValue)}.
	 * @param mapper Custom impl to map result to entities
	 * @return The scan id for input query.
	 */
	<T> ScrolledPage<T> startScroll(long scrollTimeInMillis, SearchQuery query, Class<T> clazz,
			SearchResultMapper mapper);

	/**
	 * Returns scrolled page for given query
	 *
	 * @param criteriaQuery The search query.
	 * @param scrollTimeInMillis The time in millisecond for scroll feature
	 *          {@link org.elasticsearch.action.search.SearchRequestBuilder#setScroll(org.elasticsearch.common.unit.TimeValue)}.
	 * @param clazz The class of entity to retrieve.
	 * @return The scan id for input query.
	 */
	<T> ScrolledPage<T> startScroll(long scrollTimeInMillis, CriteriaQuery criteriaQuery, Class<T> clazz);

	/**
	 * Returns scrolled page for given query
	 *
	 * @param criteriaQuery The search query.
	 * @param scrollTimeInMillis The time in millisecond for scroll feature
	 *          {@link org.elasticsearch.action.search.SearchRequestBuilder#setScroll(org.elasticsearch.common.unit.TimeValue)}.
	 * @param mapper Custom impl to map result to entities
	 * @return The scan id for input query.
	 */
	<T> ScrolledPage<T> startScroll(long scrollTimeInMillis, CriteriaQuery criteriaQuery, Class<T> clazz,
			SearchResultMapper mapper);

	<T> ScrolledPage<T> continueScroll(@Nullable String scrollId, long scrollTimeInMillis, Class<T> clazz);

	<T> ScrolledPage<T> continueScroll(@Nullable String scrollId, long scrollTimeInMillis, Class<T> clazz,
			SearchResultMapper mapper);

	/**
	 * Clears the search contexts associated with specified scroll ids.
	 *
	 * @param scrollId
	 */
	<T> void clearScroll(String scrollId);

	/**
	 * more like this query to search for documents that are "like" a specific document.
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> Page<T> moreLikeThis(MoreLikeThisQuery query, Class<T> clazz);

	ElasticsearchPersistentEntity getPersistentEntityFor(Class clazz);

	/**
	 * @return Converter in use
	 */
	ElasticsearchConverter getElasticsearchConverter();
}
