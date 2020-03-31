package com.github.anddd7.adapter.inbound

import com.github.anddd7.adapter.EndPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloController : EndPoint {
  @Value("\${image.version:development}")
  private lateinit var imageVersion: String

  @Value("\${APP_ENV:local}")
  private lateinit var appEnv: String

  @GetMapping("/profile")
  fun profile() = mapOf(
      "build.env" to appEnv,
      "image.version" to imageVersion
  )

  @GetMapping("/hello")
  fun hello() = "Hello, this is kotlin-spring-webflux-ddd !"

  @GetMapping("/ping")
  fun ping() = PONG

  companion object {
    private const val PONG = "pong"
  }
}
