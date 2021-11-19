package ru.sberschool.hystrix

class FallbackSlowlyApi : SlowlyApi {

    override fun getPokemon(): Pokemon {
        return Pokemon(name = "fallback")
    }
}
