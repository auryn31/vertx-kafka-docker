package com.example.reactive.reactive_vertx

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.vertx.junit5.VertxExtension
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import rx.plugins.RxJavaPlugins
import rx.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class FlowableTest {

  @Test
  fun testFlowable() {

    val flow = listOf(1, 2, 3, 4).map {
      Flowable.fromArray(listOf(1, it))
    }.reduce { a, b ->
      a.mergeWith(b)
    }


    flow.subscribe{
      print(it)
    }

  }

  @Test
  fun testFlowable2() {

    val flow = Flowable.fromArray(listOf(1, 2))

    flow.subscribe{
      print(it)
    }

  }
}
