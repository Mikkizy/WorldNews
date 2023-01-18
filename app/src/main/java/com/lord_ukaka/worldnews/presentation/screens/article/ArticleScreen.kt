package com.lord_ukaka.worldnews.presentation.screens.article

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.components.AppIconButton
import com.lord_ukaka.worldnews.core.components.MoreOptionsMenu
import com.lord_ukaka.worldnews.core.sealed.MenuOption
import com.lord_ukaka.worldnews.core.utils.Dimension

@Composable
fun ArticleScreen(
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel()
) {
    val state = remember {
        articleViewModel.state
    }.collectAsState()

    val showOptions = remember {
        articleViewModel.showOptions
    }.collectAsState()

    val scaffoldState = rememberScaffoldState()

    val articleUrl = state.value.article?.url

    val fabItems = listOf(
        FabOptionItem(
            icon = ImageBitmap.imageResource(id = R.drawable.ic_share),
            label = "Share"
        ),
        FabOptionItem(
            icon = ImageBitmap.imageResource(id = R.drawable.ic_save),
            label = "Save"
        )
    )

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            MultipleFloatingButtons(
                multiFloatingButtonState = state.value.floatingActionButtonState,
                onMultiFabStateChanged = {
                    state.value.floatingActionButtonState = it
                },
                fabItems = fabItems,
                onFabItemClicked = { button ->
                    articleViewModel.onEvent(ArticleEvents.OnFloatingActionButtonClicked)
                    if (button.label == "Save") {
                        articleViewModel.onEvent(ArticleEvents.OnSaveButtonClicked)
                    }
                    if (button.label == "Share") {
                        articleViewModel.onEvent(ArticleEvents.OnShareButtonClicked(state.value.article!!))
                    }
                }
            )
        },
        topBar = { TopBar(
            OnBackButtonClicked = { navController.navigateUp() },
            showOptions = showOptions.value,
            OnMoreIconClicked = { articleViewModel.onEvent(ArticleEvents.OnMoreIconClicked) }
        ) }
    ) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(articleUrl!!)
            }
        }, modifier = Modifier.fillMaxSize(),
            update = {
            it.loadUrl(articleUrl!!)
        })
    }
}

@Composable
fun MultipleFloatingButtons(
    multiFloatingButtonState: FloatingActionButtonState,
    onMultiFabStateChanged: (FloatingActionButtonState) -> Unit,
    fabItems:List<FabOptionItem>,
    onFabItemClicked: (FabOptionItem) -> Unit
) {
    val transition = updateTransition(targetState = multiFloatingButtonState, label = "fab transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == FloatingActionButtonState.Expanded) 360f else 0f
    }
    //This is to animate the size of the fabs
    val fabScale by transition.animateFloat(label = "scale") {
        if (it == FloatingActionButtonState.Expanded) 36f else 0f
    }
    //This is to create a fade-in fade-out effect
    val alpha by transition.animateFloat(label ="alpha", transitionSpec = {
        tween(50)
    }) {
        if (it == FloatingActionButtonState.Expanded) 1f else 0f
    }
    //This is to animate the text shadow
    val textShadow by transition.animateDp(label ="text shadow", transitionSpec = {
        tween(50)
    }) {
        if (it == FloatingActionButtonState.Expanded) 2.dp else 0.dp
    }

    Column (
        horizontalAlignment = Alignment.End
    ) {
        if (transition.currentState == FloatingActionButtonState.Expanded) {
            fabItems.forEach { fabItem ->
                smallFloatingButton(
                    button = fabItem,
                    onButtonItemClicked = {onFabItemClicked(fabItem)},
                    alpha = alpha,
                    fabScale = fabScale,
                    textShadow = textShadow
                )
                Spacer(modifier = Modifier.height(Dimension.oneSpacer))
            }
        }
        FloatingActionButton(
            backgroundColor = Color.Red,
            onClick = {
                onMultiFabStateChanged(
                    if (transition.currentState == FloatingActionButtonState.Expanded) {
                        FloatingActionButtonState.Collapsed
                    } else FloatingActionButtonState.Expanded
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .rotate(rotate)
            )
        }
    }
}

@Composable
fun smallFloatingButton(
    button: FabOptionItem,
    alpha: Float,
    textShadow: Dp,
    fabScale: Float,
    showLabel: Boolean = true,
    onButtonItemClicked: (FabOptionItem) -> Unit
) {
    val shadow = Color.Black.copy(alpha = 0.5f)
    Row {
        if (showLabel) {
            Text(
                text = button.label,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(50)
                        ).value
                    )
                    .shadow(textShadow)
                    .background(MaterialTheme.colors.surface)
                    .padding(Dimension.pagePadding.div(5))
            )
            Spacer(modifier = Modifier.width(Dimension.oneSpacer))
        }
        Canvas(
            modifier = Modifier
                .size(Dimension.largeIcon)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp,
                        color = MaterialTheme.colors.onSurface
                    ),
                    onClick = {
                        onButtonItemClicked.invoke(button)
                    }
                )
        ) {
            drawCircle(
                color = shadow,
                radius = fabScale,
                center = Offset(
                    center.x + 2f,
                    center.y + 2f
                )
            )
            drawCircle(
                color = Color.Red,
                radius = fabScale
            )

            drawImage(
                image = button.icon,
                topLeft = Offset(
                    center.x - (button.icon.width / 2),
                    center.y - (button.icon.width / 2),
                ),
                alpha = alpha
            )
        }
    }
}

@Composable
fun TopBar(
    OnBackButtonClicked: () -> Unit,
    OnMoreIconClicked: () -> Unit,
    showOptions: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimension.pagePadding.div(2))
            .background(Color.Red)
    ) {
        AppIconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            iconTint = Color.White,
            onButtonClicked = { OnBackButtonClicked() },
            icon = Icons.Default.ArrowBack
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Reader Mode",
            color = Color.White,
            style = MaterialTheme.typography.body1
        )
        MoreOptionsMenu(
            modifier = Modifier.align(Alignment.CenterEnd),
            icon = Icons.Default.MoreHoriz,
            options = listOf(MenuOption.Favorite, MenuOption.Report, MenuOption.TextSize),
            onOptionsMenuExpandChanges = {OnMoreIconClicked()},
            onMenuOptionSelected = { menuOption ->
                OnMoreIconClicked()
                when(menuOption) {
                    is MenuOption.Favorite -> {}
                    is MenuOption.Report -> {}
                    is MenuOption.TextSize -> {}
                }
            },
            optionsMenuExpanded = showOptions
        )
    }
}