package com.advertising.advertising_exposure.repository.search

import com.advertising.advertising_exposure.domain.AdvertisementDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Order
import org.springframework.data.domain.Sort.by
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.core.query.Query
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Repository

@Repository
class AdvertisementQueryRepositoryImpl(
    private val elasticsearchOperations: ElasticsearchOperations
) : AdvertisementQueryRepository {
    override fun filterAndSortAdvertisingInfos(
        minOrderPrice: Int?,
        maxDeliveryFee: Int?,
        sortBy: String,
        pageable: Pageable
    ): Streamable<AdvertisementDocument> {

        val criteria = Criteria().apply {
            minOrderPrice?.let { and(Criteria("minOrderPrice").greaterThanEqual(it)) }
            maxDeliveryFee?.let { and(Criteria("deliveryFee").lessThanEqual(it)) }
        }

        val sortField = if (sortBy == "rating") "rating" else "createdAt"
        val sortOrder = by(Order.desc(sortField))

        val criteriaQuery = CriteriaQuery.builder(criteria)
            .withSort(sortOrder)
            .withPageable(pageable)
            .build()

        return elasticsearchOperations
            .search(criteriaQuery, AdvertisementDocument::class.java)
            .map { it.content }
    }
}