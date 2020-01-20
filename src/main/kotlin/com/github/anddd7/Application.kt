package com.github.anddd7

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
