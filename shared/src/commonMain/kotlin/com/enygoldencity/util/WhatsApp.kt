package com.enygoldencity.util

import com.enygoldencity.data.Property

const val WHATSAPP_NUMBER = "6281280404180"

expect fun openUrl(url: String)

fun buildWhatsAppUrl(property: Property? = null, customMessage: String? = null): String {
    val base = "https://wa.me/$WHATSAPP_NUMBER"
    val message = customMessage ?: if (property != null) {
        "Halo Kak Eny, saya tertarik dengan Golden City Bekasi tipe ${property.name} (${property.priceLabel}). " +
                "LT ${property.landArea} LB ${property.buildingArea}. " +
                "Boleh info detail & jadwal survey? Terima kasih!"
    } else {
        "Halo Kak Eny, saya tertarik dengan Golden City Bekasi. Boleh info harga & promo terbaru?"
    }
    val encoded = message.encodeUrl()
    return "$base?text=$encoded"
}

// simple encode for commonMain without platform dependency
private fun String.encodeUrl(): String = buildString {
    for (c in this@encodeUrl) {
        when {
            c.isLetterOrDigit() || c in "-_.~" -> append(c)
            c == ' ' -> append("%20")
            else -> {
                val bytes = c.toString().encodeToByteArray()
                for (b in bytes) append("%${b.toUByte().toString(16).uppercase().padStart(2,'0')}")
            }
        }
    }
}
