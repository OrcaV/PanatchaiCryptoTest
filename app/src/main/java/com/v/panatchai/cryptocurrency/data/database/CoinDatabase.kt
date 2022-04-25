package com.v.panatchai.cryptocurrency.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.v.panatchai.cryptocurrency.data.dao.CoinDao
import com.v.panatchai.cryptocurrency.data.database.CoinDatabase.Companion.VERSION
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity

/**
 * Local Coins permanent storage.
 */
@Database(entities = [CoinEntity::class], version = VERSION)
abstract class CoinDatabase : RoomDatabase() {

    /**
     * Provide access to the Coin table.
     */
    abstract fun coinDao(): CoinDao

    companion object {
        /**
         * The current version of the database.
         */
        const val VERSION = 1
    }
}
