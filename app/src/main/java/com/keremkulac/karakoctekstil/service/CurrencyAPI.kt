package com.keremkulac.karakoctekstil.service

import com.keremkulac.karakoctekstil.model.ExchangeRateList
import io.reactivex.Single
import retrofit2.http.GET

interface CurrencyAPI {
    @GET("doviz.json")
    fun getCurrencies(): Single<List<ExchangeRateList>>
}