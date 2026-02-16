package manager

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.News
import repository.NewsRepository

@OptIn(ExperimentalCoroutinesApi::class)
class NewsManager(
    private val repo: NewsRepository,
    private val scope: CoroutineScope
) {
    /**
     * State Management menggunakan MutableStateFlow untuk menyimpan status aplikasi.
     * _selectedCategory: Menyimpan kategori aktif yang dipilih pengguna.
     * _readCount: Menyimpan jumlah total berita yang telah dibaca secara detail.
     * _latestNews: Menyimpan objek berita terbaru yang diterima dari stream.
     */
    private val _selectedCategory = MutableStateFlow("All")
    private val _readCount = MutableStateFlow(0)
    private val _latestNews = MutableStateFlow<News?>(null)

    // Eksposur StateFlow secara read-only untuk digunakan oleh UI/Main
    val readCount = _readCount.asStateFlow()
    val latestNews = _latestNews.asStateFlow()

    /**
     * Memperbarui kategori berita yang ingin dipantau.
     * Fungsi ini akan memicu flatMapLatest pada startStream untuk memulai ulang flow.
     */
    fun setCategory(category: String) {
        _selectedCategory.value = category
        printDivider("FILTER AKTIF: ${category.uppercase()}")
    }

    /**
     * Menginisialisasi aliran data (Stream) berita secara reaktif.
     * flatMapLatest: Menghentikan stream lama dan membuat stream baru setiap kali kategori berubah.
     * filter: Menyaring berita berdasarkan kategori yang dipilih.
     * map: Melakukan transformasi data pada judul berita.
     * onEach: Menyimpan referensi berita terbaru dan menampilkan log ke konsol.
     * catch: Menangani error yang terjadi pada tingkat upstream (Repository).
     * launchIn: Menjalankan flow di dalam CoroutineScope yang ditentukan.
     */
    fun startStream() {
        _selectedCategory
            .flatMapLatest { category ->
                repo.getNewsStream()
                    .onStart { println("Menghubungkan ke satelit [Topik: $category]...") }
                    .filter { news ->
                        category == "All" || news.category.equals(category, ignoreCase = true)
                    }
                    .map { news ->
                        // Transformasi Judul: Menambahkan prefix kategori ke judul berita
                        news.copy(title = "[${news.category.uppercase()}] ${news.title}")
                    }
                    .onEach { news ->
                        _latestNews.value = news
                        println(" >> [${news.getFormattedTime()}] ${news.title}")
                    }
            }
            .catch { e -> println("Terjadi gangguan stream: ${e.message}") }
            .launchIn(scope)
    }

    /**
     * Mengambil detail lengkap berita secara Asynchronous.
     * Menggunakan withContext(Dispatchers.IO) untuk memastikan operasi I/O tidak memblokir thread utama.
     * Setelah berhasil, jumlah berita yang dibaca akan diperbarui secara atomik.
     */
    suspend fun readDetail() {
        val news = _latestNews.value ?: run {
            println("Belum ada berita masuk untuk dibaca.")
            return
        }

        println("\nMengunduh konten lengkap (ID: ${news.id})...")

        try {
            val fullNews = withContext(Dispatchers.IO) {
                repo.getNewsDetail(news.id)
            }

            fullNews?.let {
                renderDetailView(it)
                // Memperbarui nilai StateFlow menggunakan fungsi update (thread-safe)
                _readCount.update { count -> count + 1 }
                println("Statistik: ${_readCount.value} berita telah selesai dibaca.\n")
            }
        } catch (e: Exception) {
            println("Gagal memuat detail: ${e.message}")
        }
    }

    // --- Private UI Helpers ---

    /**
     * Menampilkan tampilan detail berita dengan format tabel ASCII sederhana.
     * Menggunakan chunked untuk memastikan teks isi berita tidak melampaui lebar border.
     */
    private fun renderDetailView(news: News) {
        val border = "+----------------------------------------------------------+"
        println("\n$border")
        println("|  NEWS CONTENT DETAIL")
        println(border)
        println("| ID       : ${news.id}")
        println("| JUDUL    : ${news.title}")
        println("| KATEGORI : ${news.category}")
        println("| WAKTU    : ${news.getFormattedTime()}")
        println(border)
        println("| ISI BERITA:")

        news.content.chunked(54).forEach { line ->
            println("| $line".padEnd(59) + "|")
        }

        println(border)
    }

    /**
     * Menampilkan garis pembatas visual pada konsol untuk memisahkan log kategori.
     */
    private fun printDivider(label: String) {
        println("\n" + "=".repeat(15) + " $label " + "=".repeat(15))
    }
}