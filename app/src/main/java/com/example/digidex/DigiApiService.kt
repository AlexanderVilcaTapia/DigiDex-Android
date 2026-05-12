package com.example.digidex

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DigiApiService {

    /**
     * Obtiene una página de Digimons.
     * @param pageSize cantidad de resultados por página (máx. recomendado: 30)
     * @param page     número de página (comienza en 0)
     */
    @GET("api/v1/digimon")
    suspend fun getDigimons(
        @Query("pageSize") pageSize: Int = 30,
        @Query("page")     page: Int     = 0
    ): DigimonListResponse

    /**
     * Obtiene el detalle completo de un Digimon por su ID.
     */
    @GET("api/v1/digimon/{id}")
    suspend fun getDigimonById(@Path("id") id: Int): DigimonDetail
}