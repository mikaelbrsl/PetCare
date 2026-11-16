package br.edu.ifpb.petcare.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores extraídas diretamente dos protótipos dos slides (Penpot)
val PetCareGreen = Color(0xFF1F6341)      // Barra superior e Drawer verde escuro
val PetCareOrange = Color(0xFFE57C32)     // Botões CADASTRAR / SALVAR / Editar laranja
val PetCareBackground = Color(0xFFFBF9F3) // Fundo creme claro
val PetCareCardBg = Color(0xFFD6E2DC)     // Card arredondado verde-acinzentado claro
val PetCareTextDark = Color(0xFF1E2420)
val PetCareTextMuted = Color(0xFF54635A)

private val LightColorScheme = lightColorScheme(
    primary = PetCareGreen,
    onPrimary = Color.White,
    secondary = PetCareOrange,
    onSecondary = Color.White,
    background = PetCareBackground,
    onBackground = PetCareTextDark,
    surface = Color.White,
    onSurface = PetCareTextDark,
    surfaceVariant = PetCareCardBg,
    onSurfaceVariant = PetCareTextDark
)

@Composable
fun PetCareTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
