package com.lord_ukaka.worldnews.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    private val _state = MutableStateFlow(SavedArticleState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var deletedArticle: NewsArticle? = null

    private var deletedArticles: List<NewsArticle> = emptyList()

    init {
        getSavedNews()
    }

    private fun getSavedNews() {
        viewModelScope.launch {
            repository.getSavedNews().onEach { savedArticles ->
                _state.value = _state.value.copy(
                    articles = flowOf(savedArticles)
                )
            }
        }
    }

    fun onEvent(event: SavedArticleEvents) {
       when (event) {
           is SavedArticleEvents.OnArticleSwiped -> {
              viewModelScope.launch {
                  repository.deleteNews(event.article)
                  deletedArticle = event.article
                  _eventFlow.emit(UiEvents.ShowSnackBar("Article has been deleted"))
              }
           }
           is SavedArticleEvents.OnDeleteIconClicked -> {
               viewModelScope.launch {
                   repository.deleteAllNews()
                   _state.value.articles.collectLatest {
                       deletedArticles = it
                   }
                   _eventFlow.emit(UiEvents.ShowSnackBar("All saved articles have been deleted"))
               }
           }
           is SavedArticleEvents.RestoreArticle -> {
                viewModelScope.launch {
                    repository.saveNews(deletedArticle ?: return@launch)
                    deletedArticle = null
                }
           }
           is SavedArticleEvents.RestoreArticles -> {
                viewModelScope.launch {
                    deletedArticles.onEach {
                        repository.saveNews(it)
                    }
                    deletedArticles = emptyList()
                }
           }
       }
    }
}