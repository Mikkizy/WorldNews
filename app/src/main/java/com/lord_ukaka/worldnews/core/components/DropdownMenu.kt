package com.lord_ukaka.worldnews.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.sealed.MenuOption
import com.lord_ukaka.worldnews.core.utils.Dimension
import com.lord_ukaka.worldnews.domain.models.CountryDetails

@Composable
fun CountriesOptionsMenu(
    containerBackgroundColor: Color = MaterialTheme.colors.background,
    menuBackgroundColor: Color = MaterialTheme.colors.surface,
    menuContentColor: Color = MaterialTheme.colors.onSurface,
    options: List<CountryDetails>,
    onOptionsMenuExpandChanges: () -> Unit,
    onMenuOptionSelected: (option: CountryDetails) -> Unit,
    optionsMenuExpanded: Boolean,
) {
    Box {
        var country by remember {
            mutableStateOf("Country")
        }
        val scrollState = rememberScrollState()
        CustomButton(
            buttonColor = containerBackgroundColor,
            contentColor = MaterialTheme.colors.onBackground,
            text = country,
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_world), contentDescription = "world")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Arrow Dropdown"
                )
            }
        ) {
            onOptionsMenuExpandChanges()
        }
        DropdownMenu(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .background(menuBackgroundColor)
                .verticalScroll(scrollState),
            expanded = optionsMenuExpanded,
            onDismissRequest = onOptionsMenuExpandChanges,
        ) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(menuBackgroundColor)
                        .clickable {
                            onMenuOptionSelected(option)
                            country = option.name
                        }
                        .padding(
                            horizontal = Dimension.pagePadding,
                            vertical = Dimension.extraSmall
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding.div(2)),
                ) {

                    Text(
                        text = option.code,
                        color = menuContentColor,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                    )
                    Text(
                        text = option.name,
                        color = menuContentColor,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                    )
                }
            }
        }
    }
}

@Composable
fun GenderOptionsMenu(
    containerBackgroundColor: Color = MaterialTheme.colors.background,
    menuBackgroundColor: Color = MaterialTheme.colors.surface,
    menuContentColor: Color = MaterialTheme.colors.onSurface,
    options: List<String> = listOf("Male", "Female"),
    onMenuOptionSelected: (option: String) -> Unit
) {
    Box {
        var gender by remember {
            mutableStateOf("Gender")
        }
        var expanded by remember {
            mutableStateOf(false)
        }
        val scrollState = rememberScrollState()
        CustomButton(
            buttonColor = containerBackgroundColor,
            contentColor = MaterialTheme.colors.onBackground,
            text = gender,
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_people), contentDescription = "gender")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Arrow Dropdown"
                )
            }
        ) {
            expanded = !expanded
        }
        DropdownMenu(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .background(menuBackgroundColor)
                .verticalScroll(scrollState),
            expanded = expanded,
            onDismissRequest = {
                               expanded = false
            },
        ) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(menuBackgroundColor)
                        .clickable {
                            onMenuOptionSelected(option)
                            gender = option
                        }
                        .padding(
                            horizontal = Dimension.pagePadding,
                            vertical = Dimension.extraSmall
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = option,
                        color = menuContentColor,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                    )
                }
            }
        }
    }
}

@Composable
fun MoreOptionsMenu(
    modifier: Modifier,
    icon: ImageVector,
    iconSize: Dp = Dimension.smallIcon,
    iconBackgroundColor: Color = MaterialTheme.colors.background,
    menuBackgroundColor: Color = MaterialTheme.colors.surface,
    menuContentColor: Color = MaterialTheme.colors.onSurface,
    options: List<MenuOption>,
    onOptionsMenuExpandChanges: () -> Unit,
    onMenuOptionSelected: (option: MenuOption) -> Unit,
    optionsMenuExpanded: Boolean,
) {
    Box {
        AppIconButton(
            modifier = modifier,
            icon = icon,
            onButtonClicked = onOptionsMenuExpandChanges,
            iconTint = menuContentColor,
            backgroundColor = iconBackgroundColor,
            iconSize = iconSize,
        )
        DropdownMenu(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .background(menuBackgroundColor),
            expanded = optionsMenuExpanded,
            onDismissRequest = onOptionsMenuExpandChanges,
        ) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(menuBackgroundColor)
                        .clickable { onMenuOptionSelected(option) }
                        .padding(
                            horizontal = Dimension.pagePadding,
                            vertical = Dimension.extraSmall
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding.div(2)),
                ) {

                    option.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = option.title,
                            modifier = Modifier.size(Dimension.smallIcon),
                            tint = menuContentColor,
                        )
                    }
                    Text(
                        text = option.title,
                        color = menuContentColor,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                    )
                }
            }
        }
    }
}