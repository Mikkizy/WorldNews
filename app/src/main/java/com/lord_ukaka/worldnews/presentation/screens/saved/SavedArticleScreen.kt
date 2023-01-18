package com.lord_ukaka.worldnews.presentation.screens.saved

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lord_ukaka.worldnews.core.components.AppIconButton
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.core.utils.Dimension
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.presentation.screens.home.NewsArticleItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SavedArticleScreen(
    navController: NavController,
    viewModel: SavedArticlesViewModel = hiltViewModel()
) {
    val state = remember {
        viewModel.state.value
    }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    var savedArticles = emptyList<NewsArticle>()

    lateinit var snackbarResult: SnackbarResult

    scope.launch {
        state.articles.collectLatest {
            savedArticles = it
        }
    }
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackBar -> {
                    snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Restore"
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SA_TopBar(onBackButtonClicked = { navController.navigateUp() }) {
                viewModel.onEvent(SavedArticleEvents.OnDeleteIconClicked)
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(SavedArticleEvents.RestoreArticles)
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
        ) {
            items(
               items = savedArticles,
                key = { newsArticle ->
                    newsArticle.id!!
                }
            ) { article ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.onEvent(SavedArticleEvents.OnArticleSwiped(article))
                }
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { dismissDirection ->
                        FractionalThreshold(if (dismissDirection == DismissDirection.EndToStart) 0.25f else 0.5f)
                    },
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.Default -> Color.LightGray
                                else -> Color.Red
                            }
                        )
                        val alignment = Alignment.CenterEnd
                        val icon = Icons.Default.Delete
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Delete Article",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    }
                ) {
                    NewsArticleItem(
                        newsArticle = article,
                        onArticleClicked = {
                            navController.navigate(Screen.Article.route + "?articleId=${article.id}")
                        }) {
                        //Nothing to be done here.
                    }
                    Divider(Modifier.fillMaxWidth(), Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun SA_TopBar(
    onBackButtonClicked: () -> Unit,
    onDeleteIconClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(Dimension.pagePadding.div(5))
            .background(Color.Red)
    ) {
        AppIconButton(
            modifier =Modifier.align(Alignment.CenterStart),
            iconTint = Color.White,
            onButtonClicked = { onBackButtonClicked() },
            icon = Icons.Default.ArrowBack
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Offline Reading",
            style = MaterialTheme.typography.h5,
            color = Color.White
        )
        AppIconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            iconTint = Color.White,
            onButtonClicked = { onDeleteIconClicked() },
            icon = Icons.Default.Delete
        )
    }
}