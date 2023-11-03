# Spring Cloud Stream sandbox for JPA and Kafka producer

This is sandbox for [Synchronizing with External Transaction Managers in Spring Cloud Stream Kafka Applications](https://spring.io/blog/2023/10/04/synchronizing-with-external-transaction-managers-in-spring-cloud-stream)

## Scenario 1: Producer-initiated transactions

Kafka producer is transactional (see `transaction-id-prefix`is sent in `application.yml`)
```
spring.cloud:
  stream:
    kafka:
      binder:
        transaction:
          transaction-id-prefix: sandbox-tx-
```

Without `KafkaTransactionManager` bean defined transactional call `PersonService.createAndSend()` produces these log entries:

```
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [PersonService.createAndSend]
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [PersonService.createAndSend]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] commitTransaction()
[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Sent ok: ProducerRecord(...), metadata: demo.tx.out.send-person-0@93
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] close(PT5S)
```

If exception happen:

```
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [PersonService.createAndSend]
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [BEGIN]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.findAll]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Getting transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [SimpleJpaRepository.save]
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] beginTransaction()
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sending: ProducerRecord(...)
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] send(ProducerRecord(...))
[   scheduling-1] o.s.kafka.core.KafkaTemplate             : Sent: ProducerRecord(...)
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [END]
[   scheduling-1] i.k.s.s.t.p.service.PersonService        : [EXCEPTION] Some error is happen
[   scheduling-1] o.s.t.i.TransactionInterceptor           : Completing transaction for [PersonService.createAndSend] after exception: java.lang.RuntimeException
[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [KafkaProducer@4fdeb921] abortTransaction()
[   scheduling-1] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-sandbox-tx-0, transactionalId=sandbox-tx-0] Aborting incomplete transaction
[er-sandbox-tx-0] o.s.k.support.LoggingProducerListener    : Exception thrown when sending a message with key='null' and payload='byte[34]' to topic demo.tx.out.send-person:

org.apache.kafka.common.errors.TransactionAbortedException: Failing batch since transaction was aborted

[er-sandbox-tx-0] o.s.kafka.core.KafkaTemplate             : Failed to send: ProducerRecord(...)

org.apache.kafka.common.errors.TransactionAbortedException: Failing batch since transaction was aborted

[   scheduling-1] o.s.k.core.DefaultKafkaProducerFactory   : CloseSafeProducer [delegate=org.apache.kafka.clients.producer.KafkaProducer@4fdeb921] close(PT5S)
```