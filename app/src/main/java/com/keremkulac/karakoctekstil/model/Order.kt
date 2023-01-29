package com.keremkulac.karakoctekstil.model

data class Order(
    val patternName : String,
    val piece: String?,
    val clothType : String,
    val series : String,
    val date : String,
    val status : String)