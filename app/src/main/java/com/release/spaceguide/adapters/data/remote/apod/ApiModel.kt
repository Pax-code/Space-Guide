package com.release.spaceguide.adapters.data.remote.apod

data class ApiModel(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)