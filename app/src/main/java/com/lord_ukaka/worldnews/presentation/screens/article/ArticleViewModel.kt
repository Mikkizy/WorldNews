package com.lord_ukaka.worldnews.presentation.screens.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ArticleViewModel @Inject constructor(
    private val repository: NewsRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(ArticleState())
    val state = _state.asStateFlow()

    private val _showOptions = MutableStateFlow(false)
    val showOptions = _showOptions.asStateFlow()

    init {
       viewModelScope.launch {
           val articleId = savedStateHandle.get<Int>("articleId")!!
           val article = repository.getNewsArticleById(articleId)
           _state.value = _state.value.copy(article = article)
       }
    }

    fun onEvent(event: ArticleEvents) {
        when(event) {
            is ArticleEvents.OnFloatingActionButtonClicked -> {
                toggleFloatingActionButton()
            }
            is ArticleEvents.OnMoreIconClicked -> {
                toggleOptionsVisibility()
            }
            is ArticleEvents.OnSaveButtonClicked -> {
                toggleFloatingActionButton()
                viewModelScope.launch {
                    val article = _state.value.article
                    article?.saveArticle = true
                    repository.saveNews(article = article!!)
                }
            }
            is ArticleEvents.OnShareButtonClicked -> {
                toggleFloatingActionButton()
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, _state.value.article?.url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(context, shareIntent, null)
            }
        }
    }

    private fun toggleFloatingActionButton() {
        if (_state.value.floatingActionButtonState == FloatingActionButtonState.Expanded) {
            _state.value = _state.value.copy(floatingActionButtonState = FloatingActionButtonState.Collapsed)
        } else _state.value = _state.value.copy(floatingActionButtonState = FloatingActionButtonState.Expanded)

    }

    private fun toggleOptionsVisibility() {
        _showOptions.value = !_showOptions.value
    }
}