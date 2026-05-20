package com.example.furniturestore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.furniturestore.data.local.entity.FavoriteEntity

@Composable
fun FavoritesScreen(
    items: List<FavoriteEntity>,
    onRemove: (FavoriteEntity) -> Unit,
    onOpen: (FavoriteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Нет избранных товаров", style = MaterialTheme.typography.titleMedium)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier) {
            lazyItems(items, key = { it.productId }) { favorite ->
                Card(elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = favorite.imageUrl,
                            contentDescription = favorite.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = favorite.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 6.dp))
                        Button(onClick = { onOpen(favorite) }) { Text(text = "Открыть") }
                        Button(onClick = { onRemove(favorite) }, modifier = Modifier.padding(top = 6.dp)) { Text(text = "Удалить") }
                    }
                }
            }
        }
    }
}
