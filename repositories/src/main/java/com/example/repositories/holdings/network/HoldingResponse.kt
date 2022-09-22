package com.example.repositories.holdings.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class HoldingResponse(
    @field:Json(name = "client_id") val clientId: String,
    @field:Json(name = "data") val `data`: List<Data>,
    @field:Json(name = "error") val error: Any?,
    @field:Json(name = "response_type") val responseType: String,
    @field:Json(name = "timestamp") val timestamp: Long
) {
    @Entity(tableName = "holdings")
    data class Data(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
        @field:Json(name = "avg_price") @ColumnInfo(name = "avg_price") val avgPrice: String,
        @field:Json(name = "close") @ColumnInfo(name = "close") val close: Double,
        @field:Json(name = "cnc_used_quantity") @ColumnInfo(name = "cnc_used_quantity") val cncUsedQuantity: Int,
        @field:Json(name = "collateral_qty") @ColumnInfo(name = "collateral_qty") val collateralQty: Int,
        @field:Json(name = "collateral_type") @ColumnInfo(name = "collateral_type") val collateralType: String,
        @field:Json(name = "collateral_update_qty") @ColumnInfo(name = "collateral_update_qty") val collateralUpdateQty: Int,
        @field:Json(name = "company_name") @ColumnInfo(name = "company_name") val companyName: String?,
        @field:Json(name = "haircut") @ColumnInfo(name = "haircut") val haircut: Double,
        @field:Json(name = "holdings_update_qty") @ColumnInfo(name = "holdings_update_qty") val holdingsUpdateQty: Int,
        @field:Json(name = "isin") @ColumnInfo(name = "isin") val isin: String,
        @field:Json(name = "ltp") @ColumnInfo(name = "ltp") val ltp: Double,
        @field:Json(name = "product") @ColumnInfo(name = "product") val product: String,
        @field:Json(name = "quantity") @ColumnInfo(name = "quantity") val quantity: Long,
        @field:Json(name = "symbol") @ColumnInfo(name = "symbol") val symbol: String,
        @field:Json(name = "t1_holding_qty") @ColumnInfo(name = "t1_holding_qty") val t1HoldingQty: Int,
        @field:Json(name = "token_bse") @ColumnInfo(name = "token_bse") val tokenBse: String,
        @field:Json(name = "token_nse") @ColumnInfo(name = "token_nse") val tokenNse: String,
        @field:Json(name = "withheld_collateral_qty") @ColumnInfo(name = "withheld_collateral_qty") val withheldCollateralQty: Int,
        @field:Json(name = "withheld_holding_qty") @ColumnInfo(name = "withheld_holding_qty") val withheldHoldingQty: Int
    )
}