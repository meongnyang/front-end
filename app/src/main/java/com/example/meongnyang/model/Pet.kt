package com.example.meongnyang.model

data class Pet(
    val type: Int,
    val name: String,
    val gender: Int,
    val neutering: Int,
    val birth: String,
    val adopt: String,
    val speciesName: String,
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
