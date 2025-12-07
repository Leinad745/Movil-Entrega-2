package com.example.animeping.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animeping.viewmodel.AnimeViewModel
import com.example.animeping.viewmodel.PerfilViewModel
import com.example.animeping.viewmodel.RegViewModel

sealed class AppScreen(val route: String){
    object Register : AppScreen("register")
    object Login : AppScreen("login")
    object QuickLogin : AppScreen("quick_login")
    object Perfil : AppScreen("perfil")
    object HomeScreen : AppScreen("home_screen")
    object WelcomeScreen : AppScreen("welcome_screen")
    object MainAnimeScreen : AppScreen("main_anime_screen")
    object AnimeSearch : AppScreen("anime_search")
    object AnimeDetail : AppScreen("anime_detail/{animeId}") {
        fun createRoute(animeName: String) = "anime_detail/$animeName"
    }
    object FavsScreen : AppScreen("favs_screen")
}

@Composable
fun AppNavigation(regViewModel: RegViewModel = viewModel()) {
    val navController = rememberNavController()
    val startDestination = AppScreen.WelcomeScreen.route

    NavHost(navController, startDestination = startDestination) {

        composable(AppScreen.WelcomeScreen.route) {
            val userExists = regViewModel.usuarioExiste()

            LaunchedEffect(userExists) {
                if (userExists) {
                    navController.navigate(AppScreen.MainAnimeScreen.route) {
                        popUpTo(AppScreen.WelcomeScreen.route) { inclusive = true }
                    }
                }
            }

            if (!userExists) {
                WelcomeScreen(
                    onLoginClick = { navController.navigate(AppScreen.Login.route) },
                    onRegisterClick = { navController.navigate(AppScreen.Register.route) }
                )
            }
        }

        composable(AppScreen.Login.route) {
            LoginScreen(navController = navController, regViewModel = regViewModel)
        }

        composable(AppScreen.Register.route) {
            Registro(navController = navController, regViewModel = regViewModel)
        }


        composable(AppScreen.QuickLogin.route) {
            registroRapido(navController = navController, regViewModel = regViewModel)
        }

        composable(AppScreen.FavsScreen.route) {
            FavScreen(navController = navController)
        }

        composable(AppScreen.Perfil.route) {
            val perfilViewModel: PerfilViewModel = viewModel()
            PerfilScreen(
                viewModel = perfilViewModel,
                onNavigateToFavs = { navController.navigate(AppScreen.FavsScreen.route) },
                navController = navController,
                regViewModel = regViewModel

            )
        }

        composable(AppScreen.MainAnimeScreen.route) {
            AnimeListScreen (
                onAnimeClickScreen = { animeId ->
                    navController.navigate(AppScreen.AnimeDetail.createRoute(animeId))
                },onSearchClick = {
                    navController.navigate(AppScreen.AnimeSearch.route)
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Perfil.route)
                }
            )
        }
        composable(
            route = AppScreen.AnimeDetail.route,
            arguments = listOf(navArgument("animeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId") ?: ""
            AnimeScreen(
                animeId = animeId,
                onBack = { navController.popBackStack() }
            )
        }
        //Pantallas que falta crear y hacer composables

        composable(AppScreen.HomeScreen.route) {

        }
        composable(AppScreen.AnimeSearch.route) {
            AnimeSearchScreen(
                onBack = { navController.popBackStack() },
                onAnimeClick = { animeId ->
                    navController.navigate(AppScreen.AnimeDetail.createRoute(animeId))
                }
            )
        }
    }
}


