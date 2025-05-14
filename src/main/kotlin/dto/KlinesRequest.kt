package com.marketsmasher.dto

import kotlinx.serialization.Serializable

@Serializable
data class KlinesRequest(
    val category: String?,
    val symbol: String,
    val interval: String,
    val start: String?,
    val end: String?,
    val limit: String?
)