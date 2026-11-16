package br.edu.ifpb.petcare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCareTopBar(
    title: String = "PetCare",
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menu de funcionalidades",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PetCareGreen
        )
    )
}

@Composable
fun OrangePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = PetCareOrange,
            contentColor = Color.White
        ),
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth(0.75f)
    ) {
        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SuggestionBannerCard(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PetCareCardBg,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PetCareGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = PetCareGreen
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = PetCareGreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetSelectorDropdown(
    pets: List<Pet>,
    selectedPetId: Int?,
    onPetSelected: (Int?) -> Unit,
    allowAllOption: Boolean = true,
    label: String = "Filtrar por Pet"
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedPetName = when (selectedPetId) {
        null -> if (allowAllOption) "Todos os Pets" else "Selecione um Pet"
        else -> pets.find { it.id == selectedPetId }?.name ?: "Selecione um Pet"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedPetName,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (allowAllOption) {
                DropdownMenuItem(
                    text = { Text("Todos os Pets") },
                    onClick = {
                        onPetSelected(null)
                        expanded = false
                    }
                )
            }
            pets.forEach { pet ->
                DropdownMenuItem(
                    text = { Text("${pet.name} (${pet.species})") },
                    onClick = {
                        onPetSelected(pet.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
