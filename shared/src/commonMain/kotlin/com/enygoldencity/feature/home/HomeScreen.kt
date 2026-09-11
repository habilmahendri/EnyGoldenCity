package com.enygoldencity.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.enygoldencity.data.Cluster
import com.enygoldencity.ui.component.PropertyCard
import com.enygoldencity.util.buildWhatsAppUrl
import com.enygoldencity.util.openUrl
import com.enygoldencity.util.proxiedImageUrl

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel { HomeViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var lightboxUrl by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val maxW = maxWidth
        val isCompact = maxW < 720.dp
        val isMedium = maxW < 1020.dp
        val horizontalPadding: Dp = when {
            isCompact -> 16.dp
            isMedium -> 24.dp
            else -> 32.dp
        }
        val contentMaxWidth = 1120.dp

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                HeroSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding,
                    onPrimaryCta = { openUrl(buildWhatsAppUrl()) },
                    onImageClick = { lightboxUrl = it }
                )
                // centered content
                Column(
                    modifier = Modifier.widthIn(max = contentMaxWidth).fillMaxWidth().padding(horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Spacer(Modifier.height(20.dp))
                    FilterSection(state = state, onEvent = viewModel::onEvent, isCompact = isCompact)
                    Spacer(Modifier.height(12.dp))
                    PropertyGrid(state = state, isCompact = isCompact, isMedium = isMedium, onImageClick = { lightboxUrl = it })
                    Spacer(Modifier.height(8.dp))
                }
                KelebihanSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                FacilitiesSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                LocationSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding,
                    onWhatsAppClick = { openUrl(buildWhatsAppUrl()) },
                    onImageClick = { lightboxUrl = it }
                )
                TestimoniSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                Footer(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                Spacer(Modifier.height(88.dp))
            }
            FloatingWhatsApp(
                modifier = Modifier.align(Alignment.BottomEnd).padding(if (isCompact) 14.dp else 20.dp),
                compact = isCompact,
                onClick = { openUrl(buildWhatsAppUrl()) }
            )
            lightboxUrl?.let { url ->
                ImageLightbox(imageUrl = url, onDismiss = { lightboxUrl = null })
            }
        }
    }
}

@Composable
private fun TopBar(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().widthIn(max = maxWidth).padding(horizontal = padding, vertical = if (isCompact) 10.dp else 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // logo mark
                Box(
                    modifier = Modifier.size(if (isCompact) 32.dp else 38.dp).clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("GC", color = Color.White, fontWeight = FontWeight.Black, fontSize = if (isCompact) 11.sp else 13.sp)
                }
                Column {
                    Text(
                        "GOLDEN CITY",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.4.sp, fontSize = if (isCompact) 13.sp else 15.sp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "BEKASI • Kak Eny  •  100 HA",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.4.sp)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (!isCompact) {
                    TextButton(onClick = { openUrl(buildWhatsAppUrl()) }) {
                        Text("Lihat Tipe", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
                Button(
                    onClick = { openUrl(buildWhatsAppUrl()) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    contentPadding = PaddingValues(horizontal = if (isCompact) 14.dp else 18.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text(if (isCompact) "WA" else "WhatsApp Kak Eny", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun HeroSection(isCompact: Boolean, maxWidth: Dp, padding: Dp, onPrimaryCta: () -> Unit, onImageClick: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF0B1B30), Color(0xFF14325A), Color(0xFFC9A86A).copy(alpha = 0.85f))))
    ) {
        Box(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = if (isCompact) 20.dp else 28.dp)
        ) {
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    HeroText(isCompact = true, onPrimaryCta = onPrimaryCta)
                    HeroImage(modifier = Modifier.fillMaxWidth().height(220.dp), onImageClick = onImageClick)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1.05f)) { HeroText(isCompact = false, onPrimaryCta = onPrimaryCta) }
                    Box(Modifier.weight(0.95f)) { HeroImage(modifier = Modifier.fillMaxWidth().height(320.dp), onImageClick = onImageClick) }
                }
            }
        }
    }
}

