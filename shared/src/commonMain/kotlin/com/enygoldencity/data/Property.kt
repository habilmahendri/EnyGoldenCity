package com.enygoldencity.data

enum class Cluster(val displayName: String) {
    All("Semua"),
    Greenwood("Greenwood"),
    Diamond("Diamond"),
    Gardenia("Gardenia"),
    FlowerGarden("Flower Garden"),
    Rukan("Ruko Komersial")
}

data class Property(
    val id: String,
    val name: String,
    val cluster: Cluster,
    val priceLabel: String,
    val priceValueMio: Int,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val carport: Int?,
    val landArea: String,
    val buildingArea: String,
    val imageFront: String,
    val imagePlan: String,
    val description: String,
    val promo: String = "DP 10% • Promo Rp20 jt",
    val floors: String = "2 Lantai",
    val status: String = "Tersedia"
)
