# Spring Cloud Stream sandbox for Kafka consumer

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

With `KafkaTransactionManager` and transactional call `PersonService.createAndSend()` produces these log entries:

```
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [PersonService.createAndSend]
[   scheduling-1] o.s.k.t.KafkaTransactionManager          : Initiating transaction commit
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@53
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@54
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@55
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@56
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [...] close(PT5S)
```
