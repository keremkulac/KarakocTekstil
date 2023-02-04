package com.keremkulac.karakoctekstil.model

data class Order(
    val patternName : String,
    val piece: String?,
    val remainderPiece: String?,
    val clothType : String,
    val series : String,
    val orderDate : String,
    val orderEndDate : String,
    val status : String,
    val orderID : String)