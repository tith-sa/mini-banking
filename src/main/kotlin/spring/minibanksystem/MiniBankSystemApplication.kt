package spring.minibanksystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MiniBankSystemApplication

fun main(args: Array<String>) {
    runApplication<MiniBankSystemApplication>(*args)
}
