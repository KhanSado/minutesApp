package io.github.bersoncrios.minutesapp.models

import java.time.LocalTime

data class Minute (
    val local: String,
    val horaInicio: String,
    val horaFim: String,
    val data: String, // TODO: Passar para Date
    val pauta: String,


    val userId:String,
    var username: String
)