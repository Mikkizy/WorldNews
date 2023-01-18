package com.lord_ukaka.worldnews.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.lord_ukaka.worldnews.R
import com.lord_ukaka.worldnews.core.components.AppIconButton
import com.lord_ukaka.worldnews.core.components.CustomButton
import com.lord_ukaka.worldnews.core.components.SecondaryTopBar
import com.lord_ukaka.worldnews.core.sealed.Screen
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.core.utils.Dimension
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.NewsCategory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by remember {
        homeViewModel.state
    }
    val showBottomSheet = remember {
        homeViewModel.showBottomSheet
    }.collectAsState()

    val showInterestBottomSheet = remember {
        homeViewModel.showInterestBottomSheet
    }.collectAsState()

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val scope = rememberCoroutineScope()

    val articles = state.articles.collectAsLazyPagingItems()
    
    LaunchedEffect(key1 = true) {
        homeViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.ShowSnackBar -> scaffoldState.snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shadow(1.dp, MaterialTheme.shapes.medium)
            ) {
                if (showBottomSheet.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimension.pagePadding)
                    ) {
                        AppIconButton(
                            modifier = Modifier.align(Alignment.End),
                            iconTint = Color.Gray,
                            onButtonClicked = {
                                homeViewModel.onHomeEvent(HomeEvents.OnPostIconClicked)
                                scope.launch {
                                    sheetState.collapse()
                                }
                                              },
                            icon = Icons.Default.Close
                        )
                        Spacer(modifier = Modifier.height(Dimension.oneSpacer))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BottomSheetItem(
                                icon = Icons.Default.TextFields,
                                text = "Text",
                                iconTint = Color.Magenta
                            )
                            BottomSheetItem(
                                icon = Icons.Default.Image,
                                text = "Photo",
                                iconTint = Color.Yellow
                            )
                            BottomSheetItem(
                                icon = Icons.Default.Movie,
                                text = "Video",
                                iconTint = Color.Red
                            )
                            BottomSheetItem(
                                icon = Icons.Default.Poll,
                                text = "Vote",
                                iconTint = Color.Blue
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimension.oneSpacer))
                        BottomSheetItem(
                            icon = Icons.Default.Feed,
                            text = "Article",
                            iconTint = Color.Green
                        )
                    }
                }

                if (showInterestBottomSheet.value) {
                    Column(
                        modifier = Modifier
                                       .fillMaxSize()
                                       .padding(vertical = 16.dp, horizontal = 25.dp)
                               ) {
                                    InterestBottomSheetItem(
                                        icon = Icons.Outlined.ThumbDown,
                                        text = "Not interested",
                                        addArrow = false
                                    )
                                    InterestBottomSheetItem(
                                        icon = Icons.Outlined.RemoveCircleOutline,
                                        text = "Show less topics like this",
                                        addArrow = false
                                    )
                                    InterestBottomSheetItem(
                                        icon = Icons.Outlined.Block,
                                        text = "Block the source of this article",
                                        addArrow = false
                                    )
                                    InterestBottomSheetItem(
                                        icon = Icons.Outlined.Report,
                                        text = "Report",
                                        addArrow = true
                                    )
                                   Box(
                                       modifier = Modifier.fillMaxSize(),
                                       contentAlignment = Alignment.Center
                                   ) {
                                       CustomButton(
                                           modifier = Modifier.fillMaxWidth(),
                                           buttonColor = Color.Gray,
                                           contentColor = Color.White,
                                           text = "SUBMIT"
                                       ) {}
                                   }
                               }
                           }
                       }
        },
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetPeekHeight = 0.dp,
        scaffoldState = scaffoldState,
        snackbarHost = {scaffoldState.snackbarHostState},
        sheetGesturesEnabled = false
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(Dimension.oneSpacer)
        ) {
            stickyHeader {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SecondaryTopBar(
                        onCountryClicked = { navController.navigate(Screen.Profile.route) },
                        onSearchFieldClicked = { navController.navigate(Screen.Search.route) },
                        onDownloadIconClicked = { homeViewModel.onHomeEvent(HomeEvents.OnDownloadIconClicked) },
                        onPostIconClicked = {
                            scope.launch {
                                homeViewModel.onHomeEvent(HomeEvents.OnPostIconClicked)
                                if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                            }
                        }
                    )
                    NewsCategorySection(
                        categories = listOf(
                            NewsCategory(
                                name = "General",
                                endpoint = "general"
                            ),
                            NewsCategory(
                                name = "Business",
                                endpoint = "business"
                            ),
                            NewsCategory(
                                name = "Entertainment",
                                endpoint = "entertainment"
                            ),
                            NewsCategory(
                                name = "Technology",
                                endpoint = "technology"
                            ),
                            NewsCategory(
                                name = "Health",
                                endpoint = "health"
                            ),
                            NewsCategory(
                                name = "Science",
                                endpoint = "science"
                            ),
                            NewsCategory(
                                name = "Sports",
                                endpoint = "sports"
                            )
                        ),
                        onCategoryClicked = {
                                            homeViewModel.onHomeEvent(HomeEvents.OnCategoryClicked(it))
                        },
                        onOptionsButtonClicked = { navController.navigate(Screen.Options.route) },
                        selectedCategory = state.category
                    )
                }
            }
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
                        homeViewModel.onHomeEvent(HomeEvents.OnMoreInfoIconClicked)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun InterestBottomSheetItem(
    icon: ImageVector,
    text: String,
    addArrow: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(Dimension.mediumIcon),
                imageVector = icon,
                contentDescription = text
            )
            Spacer(modifier = Modifier.width(Dimension.twoSpacers))
            Text(
                text = text,
                style = MaterialTheme.typography.body1
            )
            if (addArrow) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Icon(
                        modifier = Modifier
                            .size(Dimension.smallIcon)
                            .align(Alignment.CenterEnd),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Proceed"
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray
        )
    }
}
@Composable
fun BottomSheetItem(
    icon: ImageVector,
    text: String,
    iconTint: Color
) {
    Column(
        modifier = Modifier
            .size(70.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppIconButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            iconTint = iconTint,
            onButtonClicked = { /*TODO*/ },
            icon = icon
        )
        Text(
            text = text,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun NewsArticleItem(
    newsArticle: NewsArticle,
    onArticleClicked: () -> Unit,
    onMoreIconClicked: () -> Unit
) {
    val imagePainter = rememberAsyncImagePainter(
        model = newsArticle.urlToImage,
        error = painterResource(id = R.drawable.ic_image_placeholder),
        placeholder = painterResource(id = R.drawable.ic_image_placeholder)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f)
            .padding(Dimension.pagePadding.minus(5.dp))
            .background(MaterialTheme.colors.background)
            .clickable {
                onArticleClicked()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(150.dp, 50.dp)
                ) {
                    Text(
                        text = newsArticle.title ?: "No Title",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onSurface,
                        softWrap = true,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Justify
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Image(painter = imagePainter, contentDescription = newsArticle.description)
            }
            Spacer(modifier = Modifier.height(Dimension.oneSpacer.div(2)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.wrapContentWidth(align = Alignment.Start)) {
                    ImageCounter(
                        image = Icons.Default.ThumbUp,
                        likes = newsArticle.likes
                    )
                    Spacer(modifier = Modifier.width(Dimension.oneSpacer.div(3f)))
                    ImageCounter(
                        image = Icons.Default.Message,
                        likes = newsArticle.comments
                    )
                    Spacer(modifier = Modifier.width(Dimension.oneSpacer.div(3f)))
                    Text(text = newsArticle.publishedAt ?: "Unknown Publisher")
                }
                AppIconButton(
                    iconTint = Color.LightGray,
                    onButtonClicked = { onMoreIconClicked() },
                    icon = Icons.Default.MoreVert
                )
            }
        }
    }
}

@Composable
fun ImageCounter(
    image: ImageVector,
    likes: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = image,
            contentDescription = "Heart Icon"
        )
        Divider(modifier = Modifier.width(6.dp))
        Text(
            text = likes.toString(),
            color = Color.White,
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        )
    }
}

