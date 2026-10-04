package com.example.productcatalog.presentation.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.productcatalog.R
import com.example.productcatalog.presentation.components.*
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun ProductListScreen(state: ProductListUiState, onQueryChanged: (String) -> Unit,
    onRetry: () -> Unit, onProduct: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            Text(stringResource(R.string.catalog_subtitle), style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            SearchField(state.query, onQueryChanged)
            Text(stringResource(R.string.usd_note), style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        when (val content = state.content) {
            ContentState.Loading -> LoadingView()
            ContentState.Empty -> EmptyView(
                stringResource(if (state.query.isBlank()) R.string.catalog_empty_title else R.string.search_empty_title),
                stringResource(if (state.query.isBlank()) R.string.catalog_empty_body else R.string.search_empty_body))
            is ContentState.Error -> ErrorView(content.error, onRetry)
            is ContentState.Success -> LazyVerticalGrid(
                columns = GridCells.Adaptive(156.dp), contentPadding = PaddingValues(Spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                items(content.data, key = { it.id }) { product ->
                    ProductCard(product, onClick = { onProduct(product.id) })
                }
            }
        }
    }
}
