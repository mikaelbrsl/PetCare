package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.data.model.PetProduct
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@Composable
fun ProductsScreen(
    pets: List<Pet>,
    products: List<PetProduct>,
    onAddProduct: (Int?, String, String, Double, String, Boolean) -> Unit,
    onToggleRestock: (PetProduct) -> Unit,
    onDeleteProduct: (PetProduct) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PetCareOrange,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Produto", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Produtos & Suprimentos (RF21)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gerencie rações, areia higiênica, brinquedos e controle o que precisa comprar.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum produto cadastrado. Toque em 'Novo Produto'.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(products, key = { it.id }) { prod ->
                        val petName = pets.find { it.id == prod.petId }?.name ?: "Todos os Pets"
                        ElevatedCard(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = if (prod.needsRestock) PetCareOrange else PetCareGreen,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${prod.category.uppercase()} • $petName",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (prod.needsRestock) PetCareOrange else PetCareGreen
                                    )
                                    Text(prod.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Qtd: ${prod.quantity} • R$ ${"%.2f".format(prod.price)}",
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (prod.needsRestock) "Status: Precisa repor (Lista de Compras)" else "Status: Em estoque",
                                        fontSize = 12.sp,
                                        color = if (prod.needsRestock) PetCareOrange else PetCareGreen,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                IconButton(onClick = { onToggleRestock(prod) }) {
                                    Icon(
                                        Icons.Default.ShoppingCartCheckout,
                                        contentDescription = "Alternar estoque",
                                        tint = PetCareGreen
                                    )
                                }
                                IconButton(onClick = { onDeleteProduct(prod) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, category, price, qty, restock ->
                onAddProduct(null, name, category, price, qty, restock)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Alimentação") }
    var priceText by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var needsRestock by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cadastrar Produto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Alimentação", "Higiene", "Acessório").forEach { c ->
                        FilterChip(
                            selected = category == c,
                            onClick = { category = c },
                            label = { Text(c, fontSize = 11.sp) }
                        )
                    }
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Produto *") },
                    placeholder = { Text("Ex: Ração Golden 10kg") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade/Peso") },
                        placeholder = { Text("Ex: 1 saco 10kg") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Preço (R$)") },
                        placeholder = { Text("150.00") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = needsRestock, onCheckedChange = { needsRestock = it })
                    Text("Marcar para comprar / repor estoque", fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val price = priceText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        onConfirm(name, category, price, quantity.ifBlank { "1 un" }, needsRestock)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PetCareOrange)
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
