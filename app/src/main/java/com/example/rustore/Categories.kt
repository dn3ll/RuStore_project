package com.example.rustore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star

val CatIcons = mapOf(
    AppCategory.FINANCE to Icons.Outlined.ShoppingCart,
    AppCategory.TOOLS to Icons.Outlined.Build,
    AppCategory.GAMES to Icons.Outlined.Star,
    AppCategory.TRANSPORT to Icons.Outlined.Face,
    AppCategory.GOVERNMENT to Icons.Outlined.Person
)

enum class AppCategory(val title: String) {
    FINANCE("Финансы"), TOOLS("Инструменты"), GAMES("Игры"),
    GOVERNMENT("Государственные"), TRANSPORT("Транспорт")
}

enum class AgeRating(val label: String) {
    A0("0+"), A6("6+"), A8("8+"), A12("12+"), A16("16+"), A18("18+")
}
