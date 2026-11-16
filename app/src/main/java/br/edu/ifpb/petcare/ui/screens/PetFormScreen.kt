package br.edu.ifpb.petcare.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.ui.components.OrangePrimaryButton
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import coil.compose.AsyncImage

@Composable
fun PetFormScreen(
    existingPet: Pet?,
    onSavePet: (Int?, String, String, String, String, String, String, String?) -> Unit,
    onDeletePet: (Pet) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember(existingPet) { mutableStateOf(existingPet?.name ?: "") }
    var species by remember(existingPet) { mutableStateOf(existingPet?.species ?: "") }
    var breed by remember(existingPet) { mutableStateOf(existingPet?.breed ?: "") }
    var age by remember(existingPet) { mutableStateOf(existingPet?.age ?: "") }
    var birthDate by remember(existingPet) { mutableStateOf(existingPet?.birthDate ?: "") }
    var description by remember(existingPet) { mutableStateOf(existingPet?.description ?: "") }
    var photoUri by remember(existingPet) { mutableStateOf(existingPet?.photoUri) }
    var showError by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

    val isEditing = existingPet != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isEditing) "Editar Pet" else "Cadastrar Pet",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Avatar circular ("Adicionar foto" conforme Slide 4 e 5)
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(PetCareCardBg)
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (!photoUri.isNullOrBlank()) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Foto do pet",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Adicionar foto",
                    tint = PetCareGreen,
                    modifier = Modifier.size(38.dp)
                )
            }
        }
        TextButton(onClick = { imagePicker.launch("image/*") }) {
            Text(
                text = if (photoUri == null) "Adicionar foto" else "Trocar foto",
                color = PetCareGreen,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campos exigidos nos Requisitos Funcionais (RF01-RF07) e Protótipo (Slide 4/5)
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                showError = false
            },
            label = { Text("Nome do pet *") },
            placeholder = { Text("Ex: Mingau") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Espécie *") },
                placeholder = { Text("Cachorro, gato...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Raça (RF05)") },
                placeholder = { Text("Ex: Siamês, SRD") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Idade (RF03)") },
                placeholder = { Text("Ex: 2 anos, 8 meses") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Data de nascimento") },
                placeholder = { Text("dd/mm/aaaa") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") },
            placeholder = { Text("Ex: Siamês, pelo curto, grande...") },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        if (showError) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Informe pelo menos o nome e a espécie do pet.",
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        OrangePrimaryButton(
            text = "SALVAR",
            onClick = {
                if (name.isBlank() || species.isBlank()) {
                    showError = true
                } else {
                    onSavePet(
                        existingPet?.id,
                        name,
                        species,
                        breed,
                        age,
                        birthDate,
                        description,
                        photoUri
                    )
                    onNavigateBack()
                }
            }
        )

        // RF07 - O sistema deve permitir excluir um animal cadastrado
        if (isEditing && existingPet != null) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    onDeletePet(existingPet)
                    onNavigateBack()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Excluir Pet")
            }
        }
    }
}
