package com.example.productcatalog.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.productcatalog.R
import com.example.productcatalog.domain.model.AppError
import com.example.productcatalog.presentation.theme.Spacing

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(Spacing.large), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
        Spacer(Modifier.height(Spacing.medium))
        Text(stringResource(R.string.loading), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ErrorView(error: AppError, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    EmptyView(stringResource(R.string.error_title), stringResource(error.messageResource()), modifier) {
        Button(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}

@Composable
fun EmptyView(title: String, message: String, modifier: Modifier = Modifier,
    action: @Composable () -> Unit = {}) {
    Column(modifier.fillMaxSize().padding(Spacing.extraLarge), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.medium, Alignment.CenterVertically)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Text(message, style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        action()
    }
}