@Composable
private fun HeroText(isCompact: Boolean, onPrimaryCta: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primary) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onPrimary)
                Text(
                    "HARGA MULAI Rp637 JT  •  KPR SIAP BANTU  •  FREE SURVEY",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, fontSize = 10.sp, letterSpacing = 0.5.sp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Text(
            "Hunian Terpadu\nGolden City\nBekasi",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Black,
                lineHeight = if (isCompact) 28.sp else 38.sp,
                fontSize = if (isCompact) 26.sp else 34.sp,
                letterSpacing = (-0.8).sp
            ),
            color = Color.White
        )
        Text(
            "Cluster Diamond • Flower Garden • Rukan Greenwood. Minimalis modern, pondasi plat beton, sanitary TOTO, Wi-Fi & CCTV kawasan. Dekat tol & stasiun.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.88f), lineHeight = 20.sp, fontSize = if (isCompact) 13.sp else 14.sp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Button(
                onClick = onPrimaryCta,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Chat Kak Eny Sekarang", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary, fontSize = 13.sp)
            }
            if (!isCompact) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.12f), border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.3f))) {
                    Text("100 ha • Bekasi Utara", modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White.copy(0.7f))
            Text(
                "Jl. Kaliabang Villa Indah Permai, Teluk Pucung, Bekasi Utara — 12 mnt Stasiun Bekasi, 10 mnt Summarecon Mall",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.7f), fontSize = 11.sp, lineHeight = 14.sp)
            )
        }
    }
}

@Composable
private fun HeroImage(modifier: Modifier, onImageClick: (String) -> Unit = {}) {
    val bannerUrl = proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Banner-Golden-City-Bekasi1-1.webp", 800)
    Box(
        modifier = modifier.clip(RoundedCornerShape(18.dp)).background(Color.White.copy(0.08f))
            .clickable { onImageClick(proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Banner-Golden-City-Bekasi1-1.webp", 1200)) }
    ) {
        AsyncImage(
            model = bannerUrl,
            contentDescription = "Tap untuk perbesar - Golden City Bekasi",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // subtle gradient overlay bottom for text legibility if needed
        Box(
            modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.25f))))
        )
    }
}

