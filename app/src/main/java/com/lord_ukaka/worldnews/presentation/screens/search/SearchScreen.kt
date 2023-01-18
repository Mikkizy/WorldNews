package com.lord_ukaka.worldnews.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.lord_ukaka.worldnews.core.components.AppIconButton
import com.lord_ukaka.worldnews.core.components.CustomInputField
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.core.utils.Dimension
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.presentation.screens.home.NewsArticleItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val state = remember {
        searchViewModel.state.value
    }

    val articles = state.searchResults.collectAsLazyPagingItems()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        searchViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackBar -> scaffoldState.snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchTopBar(
                modifier = Modifier,
                searchQuery = state.searchQuery,
                OnCancelSearchClicked = { searchViewModel.emptySearchQuery() },
                OnSearchClicked = { searchViewModel.searchEvents(SearchEvents.OnSearchTextClicked) },
                onValueChange = {searchViewModel.searchEvents(SearchEvents.OnSearchQueryEntered(it))},
                onFocusChange = {},
                onImeActionClicked = {searchViewModel.searchEvents(SearchEvents.OnSearchTextClicked)}
            ) {
               navController.navigateUp()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = articles,
                key = { item: NewsArticle ->
                    item.id!!
                }
            ) { newsArticle ->
                newsArticle?.let { article ->
                    NewsArticleItem(
                        newsArticle = article,
                        onArticleClicked = { navController.navigate(Screen.Article.route + "?articleId=${article.id}") }
                    ) {
                        //I don't want to carry out any action here.
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}

@Composable
fun SearchTopBar(
    modifier: Modifier,
    searchQuery: String,
    OnCancelSearchClicked: () -> Unit,
    OnSearchClicked: () -> Unit,
    onValueChange: (query: String) -> Unit,
    onFocusChange: (isFocused: Boolean) -> Unit,
    onImeActionClicked: KeyboardActionScope.() -> Unit,
    onBackArrowClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Red)
            .padding(
                vertical = Dimension.pagePadding.times(0.7f),
                horizontal = Dimension.pagePadding.times(0.7f)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        AppIconButton(
            iconTint = Color.White,
            onButtonClicked = { onBackArrowClicked() },
            icon = Icons.Default.ArrowBack
        )

        CustomInputField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            placeholder = "Look up your favorite headline",
            onValueChange = onValueChange,
            onFocusChange = onFocusChange,
            onKeyboardActionClicked = onImeActionClicked,
            textStyle = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
            padding = PaddingValues(
                horizontal = Dimension.pagePadding.div(2),
                vertical = Dimension.pagePadding.times(0.5f),
            ),
            backgroundColor = MaterialTheme.colors.surface,
            textColor = MaterialTheme.colors.onBackground,
            imeAction = ImeAction.Search,
            leadingIcon = { Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            ) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    AppIconButton(
                        iconTint = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
                        onButtonClicked = {OnCancelSearchClicked() },
                        icon = Icons.Default.Cancel
                    )
                }
            }
        
        )

        Text(
            modifier = Modifier.clickable {
                OnSearchClicked()
            },
            text = "SEARCH",
            style = MaterialTheme.typography.body1.copy(
                color = Color.White
            ),
        )
    }
}