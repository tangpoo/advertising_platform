package com.advertising.advertising_exposure.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchConfig(
    @Value("\${es.host}") val host: String,
    @Value("\${es.port}") val port: Int
) {
    @Bean
    fun elasticsearchClient(): ElasticsearchClient {
        val builder = RestClient.builder(
            HttpHost(
                host,
                port,
                "http"
            )
        )

        val transport = RestClientTransport(
            builder.build(),
            JacksonJsonpMapper()
        )

        return ElasticsearchClient(transport)
    }
}