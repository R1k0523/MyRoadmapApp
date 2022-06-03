package ru.boringowl.myroadmapapp.presentation.base

fun Double.format(digits:Int) = String.Companion.format(
    java.util.Locale.ENGLISH,
    "%#.${digits}f",
    this
)