package com.fidel.githubuserappfinal.model

data class Post(
    var login: String? = null,
    var name: String? = null,
    var avatar_url: String? = null,
    var company: String? = null,
    var location: String? = null,
    var public_repos: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var isFav: String? = null
)