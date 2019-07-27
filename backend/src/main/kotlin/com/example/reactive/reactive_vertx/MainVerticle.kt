package com.example.reactive.reactive_vertx

import com.example.reactive.reactive_vertx.handler.KafkaEventHandler
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord
import java.util.*

val KAFKA_QUEUE = "kafkaqueue"
val KAFKA_URL = "kafka:9092"

class MainVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {

    val startTime = System.currentTimeMillis()

    val server = vertx.createHttpServer()

    val router = Router.router(vertx)

    router.route()
      .handler(StaticHandler.create())
      .failureHandler {
        println("fooo cannot find this shit\n")
        it.response().end("time error asynchron response\n")
      }

    router.route("/kafka")
      .handler(KafkaEventHandler(vertx))
      .failureHandler {
        println("kafka error asynchron response\n")
        it.response().end("kafka error asynchron response\n")
      }

    val producer: KafkaProducer<String, String> = createKafkaProducer()
    router.route("/event*").handler(BodyHandler.create());
    router.post("/event")
      .handler{
        val record: KafkaProducerRecord<String, String> = KafkaProducerRecord.create(KAFKA_QUEUE, it.bodyAsString)
        producer.write(record)
        it.response()
          .setStatusCode(201)
          .putHeader("content-type", "application/text; charset=utf-8")
          .end("send : ${it.bodyAsString}");
      }

    server.requestHandler(router).listen(8080) { http ->
      if (http.succeeded()) {
        startFuture.complete()
        val startTimeDone = System.currentTimeMillis() - startTime
        println("HTTP server started on port 8080 in $startTimeDone ms on event loop thread ${Thread.currentThread()}")
      } else {
        startFuture.fail(http.cause());
      }
    }
  }

  private fun createKafkaProducer(): KafkaProducer<String, String> {
    val config_producer = HashMap<String, String>()
    config_producer["bootstrap.servers"] = KAFKA_URL
    config_producer["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    config_producer["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    config_producer["acks"] = "1"

    val producer: KafkaProducer<String, String> = KafkaProducer.create(vertx, config_producer)
    return producer
  }
}
