package ru.boringowl.myroadmapapp.model

import java.util.*


class User(
    var userId: UUID? = null,
    var username: String = "",
    var email: String = "",
    var role: UserRole,
) {
    override fun equals(other: Any?): Boolean {
        return other is User &&
                other.userId == userId &&
                other.username == username &&
                other.email == email &&
                other.role == role
    }
}
