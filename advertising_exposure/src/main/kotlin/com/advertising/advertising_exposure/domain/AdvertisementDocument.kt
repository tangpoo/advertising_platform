package com.advertising.advertising_exposure.domain

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Mapping
import java.time.LocalDateTime

@Document(indexName = "advertisements")
class AdvertisementDocument(
    @Id @Field(type = FieldType.Long)
    val id: Long,
    @Field(type = FieldType.Long)
    val shopId: Long,
    @Field(type = FieldType.Text)
    val image: String?,
    @Field(type = FieldType.Text)
    val description: String,
    @Field(type = FieldType.Date, format = [], pattern = ["yyyy-MM-dd'T'HH:mm:ss"])
    val createdAt: LocalDateTime,
    @Field(type = FieldType.Text)
    val region: String,
    @Field(type = FieldType.Boolean)
    val isAllowed: Boolean,             // 광고 게시가 된 후에 인덱싱이 시작된다면 의미가 없어질 필드
    @Field(type = FieldType.Integer)
    val minOrderPrice: Int?,
    @Field(type = FieldType.Integer)
    val deliveryFee: Int?,
    @Field(type = FieldType.Double)
    val rating: Double?,
    @Field(type = FieldType.Date, format = [], pattern = ["yyyy-MM-dd'T'HH:mm:ss"])
    val startedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(advertisement: Advertisement) =
            AdvertisementDocument(
                advertisement.id!!,
                advertisement.shopId,
                advertisement.image,
                advertisement.description,
                advertisement.createdAt,
                advertisement.region,
                advertisement.isAllowed,
                advertisement.minOrderPrice,
                advertisement.deliveryFee,
                advertisement.rating,
                LocalDateTime.now(), // 임시 설정 todo 인덱싱은 광고 게시가 시작됐을 때 이벤트 발행
            )
    }
}