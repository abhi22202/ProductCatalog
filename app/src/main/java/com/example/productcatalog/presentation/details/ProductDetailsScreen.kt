package com.example.productcatalog.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.presentation.components.*
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun ProductDetailsScreen(state: ProductDetailsUiState, onRetry: () -> Unit, onAdd: () -> Unit,
    modifier: Modifier = Modifier) {
    when (val content = state.content) {
        ContentState.Loading -> LoadingView(modifier)
        ContentState.Empty -> EmptyView(stringResource(R.string.error_not_found), "", modifier)
        is ContentState.Error -> ErrorView(content.error, onRetry, modifier)
        is ContentState.Success -> ProductDetailsContent(content.data, state.adding, onAdd, modifier)
    }
}

@Composable
private fun ProductDetailsContent(product: Product, adding: Boolean, onAdd: () -> Unit, modifier: Modifier) {
    Column(modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.large)) {
            item { ProductGallery(product) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    Text(product.name, style = MaterialTheme.typography.headlineMedium)
                    RatingLabel(product.rating)
                    Text(product.price.formatted(), style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    Text(stringResource(R.string.description_heading), style = MaterialTheme.typography.titleMedium)
                    Text(product.description, style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(Spacing.medium), verticalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                        DetailRow(stringResource(R.string.category_label), product.category.replace('-', ' '))
                        DetailRow(stringResource(R.string.brand_label), product.brand ?: stringResource(R.string.brand_unknown))
                        DetailRow(stringResource(R.string.stock_label), stringResource(R.string.stock_value, product.stock))
                    }
                }
            }
        }
        Surface(tonalElevation = Spacing.tiny, shadowElevation = Spacing.small) {
            Button(onClick = onAdd, enabled = !adding && product.stock > 0,
                modifier = Modifier.fillMaxWidth().padding(Spacing.medium)) {
                Text(stringResource(when {
                    adding -> R.string.adding
                    product.stock == 0 -> R.string.out_of_stock
                    else -> R.string.add_to_cart
                }))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.medium)) {
        Text(label, Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ProductGallery(product: Product) {
    val images = product.images.ifEmpty { listOf(product.thumbnail) }
    val pager = rememberPagerState(pageCount = { images.size })
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
        HorizontalPager(pager, key = { it }) { index ->
            ProductImage(images[index], stringResource(R.string.gallery_image, product.name, index + 1, images.size),
                Modifier.fillMaxWidth().height(Spacing.gallery))
        }
        if (images.size > 1) Text(stringResource(R.string.gallery_page, pager.currentPage + 1, images.size),
            style = MaterialTheme.typography.labelMedium)
    }
}
