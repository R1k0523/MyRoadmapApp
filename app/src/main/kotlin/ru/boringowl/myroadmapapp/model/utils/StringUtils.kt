package ru.boringowl.myroadmapapp.model.utils

object StringUtils {
    fun checkUsername(username: String) =
        username.length > 5 == Regex("\\w*").matches(username)
    fun checkPassword(password: String) =
        password.length > 7 && Regex("([\\w!,()-])*").matches(password)
    fun checkEmail(email: String) =
        Regex("[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}").matches(email)
}
fun Double.format(digits:Int) = String.Companion.format(
    java.util.Locale.ENGLISH,
    "%#.${digits}f",
    this
)