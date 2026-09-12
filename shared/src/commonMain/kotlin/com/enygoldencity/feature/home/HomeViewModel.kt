package com.enygoldencity.feature.home

import androidx.lifecycle.ViewModel
import com.enygoldencity.data.Cluster
import com.enygoldencity.data.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val all = PropertyRepository.properties

    private val _state = MutableStateFlow(
        HomeUiState(
            allProperties = all,
            filteredProperties = all,
            displayedProperties = all.take(5)
        )
    )
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> applyFilter(search = event.query)
            is HomeEvent.ClusterSelected -> applyFilter(cluster = event.cluster)
            HomeEvent.ToggleSort -> toggleSort()
            HomeEvent.LoadMore -> loadMore()
            HomeEvent.ShowAll -> showAll()
        }
    }

    private fun applyFilter(
        search: String? = null,
        cluster: Cluster? = null
    ) {
        _state.update { cur ->
            val newSearch = search ?: cur.searchQuery
            val newCluster = cluster ?: cur.selectedCluster
            var filtered = if (newCluster == Cluster.All) cur.allProperties
            else cur.allProperties.filter { it.cluster == newCluster }

            if (newSearch.isNotBlank()) {
                val q = newSearch.lowercase()
                filtered = filtered.filter {
                    it.name.lowercase().contains(q) ||
                            it.description.lowercase().contains(q) ||
                            it.priceLabel.lowercase().contains(q)
                }
            }
            filtered = if (cur.sortByPriceAsc) filtered.sortedBy { it.priceValueMio }
            else filtered.sortedByDescending { it.priceValueMio }

            val displayed = filtered.take(cur.pageSize * 1)
            cur.copy(
                searchQuery = newSearch,
                selectedCluster = newCluster,
                filteredProperties = filtered,
                currentPage = 1,
                displayedProperties = displayed
            )
        }
    }

    private fun toggleSort() {
        _state.update { cur ->
            val sorted = if (cur.sortByPriceAsc) cur.filteredProperties.sortedByDescending { it.priceValueMio }
            else cur.filteredProperties.sortedBy { it.priceValueMio }
            val displayed = sorted.take(cur.pageSize * cur.currentPage)
            cur.copy(sortByPriceAsc = !cur.sortByPriceAsc, filteredProperties = sorted, displayedProperties = displayed)
        }
    }

    private fun loadMore() {
        _state.update { cur ->
            val nextPage = cur.currentPage + 1
            val displayed = cur.filteredProperties.take(nextPage * cur.pageSize)
            cur.copy(currentPage = nextPage, displayedProperties = displayed)
        }
    }

    private fun showAll() {
        _state.update { cur ->
            cur.copy(displayedProperties = cur.filteredProperties, currentPage = (cur.filteredProperties.size + cur.pageSize - 1) / cur.pageSize)
        }
    }
}
