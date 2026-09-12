package com.enygoldencity.util

/**
 * Proxy external image agar lolos CORS di wasmJs/js canvas.
 * wsrv.nl (images.weserv.nl) menambah Access-Control-Allow-Origin:* dan resize.
 * Fallback: jika url sudah proxied atau lokal, return as-is.
 */
fun proxiedImageUrl(original: String, width: Int = 600, quality: Int = 75): String {
    if (original.isBlank()) return original
    if (original.contains("wsrv.nl") || original.startsWith("data:")) return original
    if (!original.startsWith("http")) return original
    val stripped = original.removePrefix("https://").removePrefix("http://")
    // wsrv.nl: w=width, output=webp, q=quality, n=-1 keep aspect, fit=cover
    return "https://wsrv.nl/?url=$stripped&w=$width&q=$quality&output=webp&fit=cover&n=-1"
}
