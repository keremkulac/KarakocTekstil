package com.keremkulac.karakoctekstil.service

import com.keremkulac.karakoctekstil.model.ExchangeRateList
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyAPIService {

    //https://api.genelpara.com/embed/doviz.json
    private val BASE_URL ="https://api.genelpara.com/embed/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CurrencyAPI::class.java)

    fun getData() : Single<List<ExchangeRateList>>{
        return api.getCurrencies()
    }
}