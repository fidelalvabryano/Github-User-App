package com.fidel.githubuserappfinal.model

data class SearchUser(
    var total_count: String? = null,
    var incomplete_result: String? = null,
    var items: List<Post>? = null
)
