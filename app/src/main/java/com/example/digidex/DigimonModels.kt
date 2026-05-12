package com.example.digidex

import com.google.gson.annotations.SerializedName

// ── Respuesta del listado paginado ──────────────────────
data class DigimonListResponse(
    @SerializedName("content")  val content: List<DigimonBasic>,
    @SerializedName("pageable") val pageable: Pageable
)

data class DigimonBasic(
    @SerializedName("id")    val id: Int,
    @SerializedName("name")  val name: String,
    @SerializedName("href")  val href: String,
    @SerializedName("image") val image: String
)

data class Pageable(
    @SerializedName("currentPage")    val currentPage: Int,
    @SerializedName("elementsOnPage") val elementsOnPage: Int,
    @SerializedName("totalElements")  val totalElements: Int,
    @SerializedName("totalPages")     val totalPages: Int
)

// ── Detalle completo de un Digimon ──────────────────────
data class DigimonDetail(
    @SerializedName("id")          val id: Int,
    @SerializedName("name")        val name: String,
    @SerializedName("xAntibody")   val xAntibody: Boolean,
    @SerializedName("images")      val images: List<DigimonImage>,
    @SerializedName("levels")      val levels: List<DigimonLevel>,
    @SerializedName("attributes")  val attributes: List<DigimonAttribute>,
    @SerializedName("fields")      val fields: List<DigimonField>,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("descriptions") val descriptions: List<DigimonDescription>,
    @SerializedName("skills")      val skills: List<DigimonSkill>
)

data class DigimonImage(
    @SerializedName("href")    val href: String,
    @SerializedName("primary") val primary: Boolean
)

data class DigimonLevel(
    @SerializedName("id")    val id: Int,
    @SerializedName("level") val level: String
)

data class DigimonAttribute(
    @SerializedName("id")        val id: Int,
    @SerializedName("attribute") val attribute: String
)

data class DigimonField(
    @SerializedName("id")    val id: Int,
    @SerializedName("field") val field: String,
    @SerializedName("image") val image: String
)

data class DigimonDescription(
    @SerializedName("origin")      val origin: String,
    @SerializedName("language")    val language: String,
    @SerializedName("description") val description: String
)

data class DigimonSkill(
    @SerializedName("id")          val id: Int,
    @SerializedName("skill")       val skill: String,
    @SerializedName("translation") val translation: String?,
    @SerializedName("description") val description: String
)