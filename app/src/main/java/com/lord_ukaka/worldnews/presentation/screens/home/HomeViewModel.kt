package com.lord_ukaka.worldnews.presentation.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.core.utils.hasInternetConnection
import com.lord_ukaka.worldnews.domain.usecases.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val topHeadlinesUseCase: GetTopHeadlinesUseCase
): ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private var _showInterestBottomSheet = MutableStateFlow(false)
    val showInterestBottomSheet = _showInterestBottomSheet.asStateFlow()

    init {
        getTopHeadlines("ng", "general")
    }

    private fun getTopHeadlines(countryCode: String, category: String) {
        viewModelScope.launch {
            if (hasInternetConnection(context)) {
                topHeadlinesUseCase(countryCode, category).onEach { response ->
                    when (response) {
                        is DataResponse.Loading -> {
                            _state.value = _state.value.copy(
                                isLoading = true,
                                articles = flow { response.data ?: PagingData.empty() }
                            )
                        }
                        is DataResponse.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                articles = flow { response.data ?: PagingData.empty() }
                            )
                        }
                        is DataResponse.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                articles =flow { response.data ?: PagingData.empty() },
                                error = response.message ?: "Unknown Error Occurred"
                            )
                            _eventFlow.emit(UiEvents.ShowSnackBar("No Internet Connection"))
                        }
                    }
                }
            } else {
                _state.value = _state.value.copy(error = "No internet connection")
                _eventFlow.emit(UiEvents.ShowSnackBar("No Internet Connection"))
            }
        }
    }

    fun onHomeEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnCategoryClicked -> {
                _state.value = _state.value.copy(category = event.category)
                getTopHeadlines("ng", event.category.name)
            }
            is HomeEvents.OnDownloadIconClicked -> {
                viewModelScope.launch {
                    _state.value.articles.collectLatest {
                        topHeadlinesUseCase.saveAllNews(it)
                    }
                }
            }
            is HomeEvents.OnPostIconClicked -> {
                _showBottomSheet.value = !_showBottomSheet.value
            }
            is HomeEvents.OnMoreInfoIconClicked -> {
                _showInterestBottomSheet.value = !_showInterestBottomSheet.value
            }
        }
    }
}