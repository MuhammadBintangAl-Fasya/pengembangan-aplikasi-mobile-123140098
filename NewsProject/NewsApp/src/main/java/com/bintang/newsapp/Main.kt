import kotlinx.coroutines.*
import manager.NewsManager
import repository.NewsRepository
import kotlin.system.exitProcess

fun main() = runBlocking {
    val repo = NewsRepository()
    val manager = NewsManager(repo, this)

    // Header Aplikasi
    println("==============================================")
    println("       NEWS TERMINAL CLIENT v1.0")
    println("==============================================")

    // Jalankan stream di background
    manager.startStream()

    // Tampilkan Menu Sekali di Awal
    showMenu()

    while (true) {
        print("\n[INPUT] > ")
        val input = withContext(Dispatchers.IO) { readLine()?.lowercase() }

        when (input) {
            "1" -> manager.setCategory("All")
            "2" -> manager.setCategory("Teknologi")
            "3" -> manager.setCategory("Olahraga")
            "4" -> manager.setCategory("Ekonomi")
            "r" -> launch { manager.readDetail() }
            "m" -> showMenu() // Fitur tambahan untuk liat menu lagi
            "q" -> {
                println("\nMematikan sistem...")
                println("Statistik Akhir: ${manager.readCount.value} Berita dibaca.")
                println("Sampai jumpa!")
                exitProcess(0)
            }
            null -> continue
            else -> println(" [!] Perintah tidak valid. Ketik 'm' untuk menu.")
        }
    }
}

fun showMenu() {
    println("""
    PILIH SALURAN:
    (1) Semua       (2) Teknologi   (3) Olahraga
    (4) Ekonomi     (r) Baca Detail (m) Lihat Menu
    (q) Keluar
    ----------------------------------------------""".trimIndent())
}