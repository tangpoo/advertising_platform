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

        val criteria = Criteria()

        minOrderPrice?.let {
            criteria.and(Criteria("minOrderPrice").greaterThanEqual(it))
        }

        maxDeliveryFee?.let {
            criteria.and(Criteria("deliveryFee").lessThanEqual(it))
        }

        val sortField = when (sortBy) {
            "rating" -> "rating"
            else -> "createdAt"
        }

        val sortOrder = by(Order.desc(sortField))

        val criteriaQuery: Query = CriteriaQuery.builder(criteria)
            .withSort(sortOrder)
            .withPageable(pageable)
            .build()

        val searchHits: SearchHits<AdvertisementDocument> =
            elasticsearchOperations.search(criteriaQuery, AdvertisementDocument::class.java)

        return searchHits.map { it.content }
    }
}