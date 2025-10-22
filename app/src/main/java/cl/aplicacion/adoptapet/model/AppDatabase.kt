package cl.aplicacion.adoptapet.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.aplicacion.adoptapet.model.dao.MascotaDao
import cl.aplicacion.adoptapet.model.dao.SolicitudDao
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.model.entities.Solicitud

@Database(entities = [Mascota::class, Solicitud::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Definimos una función abstracta para CADA DAO que tengamos
    abstract fun mascotaDao(): MascotaDao
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        // @Volatile asegura que el valor de INSTANCE esté siempre actualizado
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // "synchronized" evita que dos hilos intenten crear la DB al mismo tiempo
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adoptapet_database" // Nombre del archivo de la DB
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}