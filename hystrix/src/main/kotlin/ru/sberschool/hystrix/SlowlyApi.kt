package ru.sberschool.hystrix

import feign.RequestLine

interface SlowlyApi {
    @RequestLine("GET /pokemon/slowpoke")
    fun getPokemon(): Pokemon
}
