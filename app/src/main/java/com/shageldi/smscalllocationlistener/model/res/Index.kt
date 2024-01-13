package com.shageldi.smscalllocationlistener.model.res

data class Index(
    val _id: String,
    val _index: String,
    val _primary_term: Int,
    val _seq_no: Int,
    val _shards: Shards,
    val _type: String,
    val _version: Int,
    val result: String,
    val status: Int
)