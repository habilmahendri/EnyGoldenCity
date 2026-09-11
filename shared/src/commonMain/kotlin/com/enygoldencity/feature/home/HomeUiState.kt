package com.enygoldencity.feature.home

import com.enygoldencity.data.Cluster
import com.enygoldencity.data.Property

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCluster: Cluster = Cluster.All,
    val allProperties: List<Property> = emptyList(),
    val filteredProperties: List<Property> = emptyList(),
    val sortByPriceAsc: Boolean = true
)

sealed interface HomeEvent {
    data class SearchQueryChanged(val query: String) : HomeEvent
    data class ClusterSelected(val cluster: Cluster) : HomeEvent
    data object ToggleSort : HomeEvent
}
