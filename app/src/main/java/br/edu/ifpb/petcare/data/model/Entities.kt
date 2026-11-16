package br.edu.ifpb.petcare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val species: String, // Cão, Gato, Ave, etc. (RF04)
    val breed: String,   // Raça (RF05)
    val age: String,     // Idade ex: "2 anos", "8 meses" (RF03)
    val birthDate: String, // Data de nascimento dd/mm/aaaa (Mockup)
    val description: String, // Descrição adicional (Mockup)
    val photoUri: String? = null // URI da foto ou avatar
)

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val title: String,       // Ex: Banho e Tosa, Consulta Dr. Carlos
    val category: String,    // Consulta Veterinária, Banho e Tosa, Cuidado Adicional (RF11, RF12, RF14)
    val date: String,        // dd/mm/aaaa
    val time: String,        // HH:mm
    val locationOrVet: String,
    val notes: String,
    val isDone: Boolean = false // Permite compor o histórico de atendimentos (RF13)
)

@Entity(
    tableName = "medications_vaccines",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class HealthRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val type: String,        // "Vacina" (RF09) ou "Remédio" (RF10)
    val name: String,        // Ex: V10, Antirrábica, Bravecto
    val dosageOrBatch: String, // Ex: 1 comprimido, Dose Anual
    val applicationDate: String, // Data aplicada
    val nextDueDate: String, // Próxima dose / reforço
    val notes: String
)

@Entity(
    tableName = "documents",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class PetDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,          // Associado a um animal específico (RF18)
    val title: String,       // Ex: Carteira de Vacinação, RG Pet, Exame de Sangue (RF16)
    val docType: String,     // Ex: Exame, Receita, Registro, Atestado
    val issueDate: String,
    val documentNumberOrCode: String,
    val summary: String      // Resumo/link/observações do documento (RF17)
)

@Entity(tableName = "products")
data class PetProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int? = null,  // Pode ser geral ou de um pet específico (RF21)
    val name: String,        // Ex: Ração Golden Gatos Castrados 10kg
    val category: String,    // Ração, Higiene, Brinquedo, Acessório
    val price: Double,
    val quantity: String,
    val needsRestock: Boolean = false // Lista de compras vs Em estoque
)
