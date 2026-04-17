package com.bintang.newsreader.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bintang.newsreader.data.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    viewModel: NewsViewModel,
    articleId: Int,
    onBack: () -> Unit
) {
    val articlesState by viewModel.articlesState.collectAsState()

    // Cari artikel dari state yang sudah ada
    val article: Article? = (articlesState as? UiState.Success)
        ?.data
        ?.find { it.id == articleId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Artikel") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (article == null) {
            // Artikel tidak ditemukan
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Artikel tidak ditemukan")
            }
        } else {
            // Konten detail artikel
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Gambar header
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.formattedTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Badge user
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "✍️ Ditulis oleh User ${article.userId}  •  Artikel #${article.id}",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    // Judul
                    Text(
                        text = article.formattedTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider()

                    // Isi konten
                    Text(
                        text = "📄 Isi Artikel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Tampilkan body teks
                    Text(
                        text = article.body,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Extended content (simulasi artikel panjang)
                    Text(
                        text = buildString {
                            append("Artikel ini membahas topik penting yang relevan dengan kehidupan sehari-hari. ")
                            append("Dalam pembahasan lebih lanjut, kita perlu memahami konteks dan latar belakang ")
                            append("yang melatarbelakangi isu ini.\n\n")
                            append("Selain itu, para ahli di bidang ini menyatakan bahwa pemahaman mendalam ")
                            append("sangat diperlukan untuk bisa mengambil keputusan yang tepat. ")
                            append("Berbagai penelitian telah membuktikan bahwa isu ini memiliki dampak ")
                            append("yang signifikan terhadap masyarakat luas.\n\n")
                            append("Oleh karena itu, sangat penting bagi kita untuk terus mengikuti ")
                            append("perkembangan berita dan informasi terkini agar dapat memahami ")
                            append("dinamika yang terjadi di sekitar kita.")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol kembali di bawah
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("← Kembali ke Daftar Berita")
                    }
                }
            }
        }
    }
}
