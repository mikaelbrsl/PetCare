package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.HealthRecord
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.ui.components.PetSelectorDropdown
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@Composable
fun MedicationsScreen(
    pets: List<Pet>,
    records: List<HealthRecord>,
    initialPetId: Int?,
    onAddRecord: (Int, String, String, String, String, String, String) -> Unit,
    onDeleteRecord: (HealthRecord) -> Unit
) {
    var selectedPetId by remember(initialPetId) { mutableStateOf(initialPetId) }
    var selectedFilter by remember { mutableStateOf("Todos") } // Todos, Vacina (RF09), Remédio (RF10)
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredRecords = records.filter { rec ->
        val matchesPet = selectedPetId == null || rec.petId == selectedPetId
        val matchesType = selectedFilter == "Todos" || rec.type == selectedFilter
        matchesPet && matchesType
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PetCareOrange,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Registrar Vacina / Remédio", fontWeight = FontWeight.Bold) }
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
                text = "Remédios & Vacinas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Controle o cartão de vacinas (RF09) e os medicamentos contínuos ou pontuais (RF10).",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            PetSelectorDropdown(
                pets = pets,
                selectedPetId = selectedPetId,
                onPetSelected = { selectedPetId = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todos", "Vacina", "Remédio").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cadastre um pet primeiro para registrar vacinas ou remédios.")
                }
            } else if (filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum registro encontrado.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredRecords, key = { it.id }) { rec ->
                        val petName = pets.find { it.id == rec.petId }?.name ?: "Pet"
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
                                    imageVector = if (rec.type == "Vacina") Icons.Default.Vaccines else Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = if (rec.type == "Vacina") PetCareGreen else PetCareOrange,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${rec.type.uppercase()} • $petName",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PetCareGreen
                                    )
                                    Text(rec.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text("Dose/Posologia: ${rec.dosageOrBatch}", fontSize = 13.sp)
                                    Text(
                                        "Aplicado em: ${rec.applicationDate} | Próxima: ${rec.nextDueDate.ifBlank { "Não se aplica" }}",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                    if (rec.notes.isNotBlank()) {
                                        Text("Obs: ${rec.notes}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                IconButton(onClick = { onDeleteRecord(rec) }) {
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
        AddHealthRecordDialog(
            pets = pets,
            preselectedPetId = selectedPetId ?: pets.first().id,
            onDismiss = { showAddDialog = false },
            onConfirm = { petId, type, name, dosage, appDate, nextDate, notes ->
                onAddRecord(petId, type, name, dosage, appDate, nextDate, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddHealthRecordDialog(
    pets: List<Pet>,
    preselectedPetId: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String, String, String, String, String) -> Unit
) {
    var petId by remember { mutableStateOf(preselectedPetId) }
    var type by remember { mutableStateOf("Vacina") }
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var appDate by remember { mutableStateOf("") }
    var nextDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Registro de Saúde", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PetSelectorDropdown(
                    pets = pets,
                    selectedPetId = petId,
                    onPetSelected = { if (it != null) petId = it },
                    allowAllOption = false,
                    label = "Pet"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = type == "Vacina",
                        onClick = { type = "Vacina" },
                        label = { Text("Vacina (RF09)") }
                    )
                    FilterChip(
                        selected = type == "Remédio",
                        onClick = { type = "Remédio" },
                        label = { Text("Remédio (RF10)") }
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Vacina ou Remédio *") },
                    placeholder = { Text("Ex: Antirrábica, Bravecto...") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosagem / Lote *") },
                    placeholder = { Text("Ex: 1 comp. 12/12h ou Dose Anual") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = appDate,
                        onValueChange = { appDate = it },
                        label = { Text("Data Aplicada *") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = nextDate,
                        onValueChange = { nextDate = it },
                        label = { Text("Próxima Dose") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Observações") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && appDate.isNotBlank()) {
                        onConfirm(petId, type, name, dosage.ifBlank { "1 dose" }, appDate, nextDate, notes)
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
