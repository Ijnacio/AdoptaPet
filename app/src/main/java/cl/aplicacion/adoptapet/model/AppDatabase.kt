package cl.aplicacion.adoptapet.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.aplicacion.adoptapet.model.dao.MascotaDao
import cl.aplicacion.adoptapet.model.dao.SolicitudDao
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.model.entities.Solicitud
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Mascota::class, Solicitud::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mascotaDao(): MascotaDao
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adoptapet_database"
                )
                    .addCallback(AppDatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.mascotaDao())
                }
            }
        }

        private suspend fun populateDatabase(mascotaDao: MascotaDao) {

            val mascota1 = Mascota(
                nombre = "Brisa",
                tipo = "Perro",
                fotoUri = "https://i5.walmartimages.com/asr/2b1ae954-9915-4f96-bc11-b279a6c07c93.85d1648e0862d9d81fed4633d993686e.png?odnHeight=612&odnWidth=612&odnBg=FFFFFF",
                raza = "Kiltro Mestizo",
                edad = 2,
                vacunasAlDia = true,
                motivoAdopcion = "Encontrada en la calle",
                descripcion = "Muy juguetona y amigable. Le encanta correr.",
                nombreContacto = "Fundación Patitas",
                telefonoContacto = "912345678"
            )
            mascotaDao.insertarMascota(mascota1)

            val mascota2 = Mascota(
                nombre = "Milo",
                tipo = "Gato",
                fotoUri = "https://cloudfront-us-east-1.images.arcpublishing.com/abccolor/L5KTZNPZI5D7HEGBMF335RC3MM.jpg",
                raza = "Siamés",
                edad = 1,
                vacunasAlDia = false,
                motivoAdopcion = "Dueño se muda de país",
                descripcion = "Tranquilo y le gusta dormir. Ideal para depto.",
                nombreContacto = "Ana Gómez",
                telefonoContacto = "987654321"
            )
            mascotaDao.insertarMascota(mascota2)

            val mascota3 = Mascota(
                nombre = "Tambor",
                tipo = "Conejo",
                fotoUri = "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRpgpxBiQckHcUKdch28aGlqhj217J-_4NZ6sWuWvkl3dyyaiq8kNipO8vHz76L6_ZLlLe8QG9hEaR1KlCYhDUh9sJFnG6y3ZWCOsCAfFs",
                raza = "Cabeza de León",
                edad = 1,
                vacunasAlDia = true,
                motivoAdopcion = "Hija desarrolló alergia",
                descripcion = "Muy tierno y tranquilo. Le gusta la zanahoria.",
                nombreContacto = "Familia Pérez",
                telefonoContacto = "911112222"
            )
            mascotaDao.insertarMascota(mascota3)

            val mascota4 = Mascota(
                nombre = "Maní",
                tipo = "Hámster/Roedor",
                fotoUri = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQQfz4RBHj8jagtLHzmG411hB627224SnCrNNW8pCoQG3mOKMhVR-guRTM35qHIejHOW_B6hCFt1rIVjn95VpBAMc9AKfiqd5Sqng0NkZg",
                raza = "Sirio",
                edad = 1,
                vacunasAlDia = false,
                motivoAdopcion = "Falta de tiempo para cuidarlo",
                descripcion = "Pequeño y curioso. Activo principalmente de noche.",
                nombreContacto = "Luis Soto",
                telefonoContacto = "933334444"
            )
            mascotaDao.insertarMascota(mascota4)

            val mascota5 = Mascota(
                nombre = "Cuco",
                tipo = "Reptil (Exótico)",
                fotoUri = "https://misanimales.com/wp-content/uploads/2016/01/animales-exoticos-reptiles.jpg",
                raza = "Lagartija Verde",
                edad = 1,
                vacunasAlDia = false,
                motivoAdopcion = "Dueño ya no puede cuidarlo",
                descripcion = "Come insectos, necesita terrario con calor.",
                nombreContacto = "Carlos Ruiz",
                telefonoContacto = "955556666"
            )
            mascotaDao.insertarMascota(mascota5)
        }
    }
}