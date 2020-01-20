package com.github.anddd7.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
  @Bean
  fun webClient(downstream: Downstream) =
      WebClient.builder().baseUrl(downstream.stock).build()
}

@Configuration
@ConfigurationProperties("downstream")
class Downstream {
  var stock: String = ""
}
