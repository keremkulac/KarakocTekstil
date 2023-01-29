package com.keremkulac.karakoctekstil.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateList(
    @SerializedName("USD")
    val usd : List<Map<String,ExchangeRateModel>>
)
