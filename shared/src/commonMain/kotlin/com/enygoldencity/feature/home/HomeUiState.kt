package com.enygoldencity.feature.home

import com.enygoldencity.data.Cluster
import com.enygoldencity.data.Property

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCluster: Cluster = Cluster.All,
    val allProperties: List<Property> = emptyList(),
    val filteredProperties: List<Property> = emptyList(),
    val sortByPriceAsc: Boolean = true,
    val pageSize: Int = 5,
    val currentPage: Int = 1,
    val displayedProperties: List<Property> = emptyList()
) {
    val hasMore: Boolean get() = displayedProperties.size < filteredProperties.size
    val remaining: Int get() = filteredProperties.size - displayedProperties.size
}

sealed interface HomeEvent {
    data class SearchQueryChanged(val query: String) : HomeEvent
    data class ClusterSelected(val cluster: Cluster) : HomeEvent
    data object ToggleSort : HomeEvent
    data object LoadMore : HomeEvent
    data object ShowAll : HomeEvent
}
