package com.example.productcatalog.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.CartItem

@Composable
fun QuantitySelector(name: String, quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    val increaseDescription = stringResource(R.string.increase_quantity, name)
    val decreaseDescription = stringResource(R.string.decrease_quantity, name)
    val quantityDescription = stringResource(R.string.quantity_value, quantity)
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrease, modifier = Modifier.semantics { contentDescription = decreaseDescription }) {
            Text("−", style = MaterialTheme.typography.titleLarge)
        }
        Text(quantity.toString(), modifier = Modifier.semantics { contentDescription = quantityDescription },
            style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = onIncrease, enabled = quantity < CartItem.MAX_QUANTITY,
            modifier = Modifier.semantics { contentDescription = increaseDescription }) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}
