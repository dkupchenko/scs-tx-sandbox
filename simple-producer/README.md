# Spring Cloud Stream sandbox for Kafka producer 

This is sandbox for [Producer Initiated Transactions in Spring Cloud Stream Kafka Applications](https://spring.io/blog/2023/09/28/producer-initiated-transactions-in-spring-cloud-stream-kafka-applications)

Kafka producer is transactional (see `transaction-id-prefix`is sent in `application.yml`)
```
spring.cloud:
  stream:
    kafka:
      binder:
        replication-factor: 1
        min-partition-count: 1
        transaction:
          transaction-id-prefix: sandbox-tx-
          producer.configuration:
            retries: 5
            acks: all
```

Without any transactional definitions `PersonService.createAndSave()` produces these log entries:

```
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: ...
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
```

If Kafka producer isn't configured as transactional

```
spring.cloud:
  stream:
    kafka:
      binder:
        replication-factor: 1
        min-partition-count: 1
#        transaction:
#          transaction-id-prefix: sandbox-tx-
#          producer.configuration:
#            retries: 5
#            acks: all
```

then

```
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
```