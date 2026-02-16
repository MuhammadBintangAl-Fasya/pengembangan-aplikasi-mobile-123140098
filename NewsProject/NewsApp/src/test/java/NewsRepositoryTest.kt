import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import repository.NewsRepository

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {

    @Test
    fun testFlowEmitsData() = runTest {
        // Arrange
        val repository = NewsRepository()

        println("Mencoba mengambil data pertama dari Flow...")

        // Act
        // .first() akan menunggu item pertama keluar, lalu stop (cocok untuk infinite flow)
        val news = repository.getNewsStream().first()

        // Assert
        assertNotNull("Berita tidak boleh null", news)
        assertTrue("Judul harus mengandung 'Breaking News'", news.title.contains("Breaking News"))

        println("Sukses! Diterima: ${news.title}")
    }
}