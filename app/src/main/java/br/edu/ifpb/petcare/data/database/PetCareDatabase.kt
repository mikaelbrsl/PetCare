package br.edu.ifpb.petcare.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifpb.petcare.data.dao.PetCareDao
import br.edu.ifpb.petcare.data.model.*

@Database(
    entities = [
        Pet::class,
        Appointment::class,
        HealthRecord::class,
        PetDocument::class,
        PetProduct::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PetCareDatabase : RoomDatabase() {
    abstract fun petCareDao(): PetCareDao

    companion object {
        @Volatile
        private var INSTANCE: PetCareDatabase? = null

        fun getDatabase(context: Context): PetCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetCareDatabase::class.java,
                    "petcare_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
