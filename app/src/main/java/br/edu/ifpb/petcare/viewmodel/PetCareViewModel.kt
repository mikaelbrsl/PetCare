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

    init {
        // Povoa automaticamente o banco na primeira vez que o app é aberto
        seedSampleDataFromSlides()
    }

    // Popula 3 animais diferentes (Cachorro, Gato e Ave) com remédios, vacinas, agenda, documentos e produtos
    fun seedSampleDataFromSlides() {
        viewModelScope.launch {
            // Verifica direto no banco se já existem pets cadastrados para não duplicar
            if (dao.getAllPets().first().isNotEmpty()) return@launch

            // ==================== 1. ANIMAIS (3 Espécies Diferentes) ====================
            val thorId = dao.insertPet(
                Pet(
                    name = "Thor",
                    species = "Cachorro",
                    breed = "Golden Retriever",
                    age = "3 anos",
                    birthDate = "14/08/2023",
                    description = "Porte grande, pelagem dourada, muito dócil e alérgico a picada de pulga."
                )
            ).toInt()

            val mingauId = dao.insertPet(
                Pet(
                    name = "Mingau",
                    species = "Gato",
                    breed = "Siamês",
                    age = "2 anos",
                    birthDate = "10/01/2024",
                    description = "Pelagem curta, olhos azuis, castrado e calmo."
                )
            ).toInt()

            val piuId = dao.insertPet(
                Pet(
                    name = "Piu",
                    species = "Ave",
                    breed = "Calopsita",
                    age = "1 ano",
                    birthDate = "05/09/2025",
                    description = "Mansa, asas aparadas, adora cantar pela manhã."
                )
            ).toInt()

            // ==================== 2. REMÉDIOS E VACINAS (HealthRecord) ====================
            // Registros do Thor (Cachorro)
            dao.insertHealthRecord(
                HealthRecord(
                    petId = thorId,
                    type = "Remédio",
                    name = "Simparic 80mg (Antipulgas e Carrapatos)",
                    dosageOrBatch = "1 comprimido mastigável",
                    applicationDate = "10/11/2026",
                    nextDueDate = "15/12/2026",
                    notes = "Dar após a refeição da manhã"
                )
            )
            dao.insertHealthRecord(
                HealthRecord(
                    petId = thorId,
                    type = "Vacina",
                    name = "Vacina Polivalente V10 + Antirrábica",
                    dosageOrBatch = "Dose Anual - Lote V10-442",
                    applicationDate = "15/08/2026",
                    nextDueDate = "15/08/2027",
                    notes = "Aplicada na Clínica VetCare sem reações"
                )
            )

            // Registros do Mingau (Gato)
            dao.insertHealthRecord(
                HealthRecord(
                    petId = mingauId,
                    type = "Remédio",
                    name = "Vermífugo Milbemax Gatos",
                    dosageOrBatch = "1 comprimido (16mg/4mg)",
                    applicationDate = "01/11/2026",
                    nextDueDate = "01/02/2027",
                    notes = "Administrado misturado ao sachê de salmão"
                )
            )
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

            // Registros do Piu (Calopsita)
            dao.insertHealthRecord(
                HealthRecord(
                    petId = piuId,
                    type = "Remédio",
                    name = "Avitrin Complexo Vitamínico",
                    dosageOrBatch = "5 gotas no bebedouro (50ml)",
                    applicationDate = "12/11/2026",
                    nextDueDate = "22/11/2026",
                    notes = "Suplementação para período de muda de penas (trocar água todo dia)"
                )
            )

            // ==================== 3. AGENDA (Appointment) ====================
            dao.insertAppointment(
                Appointment(
                    petId = thorId,
                    title = "Banho, Hidratação e Tosa Higiênica",
                    category = "Banho e Tosa",
                    date = "20/11/2026",
                    time = "14:00",
                    locationOrVet = "PetShop Amigo Fiel",
                    notes = "Usar shampoo hipoalergênico e limpar bem os ouvidos"
                )
            )
            dao.insertAppointment(
                Appointment(
                    petId = mingauId,
                    title = "Check-up Renal e Exame de Sangue",
                    category = "Consulta Veterinária",
                    date = "25/11/2026",
                    time = "09:30",
                    locationOrVet = "Clínica VetCare - Dra. Camila",
                    notes = "Levar em jejum de 8 horas"
                )
            )
            dao.insertAppointment(
                Appointment(
                    petId = piuId,
                    title = "Corte de Asas e Revisão de Bico/Unhas",
                    category = "Consulta Veterinária",
                    date = "02/12/2026",
                    time = "16:00",
                    locationOrVet = "Consultório Aves & Exóticos - Dr. Rafael",
                    notes = "Levar na gaiola de transporte coberta com tecido leve"
                )
            )

            // ==================== 4. DOCUMENTOS (PetDocument) ====================
            dao.insertDocument(
                PetDocument(
                    petId = thorId,
                    title = "Certificado de Microchipagem e RGA",
                    docType = "Identificação",
                    issueDate = "20/10/2023",
                    documentNumberOrCode = "CHIP-981020000458123",
                    summary = "Registro Geral do Animal e código de microchip subcutâneo."
                )
            )
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
            dao.insertDocument(
                PetDocument(
                    petId = piuId,
                    title = "Certificado de Sexagem por DNA",
                    docType = "Exame Laboratorial",
                    issueDate = "15/10/2025",
                    documentNumberOrCode = "LAB-DNA-77412",
                    summary = "Laudo genético confirmando sexo masculino da ave."
                )
            )

            // ==================== 5. PRODUTOS (PetProduct) ====================
            dao.insertProduct(
                PetProduct(
                    petId = thorId,
                    name = "Ração Premier Golden Retriever Adultos",
                    category = "Alimentação",
                    price = 239.90,
                    quantity = "Pacote 12 kg",
                    needsRestock = false
                )
            )
            dao.insertProduct(
                PetProduct(
                    petId = mingauId,
                    name = "Ração Super Premium Gatos Castrados Salmão",
                    category = "Alimentação",
                    price = 189.90,
                    quantity = "Pacote 10.1 kg",
                    needsRestock = false
                )
            )
            dao.insertProduct(
                PetProduct(
                    petId = mingauId,
                    name = "Areia Higiênica Biodegradável Viva Verde",
                    category = "Higiene",
                    price = 54.90,
                    quantity = "1 pacote de 4 kg",
                    needsRestock = true
                )
            )
            dao.insertProduct(
                PetProduct(
                    petId = piuId,
                    name = "Ração Extrusada Nutropica Calopsita Natural",
                    category = "Alimentação",
                    price = 38.50,
                    quantity = "Caixa 300g",
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