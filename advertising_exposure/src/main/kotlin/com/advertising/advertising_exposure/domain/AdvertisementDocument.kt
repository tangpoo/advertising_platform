package com.advertising.advertising_exposure.domain

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Document(indexName = "advertisements")
class AdvertisementDocument(
    @Id
    val id: Long?,
    @Field(type = FieldType.Long)
    val shopId: Long,
    @Field(type = FieldType.Text)
    val image: String?,
    @Field(type = FieldType.Text)
    val description: String,
    @Field(type = FieldType.Date, format = [DateFormat.basic_date_time])
    val createAt: OffsetDateTime,
    @Field(type = FieldType.Text)
    val region: String,
    @Field(type = FieldType.Boolean)
    val isAllowed: Boolean,
    @Field(type = FieldType.Integer)
    val minOrderPrice: Int = 0,
    @Field(type = FieldType.Integer)
    val deliveryFee: Int = 0,
    @Field(type = FieldType.Double)
    val rating: Double = 0.0,
    @Field(type = FieldType.Date, format = [DateFormat.basic_date_time])
    val startedAt: OffsetDateTime = OffsetDateTime.now()
) {
    companion object {
        fun fromEntity(advertisement: Advertisement) =
            AdvertisementDocument(
                advertisement.id,
                advertisement.shopId,
                advertisement.image,
                advertisement.description,
                advertisement.createAt.atOffset(ZoneOffset.UTC),
                advertisement.region,
                advertisement.isAllowed
            )
    }
}