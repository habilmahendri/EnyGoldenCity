package com.enygoldencity.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import org.jetbrains.compose.resources.painterResource
import enygoldencity.shared.generated.resources.Res
import enygoldencity.shared.generated.resources.eny_profile
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
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

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
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
                //TopBar(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                HeroSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding,
                    onPrimaryCta = { openUrl(buildWhatsAppUrl()) },
                    onImageClick = { lightboxUrl = it }
                )
                // centered content
                Column(
                    modifier = Modifier.widthIn(max = contentMaxWidth).fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Spacer(Modifier.height(20.dp))
                    FilterSection(
                        state = state,
                        onEvent = viewModel::onEvent,
                        isCompact = isCompact
                    )
                    Spacer(Modifier.height(12.dp))
                    PropertyGrid(
                        state = state,
                        isCompact = isCompact,
                        isMedium = isMedium,
                        onImageClick = { lightboxUrl = it })
                    Spacer(Modifier.height(8.dp))
                }
                KelebihanSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding
                )
                FacilitiesSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding
                )
                LocationSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding,
                    onWhatsAppClick = { openUrl(buildWhatsAppUrl()) },
                    onImageClick = { lightboxUrl = it }
                )
                TestimoniSection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding
                )
                AboutEnySection(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding
                )
                PromoSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                MapSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding, onImageClick = { lightboxUrl = it })
                BankSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                LegalitasSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                FaqSection(isCompact = isCompact, maxWidth = contentMaxWidth, padding = horizontalPadding)
                Footer(
                    isCompact = isCompact,
                    maxWidth = contentMaxWidth,
                    padding = horizontalPadding
                )
                Spacer(Modifier.height(88.dp))
            }
            FloatingWhatsApp(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(if (isCompact) 14.dp else 20.dp),
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
    // Clean elegant floating header — promo pill dihapus sesuai request
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        // Main nav — floating card clean (tanpa promo pill atas)
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = padding, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                ),
                modifier = Modifier.widthIn(max = maxWidth).fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(
                        horizontal = if (isCompact) 12.dp else 16.dp,
                        vertical = if (isCompact) 10.dp else 12.dp
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(if (isCompact) 34.dp else 40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFFFDF6D8),
                                            Color(0xFFC9A86A)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "GC",
                                color = Color(0xFF0E1A2B),
                                fontWeight = FontWeight.Black,
                                fontSize = if (isCompact) 12.sp else 13.sp,
                                letterSpacing = 0.6.sp
                            )
                        }
                        Column {
                            Text(
                                "Golden City",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.2.sp,
                                    fontSize = if (isCompact) 14.sp else 16.sp
                                ),
                                color = Color(0xFF0E1A2B)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    "Bekasi Utara",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(
                                            0xFF8A7A5A
                                        ),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.6.sp
                                    )
                                )
                                Box(
                                    Modifier.size(2.dp).clip(RoundedCornerShape(50))
                                        .background(Color(0xFFC9A86A))
                                )
                                Text(
                                    "Kak Eny",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(
                                            0xFF0E1A2B
                                        ), fontWeight = FontWeight.Bold, fontSize = 10.sp
                                    )
                                )
                                if (!isCompact) {
                                    Box(
                                        Modifier.size(2.dp).clip(RoundedCornerShape(50))
                                            .background(Color(0xFFC9A86A))
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isCompact) {
                            TextButton(onClick = { openUrl(buildWhatsAppUrl(customMessage = "Halo Kak Eny, mau cek unit ready Golden City")) }) {
                                Text(
                                    "Unit Ready",
                                    color = Color(0xFF0E1A2B).copy(0.7f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color(0xFF25D366),
                            shadowElevation = 1.dp,
                            modifier = Modifier.clickable { openUrl(buildWhatsAppUrl()) }
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = if (isCompact) 14.dp else 16.dp,
                                    vertical = 8.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.White
                                )
                                Text(
                                    if (isCompact) "Chat Kak Eny" else "Chat Kak Eny",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroSection(
    isCompact: Boolean,
    maxWidth: Dp,
    padding: Dp,
    onPrimaryCta: () -> Unit,
    onImageClick: (String) -> Unit
) {
    // Clean elegant — light cream boutique, single image natural (revert dari split yang aneh)
    Box(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFFEFCF6))
    ) {
        // subtle gold blur
        Box(
            modifier = Modifier.size(if (isCompact) 220.dp else 380.dp)
                .align(Alignment.TopEnd)
                .offset(x = if (isCompact) 60.dp else 80.dp, y = (-40).dp)
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFFC9A86A).copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = if (isCompact) 18.dp else 26.dp)
        ) {
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    HeroText(isCompact = true, onPrimaryCta = onPrimaryCta)
                    HeroSlider(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        onImageClick = onImageClick,
                        isCompact = true
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        HeroText(
                            isCompact = false,
                            onPrimaryCta = onPrimaryCta
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        HeroSlider(
                            modifier = Modifier.fillMaxWidth().height(380.dp),
                            onImageClick = onImageClick,
                            isCompact = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroText(isCompact: Boolean, onPrimaryCta: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Clean labels — soft pill, not rigid block
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFE8F5E9),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        Modifier.size(6.dp).clip(RoundedCornerShape(50))
                            .background(Color(0xFF2E7D32))
                    )
                    Text(
                        "MODERN GREEN LIVING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp,
                            letterSpacing = 0.7.sp
                        ),
                        color = Color(0xFF2E7D32)
                    )
                }
            }
            Text(
                "BEKASI UTARA",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF8A7A5A)
                )
            )
        }
        // soft promo pill gold
        Surface(shape = RoundedCornerShape(50), color = Color(0xFF0E1A2B)) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(11.dp),
                    tint = Color(0xFFC9A86A)
                )
                Text(
                    "4 Cluster  •  DP 5 jt  •  Booking Fee 5 jt  •  Free BPHTB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.5.sp,
                        letterSpacing = 0.4.sp
                    ),
                    color = Color(0xFFFDF6D8)
                )
            }
        }
        // Headline — dark elegant, not white on dark
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Golden City",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = if (isCompact) 13.sp else 14.sp,
                    letterSpacing = 3.sp,
                    color = Color(0xFF8A7A5A)
                )
            )
            Text(
                "Perumahan di\nBekasi Utara",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    lineHeight = if (isCompact) 30.sp else 38.sp,
                    fontSize = if (isCompact) 28.sp else 36.sp,
                    letterSpacing = (-1.2).sp
                ),
                color = Color(0xFF0E1A2B)
            )
            Box(
                Modifier.width(48.dp).height(3.dp).clip(RoundedCornerShape(50))
                    .background(Color(0xFFC9A86A))
            )
        }
        Text(
            "Kombinasi presisi desain arsitektural, keteduhan cluster hijau & kepraktisan selangkah dari KFC • Starbucks • Pizza Hut. One Gate System, Security 24 Jam — hunian terpadu 100 Ha.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF5A4A2F).copy(0.85f),
                lineHeight = 20.sp,
                fontSize = if (isCompact) 13.sp else 14.sp
            ),
        )
        // stats — soft white cards, not dark glass
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCardLight("250+", "Terjual", Icons.Filled.Star, Modifier.weight(1f))
            StatCardLight("100 Ha", "Kawasan", Icons.Filled.Home, Modifier.weight(1f))
            StatCardLight("98%", "Puas", Icons.Filled.Favorite, Modifier.weight(1f))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color(0xFFC9A86A)
            )
            Text(
                "Jl. Kaliabang Villa Indah Permai, Teluk Pucung — 3 mnt RS Primaya, 10 mnt Summarecon",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF8A7A5A),
                    fontSize = 10.5.sp,
                    lineHeight = 13.sp
                )
            )
        }
    }
}

