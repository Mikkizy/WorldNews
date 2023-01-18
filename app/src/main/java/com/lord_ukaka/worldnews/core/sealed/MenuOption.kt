package com.lord_ukaka.worldnews.core.sealed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuOption (
    open val title: String,
    open val icon: ImageVector? = null
) {
    object Favorite: MenuOption(title = "Favorite", icon = Icons.Default.BookmarkBorder)
    object Report: MenuOption(title = "Report", icon = Icons.Default.Report)
    object TextSize: MenuOption(title = "Text Size", icon = Icons.Default.TextFields)
}
