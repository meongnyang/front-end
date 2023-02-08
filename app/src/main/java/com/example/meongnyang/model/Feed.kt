package com.example.meongnyang.model

data class Feed(
    val feedId: Int,
    val name: String,
    val `class`: Int,
    val material: String,
    val ingredient: String,
    val img: String,
    val efficacy: List<Efficacy>
)

data class FeedModel(
    val feedId: Int,
    val name: String,
    val img: String,
    val efficacy: List<Efficacy>
)

data class Efficacy(
    val id: Int,
    val name: String
)
