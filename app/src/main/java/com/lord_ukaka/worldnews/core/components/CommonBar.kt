package com.lord_ukaka.worldnews.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.utils.Dimension

@Composable
fun NewsTopBar(
    title: String,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    onBackClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = Dimension.pagePadding, vertical = Dimension.pagePadding.div(2)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.pagePadding.times(1.5f)),
    ) {
        AppIconButton(
            icon = Icons.Rounded.KeyboardArrowLeft,
            backgroundColor = Color.Transparent,
            iconTint = contentColor,
            onButtonClicked = onBackClicked,
            shape = MaterialTheme.shapes.medium,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.button,
            color = contentColor,
            maxLines = 1,
        )
    }
}

@Composable
fun SecondaryTopBar(
    onCountryClicked: () -> Unit,
    onSearchFieldClicked: () -> Unit,
    onDownloadIconClicked: () -> Unit,
    onPostIconClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(
                horizontal = Dimension.pagePadding,
                vertical = Dimension.pagePadding.minus(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NewsLocation {
            onCountryClicked()
        }
        TextField(
            modifier = Modifier
                .clickable { onSearchFieldClicked() },
            value = "Search",
            onValueChange = {},
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            }
        )
        AppIconButton(
            iconTint = MaterialTheme.colors.onBackground,
            onButtonClicked = { onDownloadIconClicked() },
            icon = Icons.Default.ArrowCircleDown
        )
        AppIconButton(
            iconTint = MaterialTheme.colors.onBackground,
            onButtonClicked = { onPostIconClicked() },
            icon = Icons.Default.PostAdd
        )
    }
}

@Composable
fun NewsLocation(
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimension.oneSpacer.div(4))
    ) {
        DrawableButton(
            onButtonClicked = { onClick() },
            painter = painterResource(id = R.drawable.ic_world_2),
            iconSize = Dimension.smallIcon.times(0.8f)
        )
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            modifier = Modifier
                .size(Dimension.smallIcon.div(5))
        )
    }
}

@Composable
fun BottomNav(
    destinations: List<Screen>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onDestinationClicked: (Screen) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    var isInHome by remember {
        mutableStateOf(false)
    }
    val homeIcon = if (isInHome) {
        painterResource(id = R.drawable.ic_home)
    } else {
        painterResource(id = R.drawable.ic_refresh)
    }

    val homeTitle = if (isInHome) "Home" else "Refresh"

    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = Dimension.elevation
    ) {
        destinations.forEach { destination ->
            val selected = destination.route == backStackEntry?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    if (destination == Screen.Home) {
                        isInHome = !isInHome
                    }
                    onDestinationClicked(destination)
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = if (destination == Screen.Home) {
                                                                      homeIcon
                                                                      } else {
                                painterResource(id = destination.icon ?: R.drawable.ic_home)
                                                                             },
                            contentDescription = stringResource(id = destination.title ?: R.string.home)
                        )
                        Text(
                            text = if (destination == Screen.Home){
                                                     homeTitle
                                                                 } else {
                                stringResource(id = destination.title ?: R.string.home)
                                                                        },
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = with(LocalDensity.current) {
                                    Dimension.small.toSp()
                                }
                            )
                        )
                    }
                }
            )
        }
    }
}

/**
 * A function that is used to get the active route in our Navigation Graph , should return the splash route if it's null
 */
@Composable
fun getActiveRoute(navController: NavHostController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route ?: "splash"
}