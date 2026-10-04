package com.example.productcatalog.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.productcatalog.R

@Composable
fun SearchField(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    val keyboard = LocalSoftwareKeyboardController.current
    OutlinedTextField(value = query, onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(), singleLine = true,
        label = { Text(stringResource(R.string.search_hint)) },
        trailingIcon = {
            if (query.isNotEmpty()) TextButton(onClick = { onQueryChanged("") }) {
                Text(stringResource(R.string.clear_search))
            }
        }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }))
}
