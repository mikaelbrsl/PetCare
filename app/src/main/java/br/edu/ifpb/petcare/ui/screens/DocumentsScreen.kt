package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.data.model.PetDocument
import br.edu.ifpb.petcare.ui.components.PetSelectorDropdown
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@Composable
fun DocumentsScreen(
    pets: List<Pet>,
    documents: List<PetDocument>,
    initialPetId: Int?,
    onAddDocument: (Int, String, String, String, String, String) -> Unit,
    onDeleteDocument: (PetDocument) -> Unit
) {
    var selectedPetId by remember(initialPetId) { mutableStateOf(initialPetId) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredDocs = documents.filter { doc ->
        selectedPetId == null || doc.petId == selectedPetId
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PetCareOrange,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Documento", fontWeight = FontWeight.Bold) }
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
                text = "Documentos dos Pets",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cadastre e consulte RG Pet, exames, receitas, pedigree e atestados associados a cada animal (RF16-RF18).",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            PetSelectorDropdown(
                pets = pets,
                selectedPetId = selectedPetId,
                onPetSelected = { selectedPetId = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cadastre um pet antes de associar documentos.")
                }
            } else if (filteredDocs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum documento cadastrado para este filtro.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredDocs, key = { it.id }) { doc ->
                        val petName = pets.find { it.id == doc.petId }?.name ?: "Pet"
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
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = PetCareGreen,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${doc.docType.uppercase()} • $petName",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PetCareGreen
                                    )
                                    Text(doc.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        "Emissão: ${doc.issueDate} | Nº/Código: ${doc.documentNumberOrCode.ifBlank { "-" }}",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                    if (doc.summary.isNotBlank()) {
                                        Text(doc.summary, fontSize = 13.sp, color = Color.Gray)
                                    }
                                }
                                IconButton(onClick = { onDeleteDocument(doc) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog && pets.isNotEmpty()) {
        AddDocumentDialog(
            pets = pets,
            preselectedPetId = selectedPetId ?: pets.first().id,
            onDismiss = { showAddDialog = false },
            onConfirm = { petId, title, docType, issueDate, code, summary ->
                onAddDocument(petId, title, docType, issueDate, code, summary)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddDocumentDialog(
    pets: List<Pet>,
    preselectedPetId: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String, String, String, String) -> Unit
) {
    var petId by remember { mutableStateOf(preselectedPetId) }
    var title by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("Exame") }
    var issueDate by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cadastrar Documento", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PetSelectorDropdown(
                    pets = pets,
                    selectedPetId = petId,
                    onPetSelected = { if (it != null) petId = it },
                    allowAllOption = false,
                    label = "Associar ao Pet (RF18)"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Exame", "Receita", "RG/Registro").forEach { t ->
                        FilterChip(
                            selected = docType == t,
                            onClick = { docType = t },
                            label = { Text(t, fontSize = 11.sp) }
                        )
                    }
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Documento *") },
                    placeholder = { Text("Ex: Hemograma Completo, RGA...") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = issueDate,
                        onValueChange = { issueDate = it },
                        label = { Text("Data Emissão *") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Nº / Protocolo") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Resumo / Anotações do Documento") },
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && issueDate.isNotBlank()) {
                        onConfirm(petId, title, docType, issueDate, code, summary)
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
