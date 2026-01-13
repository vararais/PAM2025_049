package com.example.savitapp.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.savitapp.view.auth.LoginScreen
import com.example.savitapp.view.auth.RegisterScreen
import com.example.savitapp.view.home.DetailScreen
import com.example.savitapp.view.home.EditStuffScreen
import com.example.savitapp.view.home.EntryStuffScreen
import com.example.savitapp.view.home.HomeScreen

// Enum untuk mendaftarkan semua alamat (Route) halaman
enum class DestinasiSavit(val route: String) {
    Login("login"),
    Register("register"),
    Dashboard("dashboard/{userId}"),          // Butuh User ID
    Entry("entry/{userId}"),                  // Butuh User ID
    Detail("detail/{userId}/{stuffId}"),      // Butuh User ID & Stuff ID
    Edit("edit/{userId}/{stuffId}")           // Butuh User ID & Stuff ID
}

@Composable
fun SavitAppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiSavit.Login.route,
        modifier = modifier
    ) {
        // 1. HALAMAN LOGIN
        composable(DestinasiSavit.Login.route) {
            LoginScreen(
                onLoginSuccess = { userId ->
                    // Navigasi ke Dashboard bawa userId & Hapus Login dari history back
                    navController.navigate("dashboard/$userId") {
                        popUpTo(DestinasiSavit.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(DestinasiSavit.Register.route)
                }
            )
        }

        // 2. HALAMAN REGISTER
        composable(DestinasiSavit.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 3. HALAMAN DASHBOARD (HOME)
        composable(
            route = DestinasiSavit.Dashboard.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            HomeScreen(
                userId = userId,
                onNavigateToAdd = {
                    // Pindah ke halaman Tambah Barang
                    navController.navigate("entry/$userId")
                },
                onDetailClick = { stuffId ->
                    // Pindah ke halaman Detail Barang
                    navController.navigate("detail/$userId/$stuffId")
                }
            )
        }

        // 4. HALAMAN ENTRY (TAMBAH BARANG)
        composable(
            route = DestinasiSavit.Entry.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            EntryStuffScreen(
                userId = userId,
                navigateBack = { navController.popBackStack() }
            )
        }

        // 5. HALAMAN DETAIL (INFO + HISTORY)
        composable(
            route = DestinasiSavit.Detail.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("stuffId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val stuffId = backStackEntry.arguments?.getInt("stuffId") ?: 0

            DetailScreen(
                userId = userId,
                stuffId = stuffId,
                navigateBack = { navController.popBackStack() },
                onEditClick = { id ->
                    // Pindah ke halaman Edit
                    navController.navigate("edit/$userId/$id")
                }
            )
        }

        // 6. HALAMAN EDIT BARANG
        composable(
            route = DestinasiSavit.Edit.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("stuffId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val stuffId = backStackEntry.arguments?.getInt("stuffId") ?: 0

            EditStuffScreen(
                userId = userId,
                stuffId = stuffId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}