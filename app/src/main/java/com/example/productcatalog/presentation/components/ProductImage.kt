package com.example.productcatalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.productcatalog.R

@Composable
fun ProductImage(url: String, description: String, modifier: Modifier = Modifier) {
    Box(modifier.background(MaterialTheme.colorScheme.surfaceContainerLow), contentAlignment = Alignment.Center) {
        AsyncImage(model = url, contentDescription = description,
            placeholder = painterResource(R.drawable.image_placeholder),
            error = painterResource(R.drawable.image_placeholder),
            fallback = painterResource(R.drawable.image_placeholder),
            contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
    }
}
