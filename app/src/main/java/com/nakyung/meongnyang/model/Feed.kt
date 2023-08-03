package com.nakyung.meongnyang.model

data class Feed(
    val feedId: Int,
    val name: String,
    val `class`: Int,
    val material: String,
    val ingredient: String,
    val img: String,
    val efficacyList: List<Efficacy>
)

data class FeedModel(
    val feedId: Int,
    val name: String,
    val img: String,
    val efficacy: String,
    val ingredient: String,
    val material: String
)

data class Efficacy(
    val id: Int,
    val name: String
)
