package com.example.gym_tok.model

import kotlinx.serialization.Serializable

@Serializable
data class Gym(
    val id: Int,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val photoUrl: String,
    val websiteUrl: String
)