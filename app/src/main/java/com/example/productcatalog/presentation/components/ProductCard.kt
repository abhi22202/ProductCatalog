package com.example.productcatalog.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.Product
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun ProductCard(product: Product, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(onClick = onClick, modifier = modifier) {
        ProductImage(product.thumbnail, product.name, Modifier.fillMaxWidth().height(Spacing.image))
        Column(Modifier.padding(Spacing.medium), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            Text(product.name, style = MaterialTheme.typography.titleSmall, maxLines = 2,
                minLines = 2, overflow = TextOverflow.Ellipsis)
            Text(product.price.formatted(), style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
            RatingLabel(product.rating)
        }
    }
}

@Composable
fun RatingLabel(rating: Double) {
    val description = stringResource(R.string.rating_accessibility, rating.formattedRating())
    Text(stringResource(R.string.rating, rating.formattedRating()), style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.semantics { contentDescription = description },
        color = MaterialTheme.colorScheme.onSurfaceVariant)
}
