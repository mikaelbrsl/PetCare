package br.edu.ifpb.petcare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.petcare.data.model.Pet
import br.edu.ifpb.petcare.ui.components.OrangePrimaryButton
import br.edu.ifpb.petcare.ui.components.SuggestionBannerCard
import br.edu.ifpb.petcare.ui.theme.PetCareCardBg
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareOrange
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    pets: List<Pet>,
    onAddPetClick: () -> Unit,
    onEditPetClick: (Int) -> Unit,
    onPetClick: (Int) -> Unit,
    onNavigateAgenda: () -> Unit,
    onNavigateMedications: () -> Unit,
    onSeedSampleData: () -> Unit
) {
    if (pets.isEmpty()) {
        // Estado 1 do Slide 4: "Tela inicial sem pets cadastrados"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(PetCareCardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        tint = PetCareGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nenhum Pet cadastrado",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                OrangePrimaryButton(
                    text = "CADASTRAR",
                    onClick = onAddPetClick
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onSeedSampleData) {
                    Text(
                        text = "Carregar pets de demonstração do slide (Mingau, Kabeção, Mel, Juju)",
                        color = PetCareGreen,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }

            // Cards de sugestão idênticos ao Slide 4
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SuggestionBannerCard(
                    text = "Sugestão: adicione o próximo banho e tosa do seu pet",
                    onClick = onNavigateAgenda
                )
                SuggestionBannerCard(
                    text = "Sugestão: adicione a rotina de tratamento do seu pet",
                    onClick = onNavigateMedications
                )
            }
        }
    } else {
        // Estado 2 do Slide 5: "Tela inicial com pets cadastrados" ("Seus pets")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seus pets",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(pets, key = { it.id }) { pet ->
                    PetPillCard(
                        pet = pet,
                        onCardClick = { onPetClick(pet.id) },
                        onEditClick = { onEditPetClick(pet.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OrangePrimaryButton(
                text = "CADASTRAR",
                onClick = onAddPetClick
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PetPillCard(
    pet: Pet,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = PetCareCardBg,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto circular do pet à esquerda (Slide 5)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PetCareGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (!pet.photoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = pet.photoUri,
                        contentDescription = pet.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = pet.name.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = PetCareGreen
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nome e "Espécie, Idade" (ex: Mingau / Gato, 2 anos)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                val subtitle = buildString {
                    append(pet.species)
                    if (pet.age.isNotBlank()) append(", ${pet.age}")
                    else if (pet.breed.isNotBlank()) append(" • ${pet.breed}")
                }
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF4A5850)
                )
            }

            // Botão laranja de edição (Slide 5)
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PetCareOrange)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${pet.name}",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