@Composable
private fun FilterSection(state: HomeUiState, onEvent: (HomeEvent) -> Unit, isCompact: Boolean) {
    var search by remember { mutableStateOf(state.searchQuery) }
    LaunchedEffect(state.searchQuery) { if (state.searchQuery != search) search = state.searchQuery }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(if (isCompact) 14.dp else 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "TIPE UNIT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
                    )
                    Text(
                        "Golden City Bekasi",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 16.sp else 18.sp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Text(
                        "${state.filteredProperties.size} unit",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                "Minimalis modern • Pondasi plat beton • Sanitary TOTO • Wi-Fi & CCTV kawasan",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            OutlinedTextField(
                value = search,
                onValueChange = { search = it; onEvent(HomeEvent.SearchQueryChanged(it)) },
                placeholder = { Text("Cari: Jade, Diamond, Ruko, 3KT...", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Cluster.entries.forEach { cluster ->
                    val selected = state.selectedCluster == cluster
                    FilterChip(
                        selected = selected,
                        onClick = { onEvent(HomeEvent.ClusterSelected(cluster)) },
                        label = {
                            Text(
                                cluster.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ),
                        border = if (selected) null else FilterChipDefaults.filterChipBorder(
                            borderColor = MaterialTheme.colorScheme.outlineVariant,
                            selectedBorderColor = MaterialTheme.colorScheme.secondary,
                            enabled = true, selected = selected
                        )
                    )
                }
                VerticalDivider(Modifier.height(20.dp).padding(horizontal = 4.dp))
                AssistChip(
                    onClick = { onEvent(HomeEvent.ToggleSort) },
                    label = { Text(if (state.sortByPriceAsc) "Termurah ↑" else "Termahal ↓", fontSize = 12.sp) },
                    shape = RoundedCornerShape(10.dp),
                    border = AssistChipDefaults.assistChipBorder(enabled = true)
                )
            }

            Text(
                "${state.filteredProperties.size} ditemukan • ${state.selectedCluster.displayName} • ${if (state.sortByPriceAsc) "termurah dulu" else "termahal dulu"}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun PropertyGrid(state: HomeUiState, isCompact: Boolean, isMedium: Boolean, onImageClick: (String) -> Unit) {
    if (state.filteredProperties.isEmpty()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                Text("Tidak ada unit di filter ini", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                Text("Coba ganti cluster atau hubungi Kak Eny via WhatsApp untuk cek ketersediaan.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(
                    onClick = { openUrl(buildWhatsAppUrl()) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Chat Kak Eny", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val columns = when {
        isCompact -> 1
        isMedium -> 2
        else -> 3
    }
    val chunked = state.filteredProperties.chunked(columns)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        chunked.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { prop ->
                    PropertyCard(
                        property = prop,
                        onWhatsAppClick = { openUrl(buildWhatsAppUrl(prop)) },
                        onImageClick = onImageClick,
                        modifier = Modifier.weight(1f),
                        compact = isCompact
                    )
                }
                // fill remaining space if row not full
                repeat(columns - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun KelebihanSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    val items = listOf(
        KelebihanItem(
            Icons.Filled.LocationOn, "Lokasi Strategis",
            "Selangkah ke tol (Pondok Gede, Bekasi Barat/Timur, Jati Asih, Kalimalang) & LRT. 12 mnt Stasiun Bekasi, 10 mnt Summarecon Mall. Dikelilingi RS, sekolah internasional & pusat kuliner."
        ),
        KelebihanItem(
            Icons.Filled.Home, "Fasilitas Lengkap",
            "100 ha terpadu: Club House, Gym, Jogging Track, Kolam Renang, Playground, Taman, Restaurant & Cafe dalam kawasan. Tanpa perlu keluar komplek."
        ),
        KelebihanItem(
            Icons.Filled.EmojiEvents, "Properti Berkualitas",
            "Award Perumahan Skala Kecil Kelas Menengah Terbaik Bekasi 2018 & The Prospective Housing Development 2018. Pondasi plat beton, struktur beton bertulang, sanitary TOTO."
        ),
        KelebihanItem(
            Icons.Filled.TrendingUp, "Nilai Investasi Tinggi",
            "Lokasi prima + bangunan premium = nilai jangka panjang potensial. Cocok hunian & investasi — 5 pilihan cluster dari Rp637 jt s/d Rp2,55 M."
        ),
    )

    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("KENAPA GOLDEN CITY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 10.sp), color = MaterialTheme.colorScheme.primary)
                    }
                }
                Text(
                    "Kelebihan Perumahan Golden City",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Bukan sekadar rumah — kawasan terpadu 100 ha untuk hidup praktis, nyaman & berkualitas.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 12.5.sp),
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            // grid 2x2 compact, 4x1 expanded
            val rows = if (isCompact) items.chunked(2) else listOf(items)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { item ->
                            KelebihanCard(item = item, modifier = Modifier.weight(1f))
                        }
                        // fill spacer if odd
                        if (row.size < (if (isCompact) 2 else 4)) {
                            repeat((if (isCompact) 2 else 4) - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }

            // CTA after kelebihan
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Shield, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White.copy(0.9f))
                            Text("Keamanan 24 Jam + CCTV Kawasan", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp))
                        }
                        Text("Tinggal aman & nyaman — akses cluster system, Wi-Fi area, taman hijau & area bermain anak.", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.75f), fontSize = 11.sp))
                    }
                    Button(
                        onClick = { openUrl(buildWhatsAppUrl()) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(6.dp))
                        Text("Tanya Kak Eny", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private data class KelebihanItem(val icon: ImageVector, val title: String, val desc: String)

@Composable
private fun KelebihanCard(item: KelebihanItem, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(item.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, fontSize = 13.sp), color = MaterialTheme.colorScheme.secondary)
            Text(item.desc, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, lineHeight = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}

@Composable
private fun FacilitiesSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        "FASILITAS & AKSES",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.2.sp, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
                    )
                    Text(
                        "100 HA Kawasan Terpadu",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 16.sp else 18.sp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (!isCompact) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color.White, shadowElevation = 1.dp) {
                        Text("SECURITY 24 JAM • CCTV KAWASAN", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }

            // facilities chips - wrap on compact using Flow-like column of rows
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        listOf("Club House", "Gym", "Jogging Track", "Kolam Renang", "Playground").forEach { FacilityChip(it) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        listOf("Restaurant", "Taman", "Security 24 Jam", "CCTV Kawasan", "Wi-Fi Area").forEach { FacilityChip(it) }
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    listOf("Club House", "Gym", "Jogging Track", "Kolam Renang", "Playground", "Restaurant", "Taman", "Security 24 Jam", "CCTV Kawasan", "Wi-Fi Area").forEach { FacilityChip(it) }
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Akses Super Strategis", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    Text(
                        "Tol Bekasi Barat 1 (18 mnt) • Tol Bekasi Timur (19 mnt) • Stasiun Bekasi (12 mnt) • LRT Bekasi Barat (17 mnt) • Summarecon Mall (10 mnt) • RS Primaya (5 mnt) • Al Wildan (6 mnt)",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityChip(label: String) {
    Surface(shape = RoundedCornerShape(10.dp), color = Color.White, shadowElevation = 1.dp, border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(0.5f))) {
        Text(label, modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp), color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun LocationSection(isCompact: Boolean, maxWidth: Dp, padding: Dp, onWhatsAppClick: () -> Unit, onImageClick: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "LOKASI STRATEGIS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.2.sp, color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
            )
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Bekasi Utara — Selangkah ke Tol & Stasiun", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, fontSize = 16.sp), color = MaterialTheme.colorScheme.secondary)
                        Text("Jl. Kaliabang Villa Indah Permai, Kelurahan Teluk Pucung, Kecamatan Bekasi Utara, Kota Bekasi, Jawa Barat.", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Button(
                            onClick = onWhatsAppClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Minta ShareLoc & Jadwal Survey", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                    AsyncImage(
                        model = proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp", 600),
                        contentDescription = "Tap untuk perbesar - Main Entrance",
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(14.dp)).clickable { onImageClick(proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp", 1200)) },
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Bekasi Utara — Selangkah ke Tol & Stasiun", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = 20.sp), color = MaterialTheme.colorScheme.secondary)
                        Text("Jl. Kaliabang Villa Indah Permai, Kelurahan Teluk Pucung, Kecamatan Bekasi Utara, Kota Bekasi, Jawa Barat.", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                        Button(
                            onClick = onWhatsAppClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Minta ShareLoc & Jadwal Survey via WhatsApp", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                    AsyncImage(
                        model = proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp", 600),
                        contentDescription = "Tap untuk perbesar - Main Entrance",
                        modifier = Modifier.weight(0.9f).height(240.dp).clip(RoundedCornerShape(14.dp)).clickable { onImageClick(proxiedImageUrl("https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp", 1200)) },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun TestimoniSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    val items = listOf(
        TestimoniData(
            "Budi Santoso", "Jade+ • Diamond Cluster",
            "Dilayani Kak Eny super ramah, survei dijemput, KPR dibantu sampai akad. Rumah sesuai foto & spek — puas banget!",
            "BS"
        ),
        TestimoniData(
            "Siti & Andi", "Ruko Standar • Rukan Greenwood",
            "Beli rukan buat usaha — lokasi hook strategis. Kak Eny kasih hitungan cicilan jelas, tanpa maksa. Proses cepat!",
            "SA"
        ),
        TestimoniData(
            "Rina Marlina", "Sapphire • Diamond Cluster",
            "Pindah dari Jakarta, anak sekolah ke Al Wildan cuma 6 menit. Fasilitas Golden City komplit, lingkungan aman nyaman.",
            "RM"
        ),
    )

    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("TESTIMONI PEMBELI • DILAYANI KAK ENY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 10.sp), color = MaterialTheme.colorScheme.primary)
                    }
                }
                Text(
                    "Cerita Mereka yang Sudah Punya Rumah di Golden City",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    "3 dari puluhan keluarga yang dibantu Kak Eny — survei, hitung KPR & akad sampai terima kunci.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 12.5.sp),
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            // cards responsive
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items.forEach { TestimoniCard(it, modifier = Modifier.fillMaxWidth()) }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items.forEach { TestimoniCard(it, modifier = Modifier.weight(1f)) }
                }
            }

            // Instagram CTA
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFDF2F8),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF9A8D4).copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFF77737)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Instagram Resmi", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.6.sp, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary))
                                Icon(Icons.Filled.Verified, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF0095F6))
                            }
                            Text("@goldencitybekasi.official", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp), color = MaterialTheme.colorScheme.secondary)
                            Text("Foto update, promo & video tour harian", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                    Button(
                        onClick = { openUrl("https://www.instagram.com/goldencitybekasi.official/") },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Buka Instagram", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // kecil disclaimer
            Text(
                "Testimoni di atas ringkasan pengalaman nyata pelanggan Kak Eny — privasi nama disamarkan inisial. Hubungi Kak Eny untuk referensi langsung.",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TestimoniCard(item: TestimoniData, modifier: Modifier = Modifier) {
    val data = item
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(5) { Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFFFC107)) }
            }
            Text("\"${data.quote}\"", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 4.dp)) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(data.initial, color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
                Column {
                    Text(data.name, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = MaterialTheme.colorScheme.secondary)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(10.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(data.unit, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private data class TestimoniData(val name: String, val unit: String, val quote: String, val initial: String)

@Composable
private fun Footer(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF0E1A2B))) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(28.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                    Text("GC", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Black, fontSize = 10.sp)
                }
                Text("Eny • Marketing Golden City Bekasi", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp))
            }
            Text(
                "Hubungi langsung via WhatsApp untuk promo, simulasi KPR & booking unit. Foto & harga sampel dari golden-city-bekasi.com — hubungi Kak Eny untuk update stok terbaru.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.68f), fontSize = if (isCompact) 11.sp else 12.sp, lineHeight = 16.sp),
                modifier = Modifier.widthIn(max = 720.dp)
            )
            Text("© 2026 EnyGoldenCity — Bekasi Utara  •  WA 0812-8040-4180", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.45f), fontSize = 11.sp))
        }
    }
}

@Composable
private fun FloatingWhatsApp(modifier: Modifier = Modifier, compact: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
        contentPadding = PaddingValues(horizontal = if (compact) 16.dp else 20.dp, vertical = if (compact) 10.dp else 12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 8.dp)
    ) {
        Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
        Spacer(Modifier.width(6.dp))
        Text(if (compact) "WA" else "WA Kak Eny", color = Color.White, fontWeight = FontWeight.Black, fontSize = if (compact) 12.sp else 13.sp)
    }
}

@Composable
private fun ImageLightbox(imageUrl: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.82f)).clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // image container - click on image itself should not dismiss via propagation, so wrap
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black,
                modifier = Modifier.widthIn(max = 900.dp).fillMaxWidth().wrapContentHeight()
            ) {
                Box {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Preview",
                        modifier = Modifier.fillMaxWidth().heightIn(max = 700.dp),
                        contentScale = ContentScale.Fit
                    )
                    // close button top-end
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(Color.Black.copy(0.6f), RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Tutup", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
        // hint bottom
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(0.6f),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
        ) {
            Text("Tap di luar gambar untuk tutup", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.9f), fontSize = 11.sp))
        }
    }
}
