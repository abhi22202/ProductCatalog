package com.example.productcatalog.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.CartItem
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun CartItemCard(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit, onRemove: () -> Unit) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(Spacing.medium)) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.medium), verticalAlignment = Alignment.CenterVertically) {
                ProductImage(item.imageUrl, item.name, Modifier.size(Spacing.cartImage))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    Text(item.price.formatted(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.subtotal_label, item.subtotal.formatted()), style = MaterialTheme.typography.labelLarge)
                }
            }
            val removeDescription = stringResource(R.string.remove_item, item.name)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                QuantitySelector(item.name, item.quantity, onIncrease, onDecrease)
                TextButton(onClick = onRemove, modifier = Modifier.semantics { contentDescription = removeDescription }) {
                    Text(stringResource(R.string.remove), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
