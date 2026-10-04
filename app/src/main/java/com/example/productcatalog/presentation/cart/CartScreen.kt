package com.example.productcatalog.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.productcatalog.R
import com.example.productcatalog.presentation.components.*
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun CartScreen(state: CartUiState, onIncrease: (Int) -> Unit, onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit, onRetry: () -> Unit, onBrowse: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Text(stringResource(R.string.offline_note), modifier = Modifier.padding(Spacing.medium),
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        when (val content = state.content) {
            ContentState.Loading -> LoadingView()
            ContentState.Empty -> EmptyView(stringResource(R.string.cart_empty_title), stringResource(R.string.cart_empty_body)) {
                Button(onClick = onBrowse) { Text(stringResource(R.string.browse_products)) }
            }
            is ContentState.Error -> ErrorView(content.error, onRetry)
            is ContentState.Success -> {
                LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                    items(content.data.items, key = { it.productId }) { item ->
                        CartItemCard(item, { onIncrease(item.productId) }, { onDecrease(item.productId) }, { onRemove(item.productId) })
                    }
                    item {
                        Text(stringResource(R.string.offline_image_note), style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Surface(tonalElevation = Spacing.tiny) {
                    Row(Modifier.fillMaxWidth().padding(Spacing.large), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(stringResource(R.string.total_label), style = MaterialTheme.typography.titleLarge)
                            Text(stringResource(R.string.item_count, content.data.totalItems), style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(content.data.totalPrice.formatted(), style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
