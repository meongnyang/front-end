package com.nakyung.meongnyang.model

data class Pet(
    val type: Int,
    val name: String,
    val gender: String,
    val neutering: Int,
    val birth: String,
    val adopt: String,
    val speciesName: String,
    val category: Long
)

data class PetModel(
    val memberId: Int,
    val type: Int,
    val name: String,
    val gender: String,
    val neutering: Int,
    val img: String,
    val speciesName: String,
    val birth: String,
    val adopt: String,
    val ddaybirth: Int,
    val ddayadopt: Int,
    val category: Int,
    val conimalId: Int
)
data class myPet(
    val name: String,
    val gender: String,
    val species: String,
    val ddaybirth: Int,
    val ddayadopt: Int,
    val conimalImg: String
)
