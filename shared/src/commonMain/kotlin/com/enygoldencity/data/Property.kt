package com.enygoldencity.data

enum class Cluster(val displayName: String) {
    All("Semua"),
    Diamond("Diamond"),
    FlowerGarden("Flower Garden"),
    Rukan("Rukan Greenwood")
}

data class Property(
    val id: String,
    val name: String,
    val cluster: Cluster,
    val priceLabel: String,
    val priceValueMio: Int, // for sorting approx
    val bedrooms: Int?,
    val bathrooms: Int?,
    val carport: Int?,
    val landArea: String,
    val buildingArea: String,
    val imageFront: String,
    val imagePlan: String,
    val description: String
)
