package com.github.anddd7.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.ReactorContext
import org.slf4j.MDC
import reactor.core.publisher.Mono
import reactor.util.context.Context

const val CORRELATION_ID_KEY = "correlationId"

fun <T> Context.getOrNull(key: String): T? =
    if (hasKey(key)) get(key) else null

@ExperimentalCoroutinesApi
suspend fun context(): Context? = kotlin.coroutines.coroutineContext[ReactorContext]?.context


/**
 * logging with context in reactor Mono
 */
fun <T> Mono<T>.logOnNext(block: (T) -> Unit): Mono<T> =
    zipWith(Mono.subscriberContext()) { t, ctx ->
      MDC.putCloseable(CORRELATION_ID_KEY, ctx.getOrNull(CORRELATION_ID_KEY)).use {
        block(t)
      }
      t
    }

/**
 * logging with context in coroutine
 */
@ExperimentalCoroutinesApi
suspend fun <T> reactorContext(block: (Context) -> T): T {
  val ctx = context() ?: Context.empty()
  val correlationId = ctx.getOrNull<String>(CORRELATION_ID_KEY)

  return MDC.putCloseable(CORRELATION_ID_KEY, correlationId).use { block(ctx) }
}