@Composable
private fun StatCardLight(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFEF3E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color(0xFFC9A86A)
                )
            }
            Column {
                Text(
                    value,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF0E1A2B)
                )
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        letterSpacing = 0.4.sp,
                        color = Color(0xFF8A7A5A)
                    )
                )
            }
        }
    }
}

@Composable
private fun StatMini(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            ),
            color = Color.White
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                letterSpacing = 0.3.sp,
                color = Color.White.copy(0.7f)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeroSlider(modifier: Modifier, onImageClick: (String) -> Unit, isCompact: Boolean) {
    // 5 slides — nama + harga start from (pilihan terlaris per cluster)
    data class Slide(val name: String, val cluster: String, val price: String, val image: String)

    val slides = remember {
        listOf(
            Slide(
                "Dahlia",
                "Gardenia",
                "Mulai Rp1,12 M",
                "https://goldencitybekasi.net/wp-content/uploads/2026/06/image3-scaled.jpeg"
            ),
            Slide(
                "Allamanda",
                "Gardenia",
                "Mulai Rp899 jt",
                "https://goldencitybekasi.net/wp-content/uploads/2026/06/image3-scaled.jpeg"
            ),
            Slide(
                "Topaz",
                "Diamond",
                "Mulai Rp830 jt",
                "https://goldencitybekasi.net/wp-content/uploads/2026/06/type-jade-cluster-diamond-scaled.jpg"
            ),
            Slide(
                "Cedar",
                "Greenwood",
                "Mulai Rp931 jt",
                "https://goldencitybekasi.net/wp-content/uploads/2026/06/type-cedar-cluster-greenwood-scaled.jpg"
            ),
            Slide(
                "Ruko Gardenia Hoek",
                "Ruko • 5,5×10",
                "Mulai Rp1,39 M",
                "https://goldencitybekasi.net/wp-content/uploads/2026/07/Pi7_image_tool-4.jpeg"
            ),
        )
    }
    var index by remember { mutableIntStateOf(0) }
    // autoplay pelan 5.5s biar nggak cepat-cepat
    LaunchedEffect(index) {
        kotlinx.coroutines.delay(5500)
        index = (index + 1) % slides.size
    }
    val slide = slides[index]
    // Card style — rounded, shadow, clean
    Box(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxSize()
                .clickable { onImageClick(proxiedImageUrl(slide.image, 1200)) }
        ) {
            Box(Modifier.fillMaxSize()) {
                // simple fade animasi pas auto slide — Animatable 0→1 tiap ganti slide
                val imgAlpha = remember(slide) { Animatable(0f) }
                LaunchedEffect(slide) { imgAlpha.animateTo(1f, tween(500)) }
                Box(Modifier.fillMaxSize().alpha(imgAlpha.value)) {
                    AsyncImage(
                        model = proxiedImageUrl(slide.image, 800),
                        contentDescription = "${slide.name} — ${slide.price}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().height(72.dp).align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(0.68f)
                                )
                            )
                        )
                )
                val textAlpha = remember(slide) { Animatable(0f) }
                LaunchedEffect(slide) {
                    kotlinx.coroutines.delay(80); textAlpha.animateTo(
                    1f,
                    tween(400)
                )
                }
                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(14.dp)
                        .alpha(textAlpha.value),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(0.92f)) {
                        Text(
                            slide.cluster.uppercase(),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 8.5.sp,
                                letterSpacing = 0.6.sp
                            ),
                            color = Color(0xFF0E1A2B)
                        )
                    }
                    Text(
                        slide.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                    Text(
                        slide.price,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFFDF6D8)
                        )
                    )
                }
                // dots only (arrow dihapus)
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    slides.forEachIndexed { i, _ ->
                        val isActive = i == index
                        Box(
                            Modifier.size(if (isActive) 16.dp else 6.dp, 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isActive) Color.White else Color.White.copy(0.45f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(state: HomeUiState, onEvent: (HomeEvent) -> Unit, isCompact: Boolean) {
    var search by remember { mutableStateOf(state.searchQuery) }
    LaunchedEffect(state.searchQuery) {
        if (state.searchQuery != search) search = state.searchQuery
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
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
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        "Golden City Bekasi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = if (isCompact) 16.sp else 18.sp
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        "${state.filteredProperties.size} unit",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                "Minimalis modern • Pondasi plat beton • Sanitary TOTO • Wi-Fi & CCTV kawasan",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            OutlinedTextField(
                value = search,
                onValueChange = { search = it; onEvent(HomeEvent.SearchQueryChanged(it)) },
                placeholder = { Text("Cari: Jade, Diamond, Ruko, 3KT...", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
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
private fun PropertyGrid(
    state: HomeUiState,
    isCompact: Boolean,
    isMedium: Boolean,
    onImageClick: (String) -> Unit
) {
    if (state.filteredProperties.isEmpty()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Tidak ada unit di filter ini",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    "Coba ganti cluster atau hubungi Kak Eny via WhatsApp untuk cek ketersediaan.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = { openUrl(buildWhatsAppUrl()) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        Icons.Filled.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
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
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "KENAPA GOLDEN CITY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    "Kelebihan Perumahan Golden City",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (isCompact) 18.sp else 22.sp
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Bukan sekadar rumah — kawasan terpadu 100 ha untuk hidup praktis, nyaman & berkualitas.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontSize = 12.5.sp
                    ),
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            // grid 2x2 compact, 4x1 expanded
            val rows = if (isCompact) items.chunked(2) else listOf(items)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Filled.Shield,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White.copy(0.9f)
                            )
                            Text(
                                "Keamanan 24 Jam + CCTV Kawasan",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Text(
                            "Tinggal aman & nyaman — akses cluster system, Wi-Fi area, taman hijau & area bermain anak.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(0.75f), fontSize = 11.sp
                            )
                        )
                    }
                    Button(
                        onClick = { openUrl(buildWhatsAppUrl()) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Tanya Kak Eny",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
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
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                item.desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun FacilitiesSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    data class Facility(val title: String, val subtitle: String, val image: String)

    val facilities = listOf(
        Facility(
            "Private Swimming Pool",
            "Dalam Kawasan",
            "https://goldencitybekasi.net/wp-content/uploads/2025/03/2.png"
        ),
        Facility(
            "Community Garden & Jogging Track",
            "Outdoor",
            "https://goldencitybekasi.net/wp-content/uploads/2025/03/1.png"
        ),
        Facility(
            "One Gate System",
            "Security 24 Jam",
            "https://goldencitybekasi.net/wp-content/uploads/2025/03/4.png"
        ),
        Facility(
            "Sports Club",
            "Fitness Dalam Kawasan",
            "https://goldencitybekasi.net/wp-content/uploads/2025/03/3.png"
        ),
    )
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "FASILITAS & AKSES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        "100 HA Kawasan Terpadu",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = if (isCompact) 16.sp else 18.sp
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (!isCompact) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Text(
                            "SECURITY 24 JAM • CCTV KAWASAN",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Facilities grid with visuals — 2x2 compact, 4 col desktop
            val rows = if (isCompact) facilities.chunked(2) else listOf(facilities)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { f ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                shadowElevation = 1.dp,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(0.4f)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column {
                                    AsyncImage(
                                        model = proxiedImageUrl(f.image, 400),
                                        contentDescription = f.title,
                                        modifier = Modifier.fillMaxWidth().height(120.dp).clip(
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp
                                            )
                                        ),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            f.subtitle.uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.6.sp,
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                        Text(
                                            f.title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            ),
                                            color = MaterialTheme.colorScheme.secondary,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                        if (row.size < (if (isCompact) 2 else 4)) {
                            repeat((if (isCompact) 2 else 4) - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
            // chips tambahan
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                listOf(
                    "Club House",
                    "Playground",
                    "Restaurant",
                    "Taman",
                    "Wi-Fi Area",
                    "CCTV Kawasan"
                ).forEach { FacilityChip(it) }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Akses Super Strategis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "Tol Bekasi Barat 1 (18 mnt) • Tol Bekasi Timur (19 mnt) • Stasiun Bekasi (12 mnt) • LRT Bekasi Barat (17 mnt) • Summarecon Mall (10 mnt) • RS Primaya (5 mnt) • Al Wildan (6 mnt)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityChip(label: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(0.5f)
        )
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun LocationSection(
    isCompact: Boolean,
    maxWidth: Dp,
    padding: Dp,
    onWhatsAppClick: () -> Unit,
    onImageClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "LOKASI STRATEGIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp
                )
            )
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Bekasi Utara — Selangkah ke Tol & Stasiun",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Jl. Kaliabang Villa Indah Permai, Kelurahan Teluk Pucung, Kecamatan Bekasi Utara, Kota Bekasi, Jawa Barat.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Button(
                            onClick = onWhatsAppClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Minta ShareLoc & Jadwal Survey",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    AsyncImage(
                        model = proxiedImageUrl(
                            "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp",
                            600
                        ),
                        contentDescription = "Tap untuk perbesar - Main Entrance",
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                            .clip(RoundedCornerShape(14.dp)).clickable {
                            onImageClick(
                                proxiedImageUrl(
                                    "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp",
                                    1200
                                )
                            )
                        },
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Bekasi Utara — Selangkah ke Tol & Stasiun",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            "Jl. Kaliabang Villa Indah Permai, Kelurahan Teluk Pucung, Kecamatan Bekasi Utara, Kota Bekasi, Jawa Barat.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Button(
                            onClick = onWhatsAppClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Minta ShareLoc & Jadwal Survey via WhatsApp",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    AsyncImage(
                        model = proxiedImageUrl(
                            "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp",
                            600
                        ),
                        contentDescription = "Tap untuk perbesar - Main Entrance",
                        modifier = Modifier.weight(0.9f).height(240.dp)
                            .clip(RoundedCornerShape(14.dp)).clickable {
                            onImageClick(
                                proxiedImageUrl(
                                    "https://www.golden-city-bekasi.com/app/uploads/sites/26/2023/08/Main-Entrance.webp",
                                    1200
                                )
                            )
                        },
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
            "BS", "Jade+ • Diamond Cluster",
            "Dilayani Kak Eny super ramah, survei dijemput, KPR dibantu sampai akad. Rumah sesuai foto & spek — puas banget!",
            "BS"
        ),
        TestimoniData(
            "SA", "Ruko Standar • Rukan Greenwood",
            "Beli rukan buat usaha — lokasi hook strategis. Kak Eny kasih hitungan cicilan jelas, tanpa maksa. Proses cepat!",
            "SA"
        ),
        TestimoniData(
            "RM", "Sapphire • Diamond Cluster",
            "Pindah dari Jakarta, anak sekolah ke Al Wildan cuma 6 menit. Fasilitas Golden City komplit, lingkungan aman nyaman.",
            "RM"
        ),
    )

    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "TESTIMONI PEMBELI • DILAYANI KAK ENY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    "Cerita Mereka yang Sudah Punya Rumah di Golden City",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (isCompact) 18.sp else 22.sp
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    "3 dari puluhan keluarga yang dibantu Kak Eny — survei, hitung KPR & akad sampai terima kunci.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontSize = 12.5.sp
                    ),
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            // cards responsive
            if (isCompact) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items.forEach { TestimoniCard(it, modifier = Modifier.fillMaxWidth()) }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items.forEach { TestimoniCard(it, modifier = Modifier.weight(1f)) }
                }
            }

            // disclaimer — di atas Instagram, di bawah list testimoni (biar nggak nabrak)
            Text(
                "Testimoni di atas ringkasan pengalaman nyata pelanggan Kak Eny — privasi nama disamarkan inisial. Hubungi Kak Eny untuk referensi langsung.",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
            )

            // Instagram CTA — responsive (HP: stack vertical biar nggak nabrak)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFDF2F8),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color(0xFFF9A8D4).copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isCompact) {
                    Column(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF833AB4),
                                                Color(0xFFE1306C),
                                                Color(0xFFF77737)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "Instagram Resmi",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.6.sp,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Icon(
                                        Icons.Filled.Verified,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color(0xFF0095F6)
                                    )
                                }
                                Text(
                                    "@goldencitybekasi.official",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 1
                                )
                                Text(
                                    "Foto update, promo & video tour harian",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                        Button(
                            onClick = { openUrl("https://www.instagram.com/goldencitybekasi.official/") },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Filled.Share,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Buka Instagram",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF833AB4),
                                                Color(0xFFE1306C),
                                                Color(0xFFF77737)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "Instagram Resmi",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.6.sp,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Icon(
                                        Icons.Filled.Verified,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color(0xFF0095F6)
                                    )
                                }
                                Text(
                                    "@goldencitybekasi.official",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    "Foto update, promo & video tour harian",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutEnySection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFEFCF6))) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // header
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFF0E1A2B)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFC9A86A)
                        )
                        Text(
                            "SEJAK 2014 • 12 TAHUN",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp,
                                fontSize = 9.5.sp
                            ),
                            color = Color.White
                        )
                    }
                }
                Text(
                    "Kenalan dengan Kak Eny",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (isCompact) 22.sp else 26.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color(0xFF0E1A2B),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Marketing Golden City Bekasi — bantu puluhan keluarga sejak 2014 menemukan rumah impian.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF5A4A2F).copy(
                            0.8f
                        ), textAlign = TextAlign.Center, fontSize = 13.sp, lineHeight = 18.sp
                    ),
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            // main card — foto + story
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isCompact) {
                    Column {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(220.dp)
                                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.eny_profile),
                                contentDescription = "Kak Eny — Golden City",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.TopCenter
                            )
                        }
                        AboutEnyContent(isCompact = true)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(0.8f).height(300.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFF0EBDC), RoundedCornerShape(16.dp))
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.eny_profile),
                                contentDescription = "Kak Eny — Golden City",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.TopCenter
                            )
                        }
                        Box(Modifier.weight(1.2f)) {
                            AboutEnyContent(isCompact = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutEnyContent(isCompact: Boolean) {
    Column(
        modifier = Modifier.padding(if (isCompact) 16.dp else 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Halo, saya Eny — sejak 2014 mendampingi keluarga menemukan rumah impian di Golden City Bekasi. Saya percaya rumah bukan sekadar bangunan, tapi tempat tumbuhnya cerita keluarga.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Color(0xFF2F2F2F),
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            "Saya dampingi dari survei lokasi, konsultasi budget, simulasi KPR, sampai akad & serah terima kunci — dengan penjelasan yang jujur dan tanpa paksaan. Yang penting Anda nyaman dan yakin dengan pilihan.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
                color = Color(0xFF5A4A2F)
            )
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AboutBullet(
                Icons.Filled.Work,
                "Dampingi Sampai Akad",
                "Bantu urus berkas & konsultasi KPR BTN/Permata dengan transparan."
            )
            AboutBullet(
                Icons.Filled.Shield,
                "Respon Cepat & Jujur",
                "WA 0812-8040-4180 — respon cepat, info stok real-time, survei fleksibel."
            )
        }
    }
}

@Composable
private fun AboutBullet(icon: ImageVector, title: String, desc: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
        Box(
            Modifier.size(28.dp).clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = Color(0xFF0E1A2B)
            )
            Text(
                desc,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = Color(0xFF5A4A2F)
                )
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
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(5) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFFFC107)
                    )
                }
            }
            Text(
                "\"${data.quote}\"",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.5.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        data.initial,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }
                Column {
                    Text(
                        data.name,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            data.unit,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private data class TestimoniData(
    val name: String,
    val unit: String,
    val quote: String,
    val initial: String
)

@Composable
private fun PromoSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFFFEF3E2), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC))) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.LocalOffer, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFFC9A86A))
                        Text("PROMO & CARA BAYAR", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 9.5.sp), color = Color(0xFF8A7A5A))
                    }
                }
                Text("Promo Spesial Golden City", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp), color = Color(0xFF0E1A2B), textAlign = TextAlign.Center)
                Text("DP ringan, booking fee kecil, free BPHTB & subsidi KPR — cash atau KPR semua bisa.", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5A4A2F), textAlign = TextAlign.Center, fontSize = 12.5.sp), modifier = Modifier.widthIn(max = 600.dp))
            }
            val promos = listOf(
                Triple(Icons.Filled.Home, "DP Rumah 5 Jt", "Booking Fee 5 Jt"),
                Triple(Icons.Filled.Work, "DP Ruko 20 Jt", "Cash / KPR"),
                Triple(Icons.Filled.LocalOffer, "Free BPHTB", "Maksimal • S&K"),
                Triple(Icons.Filled.Star, "Subsidi KPR", "Bunga ringan")
            )
            val rows = if (isCompact) promos.chunked(2) else listOf(promos)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { (icon, title, sub) ->
                            Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFFEFCF6), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.weight(1f)) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color.White).border(1.dp, Color(0xFFF0EBDC), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFFC9A86A))
                                    }
                                    Column {
                                        Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = Color(0xFF0E1A2B))
                                        Text(sub, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF8A7A5A)))
                                    }
                                }
                            }
                        }
                        if (row.size < (if (isCompact) 2 else 4)) repeat((if (isCompact) 2 else 4) - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF0E1A2B), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Siap booking? DP 5jt langsung proses", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp))
                        Text("Kak Eny bantu hitung angsuran & cek unit ready", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.7f), fontSize = 11.sp))
                    }
                    Button(onClick = { openUrl(buildWhatsAppUrl(customMessage = "Halo Kak Eny, mau tanya promo DP 5jt Golden City")) }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC9A86A)), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                        Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF0E1A2B))
                        Spacer(Modifier.width(6.dp))
                        Text("Klaim Promo", color = Color(0xFF0E1A2B), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MapSection(isCompact: Boolean, maxWidth: Dp, padding: Dp, onImageClick: (String) -> Unit) {
    val mapImage = "https://goldencitybekasi.net/wp-content/uploads/2026/07/WhatsApp-Image-2026-07-09-at-11.24.51-1-1024x780.jpeg"
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFDFDFD))) {
        Column(modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFFE8F5E9), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9))) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF2E7D32))
                        Text("LOKASI", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 9.5.sp), color = Color(0xFF2E7D32))
                    }
                }
                Text("Peta Lokasi Golden City", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp), color = Color(0xFF0E1A2B), textAlign = TextAlign.Center)
                Text("Jl. Kaliabang Villa Indah Permai, Teluk Pucung, Bekasi Utara — tap peta untuk navigasi", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5A4A2F), textAlign = TextAlign.Center, fontSize = 12.5.sp), modifier = Modifier.widthIn(max = 640.dp))
            }
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 3.dp, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.fillMaxWidth().clickable { openUrl("https://www.google.com/maps/search/?api=1&query=Golden+City+Bekasi+Kaliabang") }) {
                Box(modifier = Modifier.fillMaxWidth().height(if (isCompact) 220.dp else 280.dp)) {
                    AsyncImage(model = proxiedImageUrl(mapImage, 800), contentDescription = "Peta Golden City", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Surface(modifier = Modifier.align(Alignment.Center), shape = RoundedCornerShape(50), color = Color.White, shadowElevation = 4.dp) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFD32F2F))
                            Text("Buka di Google Maps", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = Color(0xFF0E1A2B))
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("3 mnt RS Primaya", "10 mnt Summarecon", "12 mnt Stasiun Bekasi").forEach {
                    Surface(shape = RoundedCornerShape(50), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.weight(1f)) {
                        Text(it, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium, fontSize = 10.sp, color = Color(0xFF0E1A2B)), textAlign = TextAlign.Center, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun BankSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = RoundedCornerShape(50), color = Color(0xFFE8EEF6), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBDEFB))) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.AccountBalance, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF0E1A2B))
                    Text("KERJASAMA BANK", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 9.5.sp), color = Color(0xFF0E1A2B))
                }
            }
            Text("Mitra Pembiayaan KPR", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp), color = Color(0xFF0E1A2B), textAlign = TextAlign.Center)
            Text("Proses didampingi Kak Eny dari berkas sampai akad — aman & transparan", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5A4A2F), textAlign = TextAlign.Center, fontSize = 12.5.sp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AsyncImage(model = proxiedImageUrl("https://goldencitybekasi.net/wp-content/uploads/2026/08/65e4402c20cbf.jpeg", 400), contentDescription = "Bank BTN", modifier = Modifier.height(48.dp).fillMaxWidth(), contentScale = ContentScale.Fit)
                        Text("Bank BTN", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = Color(0xFF0E1A2B))
                        Text("KPR bunga kompetitif, proses mudah", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, color = Color(0xFF5A4A2F), textAlign = TextAlign.Center))
                    }
                }
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AsyncImage(model = proxiedImageUrl("https://goldencitybekasi.net/wp-content/uploads/2026/08/images-5.jpg", 400), contentDescription = "Bank Permata", modifier = Modifier.height(48.dp).fillMaxWidth(), contentScale = ContentScale.Fit)
                        Text("Bank Permata", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = Color(0xFF0E1A2B))
                        Text("Skema fleksibel, konsultasi gratis", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, color = Color(0xFF5A4A2F), textAlign = TextAlign.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun LegalitasSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFEFCF6))) {
        Column(modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = RoundedCornerShape(50), color = Color(0xFFE8F5E9), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9))) {
                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Gavel, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF2E7D32))
                    Text("LEGALITAS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 9.5.sp), color = Color(0xFF2E7D32))
                }
            }
            Text("Aman & Terpercaya", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp), color = Color(0xFF0E1A2B), textAlign = TextAlign.Center)
            val legals = listOf(
                Triple(Icons.Filled.Gavel, "SHM", "Sertifikat Hak Milik"),
                Triple(Icons.Filled.Home, "IMB", "Izin Mendirikan Bangunan"),
                Triple(Icons.Filled.Shield, "One Gate", "Security 24 Jam"),
                Triple(Icons.Filled.Verified, "GNA Group", "Developer Terpercaya")
            )
            val rows = if (isCompact) legals.chunked(2) else listOf(legals)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { (icon, title, sub) ->
                            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EBDC)), modifier = Modifier.weight(1f)) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                                        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF2E7D32))
                                    }
                                    Column {
                                        Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = Color(0xFF0E1A2B))
                                        Text(sub, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF5A4A2F)))
                                    }
                                }
                            }
                        }
                        if (row.size < (if (isCompact) 2 else 4)) repeat((if (isCompact) 2 else 4) - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqSection(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    val faqs = remember {
        listOf(
            "Berapa DP & booking fee?" to "DP Rumah 5 Jt, DP Ruko 20 Jt, Booking Fee 5 Jt. Promo Free BPHTB & subsidi KPR (S&K). Kak Eny bantu hitung angsuran sesuai budget.",
            "Bisa KPR? Bank apa?" to "Bisa — kerjasama Bank BTN & Permata. Bunga kompetitif, proses didampingi dari berkas sampai akad, transparan.",
            "Booking fee hangus?" to "Booking fee tanda jadi — jika KPR ditolak bank, ada kebijakan pengembalian sesuai S&K developer. Tanya Kak Eny untuk detail.",
            "Kapan bisa survei?" to "Survei fleksibel — weekday/weekend. Hubungi WA 0812-8040-4180, Kak Eny jemput & dampingi lihat unit ready & denah langsung.",
            "Lokasi persis di mana?" to "Jl. Kaliabang Villa Indah Permai, Teluk Pucung, Bekasi Utara — 3 mnt RS Primaya, 10 mnt Summarecon Bekasi, 12 mnt Stasiun Bekasi."
        )
    }
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Column(modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center).padding(horizontal = padding, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(shape = RoundedCornerShape(50), color = Color(0xFFF3E8FF), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF))) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Help, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF7C3AED))
                        Text("FAQ", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 0.8.sp, fontSize = 9.5.sp), color = Color(0xFF7C3AED))
                    }
                }
                Text("Pertanyaan Sering Diajukan", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = if (isCompact) 18.sp else 22.sp), color = Color(0xFF0E1A2B), textAlign = TextAlign.Center)
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                faqs.forEach { (q, a) ->
                    var expanded by remember { mutableStateOf(false) }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (expanded) Color(0xFFFDF6FF) else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (expanded) Color(0xFFE9D5FF) else Color(0xFFF0EBDC)),
                        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(q, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.5.sp), color = Color(0xFF0E1A2B), modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xFF7C3AED)
                                )
                            }
                            if (expanded) {
                                Text(a, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp, color = Color(0xFF5A4A2F)))
                                TextButton(onClick = { openUrl(buildWhatsAppUrl(customMessage = "Halo Kak Eny, mau tanya: $q")) }, contentPadding = PaddingValues(0.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("Tanya Kak Eny", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF7C3AED), fontSize = 11.sp))
                                        Icon(Icons.Filled.ChevronRight, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF7C3AED))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Footer(isCompact: Boolean, maxWidth: Dp, padding: Dp) {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF0E1A2B))) {
        Column(
            modifier = Modifier.widthIn(max = maxWidth).align(Alignment.Center)
                .padding(horizontal = padding, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(28.dp).clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "GC",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    )
                }
                Text(
                    "Eny • Marketing Golden City Bekasi",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                )
            }
            Text(
                "Hubungi langsung via WhatsApp untuk promo, simulasi KPR & booking unit. Foto & harga sampel dari golden-city-bekasi.com — hubungi Kak Eny untuk update stok terbaru.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(0.68f),
                    fontSize = if (isCompact) 11.sp else 12.sp,
                    lineHeight = 16.sp
                ),
                modifier = Modifier.widthIn(max = 720.dp)
            )
            Text(
                "© 2026 EnyGoldenCity — Bekasi Utara  •  WA 0812-8040-4180",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(0.45f),
                    fontSize = 11.sp
                )
            )
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
        contentPadding = PaddingValues(
            horizontal = if (compact) 16.dp else 20.dp,
            vertical = if (compact) 10.dp else 12.dp
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 8.dp)
    ) {
        Icon(
            Icons.Filled.Call,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
        Spacer(Modifier.width(6.dp))
        Text(
            if (compact) "WA" else "WA Kak Eny",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = if (compact) 12.sp else 13.sp
        )
    }
}

@Composable
private fun ImageLightbox(imageUrl: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.82f))
            .clickable { onDismiss() },
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
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                            .background(Color.Black.copy(0.6f), RoundedCornerShape(50))
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Tutup",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
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
            Text(
                "Tap di luar gambar untuk tutup",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(0.9f),
                    fontSize = 11.sp
                )
            )
        }
    }
}
