package com.cooperativa.app.data.models

import com.google.gson.annotations.SerializedName

data class PagedMovementsResponse(
    @SerializedName("movements")
    val movements: List<MovementDto>,
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)