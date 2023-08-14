package com.release.spaceguide.adapters.data.remote.planetsimage

data class Collection(
    val href: String,
    val items: List<İtem>,
    val links: List<LinkX>,
    val metadata: Metadata,
    val version: String
)