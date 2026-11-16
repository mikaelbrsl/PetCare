package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Appointment
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.ui.components.PetSelectorDropdown
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@Composable
fun AgendaScreen(
    pets: List<Pet>,
    appointments: List<Appointment>,
    initialPetId: Int?,
    onAddAppointment: (Int, String, String, String, String, String, String) -> Unit,
    onToggleDone: (Appointment) -> Unit,
    onDeleteAppointment: (Appointment) -> Unit
) {
    var selectedPetId by remember(initialPetId) { mutableStateOf(initialPetId) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Próximos (RF11/RF12/RF14), 1 = Histórico Concluído (RF13)
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredList = appointments.filter { appt ->
        val matchesPet = selectedPetId == null || appt.petId == selectedPetId
        val matchesTab = if (selectedTab == 0) !appt.isDone else appt.isDone
        matchesPet && matchesTab
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PetCareOrange,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Agendamento", fontWeight = FontWeight.Bold) }
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
                text = "Agenda & Consultas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Organize consultas veterinárias, banho e tosa e veja o histórico de atendimentos.",
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

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = PetCareCardBg,
                contentColor = PetCareGreen
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Próximos (${appointments.count { !it.isDone }})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Histórico (${appointments.count { it.isDone }})") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cadastre um pet na tela inicial antes de criar agendamentos.")
                }
            } else if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (selectedTab == 0) "Nenhum agendamento pendente. Toque em 'Novo Agendamento'."
                        else "Nenhum atendimento concluído no histórico ainda."
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        val petName = pets.find { it.id == item.petId }?.name ?: "Pet"
                        AppointmentCard(
                            appointment = item,
                            petName = petName,
                            onToggleDone = { onToggleDone(item) },
                            onDelete = { onDeleteAppointment(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog && pets.isNotEmpty()) {
        AddAppointmentDialog(
            pets = pets,
            preselectedPetId = selectedPetId ?: pets.first().id,
            onDismiss = { showAddDialog = false },
            onConfirm = { petId, title, category, date, time, location, notes ->
                onAddAppointment(petId, title, category, date, time, location, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AppointmentCard(
    appointment: Appointment,
    petName: String,
    onToggleDone: () -> Unit,
    onDelete: () -> Unit
) {
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
            IconButton(onClick = onToggleDone) {
                Icon(
                    imageVector = if (appointment.isDone) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Marcar como concluído",
                    tint = if (appointment.isDone) PetCareGreen else PetCareOrange
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${appointment.category} • $petName",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PetCareGreen
                )
                Text(
                    text = appointment.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (appointment.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = "Data: ${appointment.date} às ${appointment.time}",
                    fontSize = 13.sp
                )
                if (appointment.locationOrVet.isNotBlank()) {
                    Text(
                        text = "Local/Vet: ${appointment.locationOrVet}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
                if (appointment.notes.isNotBlank()) {
                    Text(
                        text = "Obs: ${appointment.notes}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAppointmentDialog(
    pets: List<Pet>,
    preselectedPetId: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String, String, String, String, String) -> Unit
) {
    var petId by remember { mutableStateOf(preselectedPetId) }
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Consulta Veterinária") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf("Consulta Veterinária", "Banho e Tosa", "Exame", "Cuidado Adicional")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cadastrar na Agenda", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PetSelectorDropdown(
                    pets = pets,
                    selectedPetId = petId,
                    onPetSelected = { if (it != null) petId = it },
                    allowAllOption = false,
                    label = "Pet"
                )
                // Seletor de categoria (RF11, RF12, RF14)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    categories.take(3).forEachIndexed { idx, cat ->
                        SegmentedButton(
                            selected = category == cat,
                            onClick = { category = cat },
                            shape = SegmentedButtonDefaults.itemShape(index = idx, count = 3)
                        ) {
                            Text(cat.split(" ").first(), fontSize = 11.sp)
                        }
                    }
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Atividade / Motivo *") },
                    placeholder = { Text("Ex: Consulta de rotina, Banho...") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Data *") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Hora *") },
                        placeholder = { Text("14:30") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Clínica / Veterinário / Local") },
                    singleLine = true
                )
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
                    if (title.isNotBlank() && date.isNotBlank()) {
                        onConfirm(
                            petId,
                            title,
                            category,
                            date,
                            time.ifBlank { "09:00" },
                            location,
                            notes
                        )
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
