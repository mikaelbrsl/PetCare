package br.edu.ifpb.petcare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.petcare.data.database.PetCareDatabase
import br.edu.ifpb.petcare.data.model.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetCareViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = PetCareDatabase.getDatabase(application).petCareDao()

    val pets: StateFlow<List<Pet>> = dao.getAllPets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = dao.getAllAppointments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthRecords: StateFlow<List<HealthRecord>> = dao.getAllHealthRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<PetDocument>> = dao.getAllDocuments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<PetProduct>> = dao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Popula os pets do slide (Mingau, Kabeção, Mel, Juju) caso o usuário queira testar rápido
    fun seedSampleDataFromSlides() {
        viewModelScope.launch {
            if (pets.value.isNotEmpty()) return@launch
            val mingauId = dao.insertPet(
                Pet(
                    name = "Mingau",
                    species = "Gato",
                    breed = "Siamês",
                    age = "2 anos",
                    birthDate = "01/01/2024",
                    description = "Siamês, pelo curto, grande"
                )
            ).toInt()
            val kabecaoId = dao.insertPet(
                Pet(
                    name = "Kabeção",
                    species = "Gato",
                    breed = "SRD",
                    age = "8 meses",
                    birthDate = "10/03/2026",
                    description = "Gato preto muito brincalhão"
                )
            ).toInt()
            dao.insertPet(
                Pet(
                    name = "Mel",
                    species = "Gato",
                    breed = "Persa",
                    age = "2 anos",
                    birthDate = "15/05/2024",
                    description = "Calma, adora dormir na janela"
                )
            )
            dao.insertPet(
                Pet(
                    name = "Juju",
                    species = "Gato",
                    breed = "Frajola",
                    age = "8 meses",
                    birthDate = "20/03/2026",
                    description = "Muito esperta e carinhosa"
                )
            )

            // Agenda inicial de exemplo
            dao.insertAppointment(
                Appointment(
                    petId = mingauId,
                    title = "Banho e Tosa Higiênica",
                    category = "Banho e Tosa",
                    date = "20/11/2026",
                    time = "14:30",
                    locationOrVet = "PetShop Amigo Fiel",
                    notes = "Usar shampoo neutro hipoalergênico"
                )
            )
            dao.insertAppointment(
                Appointment(
                    petId = kabecaoId,
                    title = "Check-up Veterinário Semestral",
                    category = "Consulta Veterinária",
                    date = "25/11/2026",
                    time = "09:00",
                    locationOrVet = "Clínica VetCare - Dra. Camila",
                    notes = "Avaliar peso e dentição"
                )
            )

            // Vacina e Remédio de exemplo
            dao.insertHealthRecord(
                HealthRecord(
                    petId = mingauId,
                    type = "Vacina",
                    name = "Quádrupla Felina (V4)",
                    dosageOrBatch = "Dose Anual - Lote 982A",
                    applicationDate = "10/02/2026",
                    nextDueDate = "10/02/2027",
                    notes = "Sem reações adversas"
                )
            )
            dao.insertHealthRecord(
                HealthRecord(
                    petId = kabecaoId,
                    type = "Remédio",
                    name = "Vermífugo Milbemax",
                    dosageOrBatch = "1 comprimido (4mg)",
                    applicationDate = "01/11/2026",
                    nextDueDate = "01/02/2027",
                    notes = "Administrado junto com sachê"
                )
            )

            // Documento de exemplo
            dao.insertDocument(
                PetDocument(
                    petId = mingauId,
                    title = "Carteira de Vacinação Digital",
                    docType = "Carteira de Vacinação",
                    issueDate = "01/02/2024",
                    documentNumberOrCode = "REG-PB-2024-889",
                    summary = "Registro oficial de vacinas e vermifugação emitido pela clínica."
                )
            )

            // Produto de exemplo
            dao.insertProduct(
                PetProduct(
                    petId = mingauId,
                    name = "Ração Super Premium Gatos Castrados",
                    category = "Alimentação",
                    price = 189.90,
                    quantity = "Pacote 10.1 kg",
                    needsRestock = false
                )
            )
            dao.insertProduct(
                PetProduct(
                    petId = null,
                    name = "Areia Higiênica Biodegradável",
                    category = "Higiene",
                    price = 42.50,
                    quantity = "2 pacotes de 4kg",
                    needsRestock = true
                )
            )
        }
    }

    // CRUD Pets
    fun savePet(
        id: Int?,
        name: String,
        species: String,
        breed: String,
        age: String,
        birthDate: String,
        description: String,
        photoUri: String?
    ) {
        viewModelScope.launch {
            val pet = Pet(
                id = id ?: 0,
                name = name.trim(),
                species = species.trim(),
                breed = breed.trim(),
                age = age.trim(),
                birthDate = birthDate.trim(),
                description = description.trim(),
                photoUri = photoUri
            )
            if (id == null || id == 0) {
                dao.insertPet(pet)
            } else {
                dao.updatePet(pet)
            }
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch { dao.deletePet(pet) }
    }

    // CRUD Agenda
    fun addAppointment(
        petId: Int,
        title: String,
        category: String,
        date: String,
        time: String,
        locationOrVet: String,
        notes: String
    ) {
        viewModelScope.launch {
            dao.insertAppointment(
                Appointment(
                    petId = petId,
                    title = title.trim(),
                    category = category,
                    date = date.trim(),
                    time = time.trim(),
                    locationOrVet = locationOrVet.trim(),
                    notes = notes.trim()
                )
            )
        }
    }

    fun toggleAppointmentDone(appointment: Appointment) {
        viewModelScope.launch {
            dao.updateAppointment(appointment.copy(isDone = !appointment.isDone))
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch { dao.deleteAppointment(appointment) }
    }

    // CRUD Remédios e Vacinas
    fun addHealthRecord(
        petId: Int,
        type: String,
        name: String,
        dosageOrBatch: String,
        applicationDate: String,
        nextDueDate: String,
        notes: String
    ) {
        viewModelScope.launch {
            dao.insertHealthRecord(
                HealthRecord(
                    petId = petId,
                    type = type,
                    name = name.trim(),
                    dosageOrBatch = dosageOrBatch.trim(),
                    applicationDate = applicationDate.trim(),
                    nextDueDate = nextDueDate.trim(),
                    notes = notes.trim()
                )
            )
        }
    }

    fun deleteHealthRecord(record: HealthRecord) {
        viewModelScope.launch { dao.deleteHealthRecord(record) }
    }

    // CRUD Documentos
    fun addDocument(
        petId: Int,
        title: String,
        docType: String,
        issueDate: String,
        documentNumberOrCode: String,
        summary: String
    ) {
        viewModelScope.launch {
            dao.insertDocument(
                PetDocument(
                    petId = petId,
                    title = title.trim(),
                    docType = docType,
                    issueDate = issueDate.trim(),
                    documentNumberOrCode = documentNumberOrCode.trim(),
                    summary = summary.trim()
                )
            )
        }
    }

    fun deleteDocument(document: PetDocument) {
        viewModelScope.launch { dao.deleteDocument(document) }
    }

    // CRUD Produtos
    fun addProduct(
        petId: Int?,
        name: String,
        category: String,
        price: Double,
        quantity: String,
        needsRestock: Boolean
    ) {
        viewModelScope.launch {
            dao.insertProduct(
                PetProduct(
                    petId = petId,
                    name = name.trim(),
                    category = category,
                    price = price,
                    quantity = quantity.trim(),
                    needsRestock = needsRestock
                )
            )
        }
    }

    fun toggleProductRestock(product: PetProduct) {
        viewModelScope.launch {
            dao.updateProduct(product.copy(needsRestock = !product.needsRestock))
        }
    }

    fun deleteProduct(product: PetProduct) {
        viewModelScope.launch { dao.deleteProduct(product) }
    }
}
