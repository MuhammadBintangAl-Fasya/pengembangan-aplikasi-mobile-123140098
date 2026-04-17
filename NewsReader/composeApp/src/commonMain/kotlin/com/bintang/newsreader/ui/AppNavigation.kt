package com.bintang.newsreader.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bintang.newsreader.di.AppModule

// Route constants
object Routes {
    const val NEWS_LIST = "news_list"
    const val NEWS_DETAIL = "news_detail/{articleId}"
    fun newsDetail(articleId: Int) = "news_detail/$articleId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: NewsViewModel = viewModel(factory = AppModule.newsViewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Routes.NEWS_LIST
    ) {
        // Screen daftar artikel
        composable(Routes.NEWS_LIST) {
            NewsListScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    navController.navigate(Routes.newsDetail(article.id))
                }
            )
        }

        // Screen detail artikel
        composable(
            route = Routes.NEWS_DETAIL,
            arguments = listOf(
                navArgument("articleId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.get("articleId")
                ?.toString()?.toIntOrNull() ?: return@composable

            // Ambil artikel dari state yang sudah ada di ViewModel
            NewsDetailScreen(
                viewModel = viewModel,
                articleId = articleId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
