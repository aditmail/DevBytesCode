package com.example.devbytesexercice.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2

/** Creating DAO For Query **/
@Dao
interface VideoDao {
    //Get All Data from Database
    @Query("SELECT * FROM tb_video")
    fun getListVideos(): LiveData<List<DatabaseVideo>>

    @Query("SELECT * FROM tb_video WHERE url = :url_id")
    fun getDetailVideo(url_id: String): LiveData<DatabaseVideo>

    //Inserting Data from Network to Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}
/** Creating DAO For Query **/

/** DAO For User Profile.. **/
@Dao
interface ProfileDao {
    @Query("SELECT * FROM tb_profile")
    fun getListProfile(): LiveData<List<DatabaseProfile>>

    @Query("SELECT * FROM tb_profile WHERE id = :id_profile")
    fun getDetailProfile(id_profile: Long): LiveData<DatabaseProfile>

    @Insert
    fun insertProfile(profile: DatabaseProfile)

    @Update
    fun updateProfile(profile: DatabaseProfile)

    @Query("DELETE FROM tb_profile")
    fun clearData()
}
/** DAO For User Profile.. **/

/** ------------------------------------------------------------------
Creating Database.. **/
@Database(
    entities = [DatabaseVideo::class, DatabaseProfile::class],
    version = 2,
    exportSchema = true
)
abstract class DevBytesDatabase : RoomDatabase() {
    abstract val videoDAO: VideoDao
    abstract val profileDAO: ProfileDao
}
/** Creating Database.. **/

/** Set Singleton for Database So the UI Can Access It.. **/
private lateinit var INSTANCE: DevBytesDatabase

fun getDatabase(context: Context): DevBytesDatabase {
    synchronized(DevBytesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                DevBytesDatabase::class.java,
                "db_video"
            ).fallbackToDestructiveMigration()
                //.addMigrations(com.example.devbytesexercice.database.MIGRATION_1_2)
                .build()
        }
    }

    return INSTANCE
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `tb_profile`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `username` TEXT, `email` TEXT, `phoneNumber` TEXT, `password` TEXT, `photo` TEXT )")
    }
}
/** Set Singleton for Database So the UI Can Access It..
------------------------------------------------------------------ **/