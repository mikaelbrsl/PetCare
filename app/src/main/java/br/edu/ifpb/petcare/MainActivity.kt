package br.edu.ifpb.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import br.edu.ifpb.petcare.ui.components.PetCareTopBar
import br.edu.ifpb.petcare.ui.navigation.Screen
import br.edu.ifpb.petcare.ui.screens.*
import br.edu.ifpb.petcare.ui.theme.PetCareGreen
import br.edu.ifpb.petcare.ui.theme.PetCareTheme
import br.edu.ifpb.petcare.viewmodel.PetCareViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetCareTheme {
                PetCareApp()
            }
        }
    }
}

@Composable
fun PetCareApp(viewModel: PetCareViewModel = viewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pets by viewModel.pets.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val healthRecords by viewModel.healthRecords.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val products by viewModel.products.collectAsState()
    val apiDogImageUrl by viewModel.apiDogImageUrl.collectAsState()
    val apiLoading by viewModel.apiLoading.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val drawerItems = listOf(
        "Home" to Screen.Home.route,
        "Remédios" to Screen.Medications.createRoute(null),
        "Produtos" to Screen.Products.route,
        "Agenda" to Screen.Agenda.createRoute(null),
        "Documentos" to Screen.Documents.createRoute(null),
        "Cadastrar" to Screen.PetForm.createRoute(null)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = PetCareGreen,
                drawerContentColor = Color.White,
                modifier = Modifier.width(260.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "PetCare",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(12.dp))

                drawerItems.forEach { (label, targetRoute) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate(targetRoute) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                PetCareTopBar(
                    title = "PetCare",
                    showBack = currentRoute != Screen.Home.route,
                    onBackClick = { navController.popBackStack() },
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        pets = pets,
                        apiImageUrl = apiDogImageUrl,
                        apiLoading = apiLoading,
                        onRefreshApi = { viewModel.fetchRandomPetFromApi() },
                        onAddPetClick = { navController.navigate(Screen.PetForm.createRoute(null)) },
                        onEditPetClick = { petId -> navController.navigate(Screen.PetForm.createRoute(petId)) },
                        onPetClick = { petId -> navController.navigate(Screen.PetDetail.createRoute(petId)) },
                        onNavigateAgenda = { navController.navigate(Screen.Agenda.createRoute(null)) },
                        onNavigateMedications = { navController.navigate(Screen.Medications.createRoute(null)) },
                        onSeedSampleData = { viewModel.seedSampleDataFromSlides() }
                    )
                }

                composable(
                    route = Screen.PetForm.route,
                    arguments = listOf(
                        navArgument("petId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val petIdArg = backStackEntry.arguments?.getInt("petId") ?: -1
                    val existingPet = pets.find { it.id == petIdArg }
                    PetFormScreen(
                        existingPet = existingPet,
                        onSavePet = { id, name, species, breed, age, birthDate, description, photoUri ->
                            viewModel.savePet(id, name, species, breed, age, birthDate, description, photoUri)
                        },
                        onDeletePet = { pet -> viewModel.deletePet(pet) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.PetDetail.route,
                    arguments = listOf(navArgument("petId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val petId = backStackEntry.arguments?.getInt("petId") ?: -1
                    val pet = pets.find { it.id == petId }
                    PetDetailScreen(
                        pet = pet,
                        appointments = appointments,
                        healthRecords = healthRecords,
                        documents = documents,
                        onEditPet = { id -> navController.navigate(Screen.PetForm.createRoute(id)) },
                        onOpenAgendaForPet = { id -> navController.navigate(Screen.Agenda.createRoute(id)) },
                        onOpenMedicationsForPet = { id -> navController.navigate(Screen.Medications.createRoute(id)) },
                        onOpenDocumentsForPet = { id -> navController.navigate(Screen.Documents.createRoute(id)) }
                    )
                }

                composable(
                    route = Screen.Agenda.route,
                    arguments = listOf(
                        navArgument("petId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val arg = backStackEntry.arguments?.getInt("petId") ?: -1
                    AgendaScreen(
                        pets = pets,
                        appointments = appointments,
                        initialPetId = if (arg != -1) arg else null,
                        onAddAppointment = { petId, title, category, date, time, loc, notes ->
                            viewModel.addAppointment(petId, title, category, date, time, loc, notes)
                        },
                        onToggleDone = { viewModel.toggleAppointmentDone(it) },
                        onDeleteAppointment = { viewModel.deleteAppointment(it) }
                    )
                }

                composable(
                    route = Screen.Medications.route,
                    arguments = listOf(
                        navArgument("petId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val arg = backStackEntry.arguments?.getInt("petId") ?: -1
                    MedicationsScreen(
                        pets = pets,
                        records = healthRecords,
                        initialPetId = if (arg != -1) arg else null,
                        onAddRecord = { petId, type, name, dosage, appDate, nextDate, notes ->
                            viewModel.addHealthRecord(petId, type, name, dosage, appDate, nextDate, notes)
                        },
                        onDeleteRecord = { viewModel.deleteHealthRecord(it) }
                    )
                }

                composable(
                    route = Screen.Documents.route,
                    arguments = listOf(
                        navArgument("petId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val arg = backStackEntry.arguments?.getInt("petId") ?: -1
                    DocumentsScreen(
                        pets = pets,
                        documents = documents,
                        initialPetId = if (arg != -1) arg else null,
                        onAddDocument = { petId, title, docType, issueDate, code, summary ->
                            viewModel.addDocument(petId, title, docType, issueDate, code, summary)
                        },
                        onDeleteDocument = { viewModel.deleteDocument(it) }
                    )
                }

                composable(Screen.Products.route) {
                    ProductsScreen(
                        pets = pets,
                        products = products,
                        onAddProduct = { petId, name, category, price, qty, restock ->
                            viewModel.addProduct(petId, name, category, price, qty, restock)
                        },
                        onToggleRestock = { viewModel.toggleProductRestock(it) },
                        onDeleteProduct = { viewModel.deleteProduct(it) }
                    )
                }
            }
        }
    }
}