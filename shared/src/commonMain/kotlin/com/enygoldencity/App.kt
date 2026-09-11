package com.enygoldencity

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.enygoldencity.feature.home.HomeScreen
import com.enygoldencity.ui.theme.GoldenCityTheme

@Composable
@Preview
fun App() {
    GoldenCityTheme {
        HomeScreen()
    }
}