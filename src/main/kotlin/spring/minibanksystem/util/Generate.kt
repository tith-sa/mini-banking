package spring.minibanksystem.util

import kotlin.random.Random

fun generatedAccountNumber(length: Int = 10): String{
    return (1..length)
        .map{ Random.nextInt(0,10) }
        .joinToString("")
}