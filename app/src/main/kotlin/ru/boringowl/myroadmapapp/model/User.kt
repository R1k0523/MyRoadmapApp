package ru.boringowl.myroadmapapp.model

import java.util.*


class User(
    var userId: UUID? = null,
    var username: String = "",
    var email: String = "",
    var role: UserRole,
)
