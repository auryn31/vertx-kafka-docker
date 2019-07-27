package com.example.reactive.reactive_vertx.service

import com.example.reactive.reactive_vertx.KAFKA_QUEUE
import com.example.reactive.reactive_vertx.KAFKA_URL
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import io.vertx.core.Vertx
import io.vertx.kafka.client.consumer.KafkaConsumer
import java.util.HashMap

object KafkaEventListener {


  var kafkaSharedFlowable: Flowable<String>? = null

  fun getFlowable(vertx: Vertx): Flowable<String> {

    if(kafkaSharedFlowable == null) {

    val config = HashMap<String, String>()
    config["bootstrap.servers"] = KAFKA_URL
    config["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
    config["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
    config["group.id"] = "my_group"
    config["auto.offset.reset"] = "latest" //earliest --> startet am anfang
    config["enable.auto.commit"] = "false"

    val consumer: KafkaConsumer<String, String> = KafkaConsumer.create(vertx, config)

    val processor = PublishProcessor.create<String>()

    consumer.subscribe(KAFKA_QUEUE)

    consumer.handler{
        processor.onNext(it.value())
      }

      kafkaSharedFlowable = processor.share()
    }

    return kafkaSharedFlowable!!
  }

}
