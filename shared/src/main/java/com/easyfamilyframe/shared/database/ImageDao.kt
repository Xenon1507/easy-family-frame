package com.easyfamilyframe.shared.database

import androidx.room.*
import com.easyfamilyframe.shared.models.ImageData
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images ORDER BY dateAdded DESC")
    fun getAllImages(): Flow<List<ImageData>>

    @Query("SELECT * FROM images ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomImages(limit: Int): List<ImageData>

    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImageById(id: Long): ImageData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageData>)

    @Update
    suspend fun updateImage(image: ImageData)

    @Delete
    suspend fun deleteImage(image: ImageData)

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun deleteImageById(id: Long)

    @Query("DELETE FROM images")
    suspend fun deleteAllImages()

    @Query("SELECT COUNT(*) FROM images")
    suspend fun getImageCount(): Int

    @Query("UPDATE images SET lastShown = :timestamp, showCount = showCount + 1 WHERE id = :id")
    suspend fun markImageAsShown(id: Long, timestamp: Long)
}
