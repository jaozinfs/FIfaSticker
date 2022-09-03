package com.example.stickerapp.domain

import java.io.Serializable

data class Team(
    val name: String,
    val prefix: String,
    val position: Int,
    val stickers: List<Pair<Int, Boolean>>,
    val images: List<Pair<Int, String>> = emptyList()
) : Serializable {
    companion object {
        private const val TOTAL_STICKER = 20.0
        private const val PERCENT = 100
        fun progressOfStickersCollection(stickers: List<Pair<Int, Boolean>>) = stickers.filter { it.second }.size / TOTAL_STICKER * PERCENT
    }

    fun toSetOfStickers() = Array(20) { position ->
        stickers.find {
            it.first == position + 1
        } ?: position + 1 to false
    }
}

object TeamFactory {
    private val teams = listOf(
        qtar,
        ecuador,
        senegal,
        netherlands,
        england,
        irIran,
        usa,
        Wales,
        Argentina,
        SaudiArabia,
        Mexico,
        POLAND,
        France,
        Australia,
        Denmark,
        Tunisia,
        Spain,
        CostaRica,
        Germany,
        Japan,
        Belgium,
        Canada,
        Morocco,
        Croatia,
        Brazil,
        Serbia,
        Switzerland,
        Cameroon,
        Portugal,
        Ghana,
        KoreaRepublic
    )

    fun getTeams() = teams
}

val qtar = Team("QTAR", "QAT", 1, listOf())
val ecuador = Team("ECUADOR", "ECU", 2, listOf())
val senegal = Team("SENEGAL", "SEN", 3, listOf())
val netherlands = Team("NETHERLANDS", "NED", 4, listOf())
val england = Team("ENGLAND", "ENG", 5, listOf())
val irIran = Team("IR IRAN", "IRN", 6, listOf())
val usa = Team("USA", "USA", 7, listOf())
val Wales = Team("WALES", "WAL", 8, listOf())
val Argentina = Team("ARGENTINA", "ARG", 9, listOf())
val SaudiArabia = Team("SAUDI ARABIA", "KSA", 10, listOf())
val Mexico = Team("MEXICO", "MEX", 11, listOf())
val POLAND = Team("POLAND", "MEX", 12, listOf())
val France = Team("FRANCE", "FRA", 10, listOf())
val Australia = Team("AUSTRALIA", "AUS", 13, listOf())
val Denmark = Team("DENMARK", "DEN", 14, listOf())
val Tunisia = Team("TUNISIA", "TUN", 15, listOf())
val Spain = Team("SPAIN", "ESP", 16, listOf())
val CostaRica = Team("COSTA RICA", "CRC", 17, listOf())
val Germany = Team("GERMANY", "GER", 18, listOf())
val Japan = Team("JAPAN", "JPN", 19, listOf())
val Belgium = Team("BELGIUM", "BEL", 20, listOf())
val Canada = Team("CANADA", "CAN", 21, listOf())
val Morocco = Team("MOROCCO", "MAR", 22, listOf())
val Croatia = Team("CROATIA", "CRO", 23, listOf())
val Brazil = Team("BRAZIL", "BRA", 24, listOf())
val Serbia = Team("SERBIA", "SRB", 25, listOf())
val Switzerland = Team("SWITZERLAND", "SUI", 26, listOf())
val Cameroon = Team("CAMEROON", "CMR", 27, listOf())
val Portugal = Team("PORTUGAL", "POR", 28, listOf())
val Ghana = Team("GHANA", "GHA", 29, listOf())
val KoreaRepublic = Team("KOREA REPUBLIC", "URU", 30, listOf())
