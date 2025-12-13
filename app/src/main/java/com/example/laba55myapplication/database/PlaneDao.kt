package com.example.laba55myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaneDao {

    @Query("SELECT * FROM planes_table ORDER BY icao24 ASC")
    fun getAllPlanes(): LiveData<List<PlaneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlane(plane: PlaneEntity)

    @Update
    suspend fun updatePlane(plane: PlaneEntity)

    @Delete
    suspend fun deletePlane(plane: PlaneEntity)

    @Query("SELECT * FROM planes_table WHERE icao24 = :id")
    suspend fun getPlaneById(id: String): PlaneEntity?
}