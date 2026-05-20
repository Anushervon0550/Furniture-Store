package com.example.furniturestore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.furniturestore.data.local.entity.CartEntity

@Composable
fun CartScreen(
    items: List<CartEntity>,
    total: Double,
    onIncrease: (CartEntity) -> Unit,
    onDecrease: (CartEntity) -> Unit,
    onRemove: (CartEntity) -> Unit,
    onCheckout: () -> Unit,
    placing: Boolean = false,
    orderId: String? = null,
    orderError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
        if (items.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Корзина пуста", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                lazyItems(items, key = { it.id }) { item ->
                    CartItemRow(
                        item = item,
                        onIncrease = onIncrease,
                        onDecrease = onDecrease,
                        onRemove = onRemove
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Итого: ${String.format("%.2f", total)} ₽", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Button(onClick = onCheckout, enabled = !placing) { if (placing) Text("Оформляем…") else Text("Оформить") }
            }
            if (orderId != null) {
                Text(text = "Заказ оформлен: #$orderId", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
            }
            if (orderError != null) {
                Text(text = orderError, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartEntity,
    onIncrease: (CartEntity) -> Unit,
    onDecrease: (CartEntity) -> Unit,
    onRemove: (CartEntity) -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "${String.format("%.2f", item.price)} ₽", color = MaterialTheme.colorScheme.primary)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onDecrease(item) }) { Text("-") }
                    Text(text = item.quantity.toString())
                    Button(onClick = { onIncrease(item) }) { Text("+") }
                }
            }
            IconButton(onClick = { onRemove(item) }) { Icon(Icons.Outlined.Delete, contentDescription = null) }
        }
    }
}
