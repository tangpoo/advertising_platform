package com.advertising.advertising_exposure.event

import com.advertising.advertising_exposure.domain.AdvertisementDocument
import org.springframework.context.event.EventListener
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Component

@Component
class AdvertisementEventListener(
    private val elasticsearchOperations: ElasticsearchOperations
) {

    @EventListener
    fun handleAdvertisementEvent(event: AdvertisementEvent) {
        val advertisement = event.advertisement
        when (event.eventType) {
            EventType.CREATED, EventType.UPDATED -> {
                val document = AdvertisementDocument.fromEntity(advertisement)
                elasticsearchOperations.save(document)
            }
            EventType.DELETED -> {
                elasticsearchOperations.delete(advertisement.id.toString())
            }
        }
    }
}