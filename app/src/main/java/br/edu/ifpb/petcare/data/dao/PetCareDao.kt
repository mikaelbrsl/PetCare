package br.edu.ifpb.petcare.data.dao

import androidx.room.*
import br.edu.ifpb.petcare.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PetCareDao {

    // --- PETS (RF01 a RF08) ---
    @Query("SELECT * FROM pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    suspend fun getPetById(id: Int): Pet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet): Long

    @Update
    suspend fun updatePet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    // --- AGENDA / CONSULTAS / HISTÓRICO (RF11 a RF15) ---
    @Query("SELECT * FROM appointments ORDER BY isDone ASC, date ASC, time ASC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    // --- REMÉDIOS E VACINAS (RF09, RF10) ---
    @Query("SELECT * FROM medications_vaccines ORDER BY applicationDate DESC")
    fun getAllHealthRecords(): Flow<List<HealthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: HealthRecord)

    @Delete
    suspend fun deleteHealthRecord(record: HealthRecord)

    // --- DOCUMENTOS (RF16, RF17, RF18, RF22) ---
    @Query("SELECT * FROM documents ORDER BY issueDate DESC")
    fun getAllDocuments(): Flow<List<PetDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: PetDocument)

    @Delete
    suspend fun deleteDocument(document: PetDocument)

    // --- PRODUTOS (RF21) ---
    @Query("SELECT * FROM products ORDER BY needsRestock DESC, name ASC")
    fun getAllProducts(): Flow<List<PetProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: PetProduct)

    @Update
    suspend fun updateProduct(product: PetProduct)

    @Delete
    suspend fun deleteProduct(product: PetProduct)
}
