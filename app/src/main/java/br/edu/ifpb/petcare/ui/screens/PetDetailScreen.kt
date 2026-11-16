package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.*
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@Composable
fun PetDetailScreen(
    pet: Pet?,
    appointments: List<Appointment>,
    healthRecords: List<HealthRecord>,
    documents: List<PetDocument>,
    onEditPet: (Int) -> Unit,
    onOpenAgendaForPet: (Int) -> Unit,
    onOpenMedicationsForPet: (Int) -> Unit,
    onOpenDocumentsForPet: (Int) -> Unit
) {
    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pet não encontrado.")
        }
        return
    }

    val petAppointments = appointments.filter { it.petId == pet.id }
    val petHealth = healthRecords.filter { it.petId == pet.id }
    val petDocs = documents.filter { it.petId == pet.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card Principal do Pet (RF15)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = PetCareCardBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(PetCareGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pet.name.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pet.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("${pet.species} • Raça: ${pet.breed.ifBlank { "Não informada" }}")
                        Text("Idade: ${pet.age.ifBlank { "-" }} | Nasc: ${pet.birthDate.ifBlank { "-" }}")
                    }
                    IconButton(onClick = { onEditPet(pet.id) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PetCareOrange)
                    }
                }
                if (pet.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Observações: ${pet.description}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Text(
            text = "Árvore de Cuidados de ${pet.name}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        // Botões rápidos filtrados por este pet
        CareShortcutCard(
            title = "Agenda, Consultas e Histórico",
            subtitle = "${petAppointments.count { !it.isDone }} pendente(s) • ${petAppointments.count { it.isDone }} concluído(s)",
            icon = Icons.Default.CalendarMonth,
            onClick = { onOpenAgendaForPet(pet.id) }
        )

        CareShortcutCard(
            title = "Vacinas e Remédios",
            subtitle = "${petHealth.count { it.type == "Vacina" }} vacina(s) • ${petHealth.count { it.type == "Remédio" }} remédio(s)",
            icon = Icons.Default.Medication,
            onClick = { onOpenMedicationsForPet(pet.id) }
        )

        CareShortcutCard(
            title = "Documentos do Animal",
            subtitle = "${petDocs.size} documento(s) anexado(s)",
            icon = Icons.Default.Description,
            onClick = { onOpenDocumentsForPet(pet.id) }
        )
    }
}

@Composable
private fun CareShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PetCareGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = PetCareGreen)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 13.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}