@Composable
fun NewsCategorySection(
    categories: List<NewsCategory>,
    onCategoryClicked: (NewsCategory) -> Unit,
    onOptionsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    selectedCategory: NewsCategory
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(vertical = Dimension.pagePadding.div(4)),
        horizontalArrangement = Arrangement.spacedBy(Dimension.medium.div(2))
    ) {
        LazyRow(
            modifier = modifier
                .weight(6.0f)
        ) {
            categories.forEach { category ->
                item {
                    CategoryItem(title = category.name, selected = selectedCategory.name == category.name) {
                        onCategoryClicked(category)
                    }
                }
            }
        }
        AppIconButton(
            modifier = modifier.weight(1.0f),
            iconTint = Color.Gray,
            onButtonClicked = { onOptionsButtonClicked() },
            icon = Icons.Default.Subject,
            iconSize = Dimension.smallIcon
        )
    }
}

@Composable
fun CategoryItem(
    title: String,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .clickable { onSelected() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = if (selected) MaterialTheme.typography.body1.copy(color = Color.Black)
                        else MaterialTheme.typography.body1.copy(color = Color.Gray)
        )
        Spacer(
            modifier = Modifier
                .padding(top = Dimension.small)
                .fillMaxWidth()
                .height(Dimension.extraSmall)
                .clip(MaterialTheme.shapes.medium)
                .background(
                    if (selected) MaterialTheme.colors.onBackground
                    else Color.Transparent,
                )
        )
    }
}