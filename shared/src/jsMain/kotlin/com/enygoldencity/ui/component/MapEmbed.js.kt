package com.enygoldencity.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.browser.document
import org.w3c.dom.HTMLIFrameElement

@Composable
actual fun MapEmbed(modifier: Modifier) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var posX by remember { mutableStateOf(0f) }
    var posY by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    // OSM embed — no API key, interactive zoom/pan
    val osmUrl = "https://www.openstreetmap.org/export/embed.html?bbox=106.9911%2C-6.2391%2C107.0311%2C-6.1991&layer=mapnik&marker=-6.21906%2C107.0111#map=15/-6.2191/107.0111"

    var iframeRef by remember { mutableStateOf<HTMLIFrameElement?>(null) }
    Box(
        modifier = modifier.onGloballyPositioned { coords ->
            size = coords.size
            val pos = coords.positionInWindow()
            posX = pos.x
            posY = pos.y
        }
    ) {
        DisposableEffect(Unit) {
            val iframe = document.createElement("iframe") as HTMLIFrameElement
            iframe.src = osmUrl
            iframe.style.position = "absolute"
            iframe.style.border = "0"
            iframe.style.borderRadius = "16px"
            iframe.style.zIndex = "5"
            iframe.setAttribute("loading", "lazy")
            iframe.setAttribute("referrerpolicy", "no-referrer-when-downgrade")
            iframe.setAttribute("allowfullscreen", "")
            document.body?.appendChild(iframe)
            iframeRef = iframe
            onDispose {
                document.body?.removeChild(iframe)
                iframeRef = null
            }
        }
        LaunchedEffect(posX, posY, size) {
            iframeRef?.let {
                it.style.left = "${posX}px"
                it.style.top = "${posY}px"
                it.style.width = "${size.width}px"
                it.style.height = "${size.height}px"
            }
        }
        Box(Modifier.fillMaxSize())
    }
}
