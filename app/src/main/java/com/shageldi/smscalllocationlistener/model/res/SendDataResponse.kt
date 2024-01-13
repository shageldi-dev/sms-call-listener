package com.shageldi.smscalllocationlistener.model.res

data class SendDataResponse(
    val errors: Boolean,
    val items: List<Item>,
    val took: Int
)