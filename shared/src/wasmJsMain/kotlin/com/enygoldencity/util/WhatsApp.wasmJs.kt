package com.enygoldencity.util

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
actual fun openUrl(url: String) {
    js("window.open(url, '_blank')")
}
