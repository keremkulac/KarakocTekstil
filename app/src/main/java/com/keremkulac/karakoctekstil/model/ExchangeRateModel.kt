package com.keremkulac.karakoctekstil.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateModel(
    @SerializedName("satis")
    val satis : String,
    @SerializedName("alis")
    val alis : String,
    @SerializedName("degisim")
    val degisim : String
)
