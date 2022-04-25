package com.v.panatchai.cryptocurrency.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity

/**
 * Representing Coin in the database.
 *
 * Note: If you wonder why I do not pass the field name instead of having separated queries,
 *      The reason is that a dynamic query does not work with Paging3.
 */
@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coins: List<CoinEntity>)

    @Query("SELECT * FROM ${CoinEntity.TABLE} WHERE name LIKE '%' || :filter || '%'  ORDER BY name COLLATE NOCASE ASC")
    fun filterByAsc(filter: String): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM ${CoinEntity.TABLE} WHERE name LIKE '%' || :filter || '%'  ORDER BY name COLLATE NOCASE DESC")
    fun filterByDesc(filter: String): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM ${CoinEntity.TABLE} ORDER BY name COLLATE NOCASE ASC")
    fun selectAllByAsc(): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM ${CoinEntity.TABLE} ORDER BY name COLLATE NOCASE DESC")
    fun selectAllByDesc(): PagingSource<Int, CoinEntity>

    @Query("SELECT COUNT(id) FROM ${CoinEntity.TABLE} ")
    fun count(): Int

    @Query("DELETE FROM ${CoinEntity.TABLE}")
    suspend fun clear()

    // for testing purposes
    @Query("SELECT * FROM ${CoinEntity.TABLE}")
    suspend fun selectAll(): List<CoinEntity>
}
