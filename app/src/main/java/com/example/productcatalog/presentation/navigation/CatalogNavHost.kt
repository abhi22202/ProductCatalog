package com.example.productcatalog.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.productcatalog.R
import com.example.productcatalog.presentation.cart.*
import com.example.productcatalog.presentation.components.ContentState
import com.example.productcatalog.presentation.components.messageResource
import com.example.productcatalog.presentation.details.*
import com.example.productcatalog.presentation.product.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogNavHost() {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: Routes.PRODUCTS
    val cartViewModel: CartViewModel = hiltViewModel()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val count = (cartState.content as? ContentState.Success)?.data?.totalItems ?: 0
    val snackbar = remember { SnackbarHostState() }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(stringResource(when (route) {
                Routes.PRODUCTS -> R.string.catalog_title
                Routes.CART -> R.string.cart_title
                else -> R.string.details_title
            }))
        }, navigationIcon = {
            if (route != Routes.PRODUCTS) {
                val backDescription = stringResource(R.string.navigate_back)
                IconButton(onClick = { navController.popBackStack() },
                    modifier = Modifier.semantics { contentDescription = backDescription }) { Text("‹") }
            }
        }, actions = {
            if (route != Routes.CART) {
                val cartDescription = stringResource(R.string.open_cart, count)
                TextButton(onClick = { navController.navigate(Routes.CART) { launchSingleTop = true } },
                    modifier = Modifier.semantics { contentDescription = cartDescription }) {
                    BadgedBox(badge = { if (count > 0) Badge { Text(count.toString()) } }) {
                        Text(stringResource(R.string.cart_title))
                    }
                }
            }
        })
    }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        NavHost(navController, startDestination = Routes.PRODUCTS, modifier = Modifier.padding(padding)) {
            composable(Routes.PRODUCTS) {
                val viewModel: ProductListViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                ProductListScreen(state, viewModel::onQueryChanged, viewModel::retry,
                    onProduct = { navController.navigate(Routes.details(it)) })
            }
            composable(Routes.DETAILS, arguments = listOf(navArgument(Routes.PRODUCT_ID) { type = NavType.IntType })) {
                val viewModel: ProductDetailsViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val feedbackMessage = when (val feedback = state.feedback) {
                    CartFeedback.Added -> stringResource(R.string.cart_added)
                    is CartFeedback.Failed -> stringResource(feedback.error.messageResource())
                    null -> null
                }
                LaunchedEffect(state.feedback) {
                    if (feedbackMessage != null) {
                        snackbar.showSnackbar(feedbackMessage)
                        viewModel.feedbackShown()
                    }
                }
                ProductDetailsScreen(state, viewModel::retry, viewModel::add)
            }
            composable(Routes.CART) {
                val errorMessage = cartState.operationError?.let { stringResource(it.messageResource()) }
                LaunchedEffect(cartState.operationError) {
                    if (errorMessage != null) {
                        snackbar.showSnackbar(errorMessage)
                        cartViewModel.errorShown()
                    }
                }
                CartScreen(cartState, cartViewModel::increase, cartViewModel::decrease, cartViewModel::remove,
                    cartViewModel::retry, onBrowse = { navController.popBackStack(Routes.PRODUCTS, false) })
            }
        }
    }
}
