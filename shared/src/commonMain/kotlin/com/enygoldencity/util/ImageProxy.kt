package com.enygoldencity.util

/**
 * Proxy external image agar lolos CORS di wasmJs/js canvas.
 * wsrv.nl (images.weserv.nl) menambah Access-Control-Allow-Origin:* dan resize.
 * Fallback: jika url sudah proxied atau lokal, return as-is.
 */
fun proxiedImageUrl(original: String, width: Int = 600): String {
    if (original.isBlank()) return original
    // sudah proxied atau data url
    if (original.contains("wsrv.nl") || original.startsWith("data:")) return original
    // hanya proxy http/https
    if (!original.startsWith("http")) return original
    val stripped = original.removePrefix("https://").removePrefix("http://")
    // wsrv.nl param: url, w, output=webp (hemat), n=-1 keep aspect
    return "https://wsrv.nl/?url=$stripped&w=$width&output=webp&n=-1"
}
