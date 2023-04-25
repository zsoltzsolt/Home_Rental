package com.example.home_rental

data class User(
    val fullname: String = "",
    val username: String = "",
    val client: Boolean = false,
    val owner: Boolean = false
)

