package com.enygoldencity.data

object PropertyRepository {
    val properties: List<Property> = listOf(
        Property(
            id = "jade",
            name = "Jade",
            cluster = Cluster.Diamond,
            priceLabel = "Rp844 jt",
            priceValueMio = 844,
            bedrooms = 3, bathrooms = 3, carport = 2,
            landArea = "56 m²", buildingArea = "57 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Jade-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Jade-denah.webp",
            description = "Minimalis modern, 3KT 3KM, cocok keluarga muda."
        ),
        Property(
            id = "jade-plus",
            name = "Jade+",
            cluster = Cluster.Diamond,
            priceLabel = "Rp906 jt",
            priceValueMio = 906,
            bedrooms = 4, bathrooms = 3, carport = 2,
            landArea = "60 m²", buildingArea = "66 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Jade-depan-1.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Jade-denah-1.webp",
            description = "Upgrade Jade dengan 4 kamar tidur lega."
        ),
        Property(
            id = "cristal",
            name = "Cristal",
            cluster = Cluster.Diamond,
            priceLabel = "Rp865 jt",
            priceValueMio = 865,
            bedrooms = 3, bathrooms = 2, carport = 2,
            landArea = "60 m²", buildingArea = "60 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Cristal-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Cristal-denah.webp",
            description = "Desain elegan 3KT 2KM, sirkulasi cahaya maksimal."
        ),
        Property(
            id = "emerald",
            name = "Emerald",
            cluster = Cluster.Diamond,
            priceLabel = "Rp1 Miliar",
            priceValueMio = 1000,
            bedrooms = 4, bathrooms = 3, carport = 2,
            landArea = "72 m²", buildingArea = "68 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Emerald-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Jade-denah-2.webp",
            description = "4KT 3KM, tanah 72m² — ruang keluarga luas."
        ),
        Property(
            id = "diamond",
            name = "Diamond",
            cluster = Cluster.Diamond,
            priceLabel = "Rp1,36 Miliar",
            priceValueMio = 1360,
            bedrooms = 4, bathrooms = 3, carport = 2,
            landArea = "105 m²", buildingArea = "87 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Diamond-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Diamond-denah.webp",
            description = "Tipe premium 105m², halaman luas & carport 2."
        ),
        Property(
            id = "sapphire",
            name = "Sapphire",
            cluster = Cluster.Diamond,
            priceLabel = "Rp1,85 Miliar",
            priceValueMio = 1850,
            bedrooms = 5, bathrooms = 4, carport = 2,
            landArea = "116 m²", buildingArea = "142 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Shapire-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Shapire-denah.webp",
            description = "Mewah 5KT 4KM, 2 lantai — untuk keluarga besar."
        ),
        Property(
            id = "oleander",
            name = "Oleander",
            cluster = Cluster.FlowerGarden,
            priceLabel = "Rp637 jt",
            priceValueMio = 637,
            bedrooms = 2, bathrooms = 2, carport = 2,
            landArea = "45 m²", buildingArea = "49,5 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Oleander-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Oleander-denah.webp",
            description = "Compact 2KT 2KM, harga paling terjangkau."
        ),
        Property(
            id = "marigold",
            name = "Marigold",
            cluster = Cluster.FlowerGarden,
            priceLabel = "Rp829 jt",
            priceValueMio = 829,
            bedrooms = 3, bathrooms = 2, carport = 2,
            landArea = "60 m²", buildingArea = "62,5 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Marigold-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Marigold-denah.webp",
            description = "3KT 2KM flower garden, taman depan asri."
        ),
        Property(
            id = "ruko-standar",
            name = "Ruko Standar",
            cluster = Cluster.Rukan,
            priceLabel = "Rp1,99 Miliar",
            priceValueMio = 1990,
            bedrooms = null, bathrooms = 3, carport = null,
            landArea = "73 m²", buildingArea = "187 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Ruko-Standart-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Ruko-Standart-denah.webp",
            description = "Rukan 3 lantai, cocok usaha + hunian."
        ),
        Property(
            id = "ruko-hook",
            name = "Ruko Hook",
            cluster = Cluster.Rukan,
            priceLabel = "Rp2,55 Miliar",
            priceValueMio = 2550,
            bedrooms = null, bathrooms = 3, carport = null,
            landArea = "92,75 m²", buildingArea = "233,5 m²",
            imageFront = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Ruko-Hook-depan.webp",
            imagePlan = "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Ruko-Hook-denah.webp",
            description = "Hook paling luas, exposure 2 sisi jalan."
        ),
    )

    fun getByCluster(cluster: Cluster): List<Property> =
        if (cluster == Cluster.All) properties else properties.filter { it.cluster == cluster }
}
