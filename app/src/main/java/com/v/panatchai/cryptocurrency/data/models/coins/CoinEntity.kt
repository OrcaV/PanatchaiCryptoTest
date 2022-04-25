package com.v.panatchai.cryptocurrency.data.models.coins

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.v.panatchai.cryptocurrency.data.models.BaseEntity
import com.v.panatchai.cryptocurrency.data.models.coins.CoinEntity.Companion.TABLE

/**
 * Represent Data-Layer Coin Model.
 */
@Entity(tableName = TABLE)
data class CoinEntity(
    @field:Json(name = "id") @PrimaryKey val id: String,
    @field:Json(name = "name") @ColumnInfo(name = "name") val name: String,
    @field:Json(name = "symbol") @ColumnInfo(name = "symbol") val symbol: String,
    @field:Json(name = "icon") @ColumnInfo(name = "icon") val icon: String = ""
) : BaseEntity() {
    companion object {
        const val TABLE = "Coins"
    }
}
