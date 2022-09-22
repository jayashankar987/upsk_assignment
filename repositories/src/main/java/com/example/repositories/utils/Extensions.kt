package com.example.repositories.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object Extensions {

    fun Float.roundOffDecimal(): String {
        val df = DecimalFormat("'\u20B9' #.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(this)
    }
}