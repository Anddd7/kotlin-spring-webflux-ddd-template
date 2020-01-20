package com.github.anddd7.adapter.inbound

import com.github.anddd7.adapter.EndPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloController(private val environment: Environment) : EndPoint {
  @Value("\${image.version:development}")
  private lateinit var imageVersion: String

  @GetMapping("/profile")
  fun profile() = mapOf(
      "build.env" to environment.activeProfiles,
      "image.version" to imageVersion
  )

  @GetMapping("/hello")
  fun hello() = "Hello, this is kotlin-spring-webflux-ddd !"
}
