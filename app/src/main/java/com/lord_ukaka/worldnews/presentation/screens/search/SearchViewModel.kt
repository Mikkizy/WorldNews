package com.lord_ukaka.worldnews.presentation.screens.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.core.sealed.UiEvents
import com.lord_ukaka.worldnews.core.utils.hasInternetConnection
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: NewsRepository
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()



    fun searchEvents(events: SearchEvents) {
        when(events) {
            is SearchEvents.OnSearchQueryEntered -> {
                _state.value = _state.value.copy(searchQuery = events.query)
            }
            is SearchEvents.OnSearchTextClicked -> {
                viewModelScope.launch {
                    if (hasInternetConnection(context)) {
                        repository.searchForNews(_state.value.searchQuery).onEach { response ->
                            when (response) {
                                is DataResponse.Loading -> {
                                    _state.value = _state.value.copy(
                                        isLoading = true,
                                        searchResults = flow { response.data ?: PagingData.empty() }
                                    )
                                }
                                is DataResponse.Success -> {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        searchResults = flow { response.data ?: PagingData.empty() }
                                    )
                                }
                                is DataResponse.Error -> {
                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        searchResults = flow { response.data ?: PagingData.empty() },
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
        }
    }

    fun emptySearchQuery() {
        _state.value = _state.value.copy(searchQuery = "")
    }
}